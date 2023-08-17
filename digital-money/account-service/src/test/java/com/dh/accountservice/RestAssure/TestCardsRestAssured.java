package com.dh.accountservice.RestAssure;

import com.dh.accountservice.entities.AccountTransactionsDTO;
import com.dh.accountservice.entities.Transaction;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Random;

public class TestCardsRestAssured {

    @Autowired
    TestAccountsRestAssured accountsRestAssured;
    String baseUrl = "http://localhost:8084/";
    private String cardId = "";
    private String cardNumber = "";

    @Test
    public void TestSaveCardForAccount() {
        String url = baseUrl + "accounts/1/cards";

        cardNumber = generateRandomCardNumber();

        String requestBody =
                "{\"type\": \"Crédito\"," +
                        " \"balance\": 0.0," +
                        " \"cardNumber\": \"" + cardNumber + "\"," +
                        "\"accountHolder\": \"Edi\", " +
                        "\"expireDate\": \"2023-06-09T14:30:00\"," +
                        "\"bankEntity\": \"Banco Internacional\"}";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200, statusCode);

        JsonObject responseBody = JsonParser.parseString(response.getBody().asString()).getAsJsonObject();
        cardId = responseBody.get("id").getAsString();
    }

    @Test
    public void testDeleteCardById() {
        TestSaveCardForAccount();
        String endpointDeleteCardById = "accounts/1/cards/" + cardId;
        String url = baseUrl + endpointDeleteCardById;
        Response response = RestAssured.delete(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void testDeleteFailedCardById() {
        cardId = "999";
        String endpointDeleteCardById = "accounts/1/cards/" + cardId;
        String url = baseUrl + endpointDeleteCardById;
        Response response = RestAssured.delete(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    public void testFindAllCardsByAccountId() {
        String endpointFindAllCardsByAccountId = "accounts/1/cards";
        String url = baseUrl + endpointFindAllCardsByAccountId;
        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200, statusCode);

        JsonObject responseBody = JsonParser.parseString(response.getBody().asString()).getAsJsonObject();
        JsonArray cards = responseBody.getAsJsonArray("cards");
        Assertions.assertTrue(responseBody.size() > 0);
    }

    @Test
    public void testFindAllCardsByAccountIdFail() {
        String accountId = "99";
        String endpointFindAllCardsByAccountId = "accounts/"+accountId + "/cards";
        String url = baseUrl + endpointFindAllCardsByAccountId;
        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(404, statusCode);

        String stringResponse = response.getBody().asString();
        Assertions.assertEquals(stringResponse, "We don't found any userAccount with the id: "+ accountId);
    }

    @Test
    public void testFindAccountWithCard() {
        TestSaveCardForAccount();
        String accountId = "1";
        String endpointFindAllCardsByAccountId = "accounts/"+accountId + "/cards/" + cardId;
        String url = baseUrl + endpointFindAllCardsByAccountId;

        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200, statusCode);

        JsonObject responseBody = JsonParser.parseString(response.getBody().asString()).getAsJsonObject();
        String accountIdToTest = responseBody.get("id").getAsString();
        Assertions.assertEquals(accountIdToTest, accountId);

        JsonObject card = responseBody.getAsJsonObject("cards");
        String cardIdToTest = card.get("id").getAsString();
        Assertions.assertEquals(cardIdToTest, cardId);
    }

    @Test
    public void testFindAccountWithCardFail() {
        String accountId = "999";
        String cardId = "999";
        String endpointFindAllCardsByAccountId = "accounts/"+accountId+"/cards/"+cardId+"";
        String url = baseUrl + endpointFindAllCardsByAccountId;

        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(404, statusCode);

        String stringResponse = response.getBody().asString();

        boolean isValidError = stringResponse.equals("We don't found any userAccount with the id: " + accountId);
       //        || stringResponse.equals("No card found with ID: " + cardId);

        Assertions.assertTrue(isValidError);
    }

    private String generateRandomCardNumber() {
        String characters = "0123456789";
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            cardNumber.append(randomChar);
        }

        return cardNumber.toString();
    }
}
