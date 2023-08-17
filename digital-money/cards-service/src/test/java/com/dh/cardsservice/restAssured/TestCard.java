package com.dh.cardsservice.restAssured;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class TestCard {

    String baseUrl = "http://localhost:8085/";
    private String cardId = "";
    private String cardNumber = "";
    private String existingCardNumber = "";

    @Test
    public void testSaveCard() {

        String url = baseUrl + "cards";

        cardNumber = generateRandomCardNumber();

        String requestBody =
                "{\"type\": \"Crédito\"," +
                        " \"balance\": 0.0," +
                        "\"accountId\": 1," +
                        " \"cardNumber\": \"" + cardNumber + "\"," +
                        "\"accountHolder\": \"Edi\", " +
                        "\"expireDate\": \"2026-06-09T14:30:00\"," +
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

    //TODO prueba no pasa porque no se retorna un status 409
    @Test
    public void testSaveCardFail() {
        testFindCardById();

        String url = baseUrl + "accounts";

        String requestBody =
                "{\"type\": \"Crédito\"," +
                        " \"balance\": 0.0," +
                        "\"accountId\": 999," +
                        " \"cardNumber\": \"" + cardNumber + "\"," +
                        "\"accountHolder\": \"Edi\", " +
                        "\"expireDate\": \"2025-06-09T14:30:00\"," +
                        "\"bankEntity\": \"Banco Internacional\"}";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(409, statusCode);
    }

    @Test
    public void testDeleteCardById() {
        testSaveCard();
        String endpointDeleteCardById = "cards/accounts/" + cardId;
        String url = baseUrl + endpointDeleteCardById;
        Response response = RestAssured.delete(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200, statusCode);
    }

    @Test
    public void testDeleteCardByIdFail() {
        cardId = "999";
        String endpointDeleteCardById = "accounts/" + cardId;
        String url = baseUrl + endpointDeleteCardById;
        Response response = RestAssured.delete(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    public void testSearchCardsByAccountId() {
        String accountId = "1";
        String endpointSearchCardsByAccountId = "cards/accounts/" + accountId;
        String url = baseUrl + endpointSearchCardsByAccountId;
        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200, statusCode);

        JsonArray responseBody = JsonParser.parseString(response.getBody().asString()).getAsJsonArray();
        Assertions.assertTrue(responseBody.size() > 0);

    }

    @Test
    public void testSearchCardsByAccountIdFail() {
        String accountId = "999";
        String endpointSearchCardsByAccountId = "cards/accounts/" + accountId;
        String url = baseUrl + endpointSearchCardsByAccountId;
        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(404, statusCode);

//        JsonArray responseBody = JsonParser.parseString(response.getBody().asString()).getAsJsonArray();
//        Assertions.assertTrue(responseBody.size() == 0);
    }

    @Test
    public void testFindCardById() {
        testSaveCard();
        String endpointFindCardById = "cards/" + cardId;
        String url = baseUrl + endpointFindCardById;
        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200, statusCode);

        JsonObject responseBody = JsonParser.parseString(response.getBody().asString()).getAsJsonObject();
        String id = responseBody.get("id").getAsString();
        Assertions.assertEquals(id ,cardId);
        cardNumber = responseBody.get("cardNumber").getAsString();
    }

    @Test
    public void testFindCardByIdFail() {
        String endpointFindCardById = "cards/" + 999;
        String url = baseUrl + endpointFindCardById;
        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    public void testUpdateCardBalance() {

        testSaveCard();

        String updateBalance = "1000.0";
        String endpointUpdateCardBalance = "cards/" + cardId;
        String url = baseUrl + endpointUpdateCardBalance;

        String endpointFindCardById = "cards/" + cardId;
        String urlFindCard = baseUrl + endpointFindCardById;

        String requestBody =
                "{\"balance\":" +  updateBalance + " }";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .put(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(201, statusCode);

        Response responseFindCard = RestAssured.get(url);

        JsonObject responseBody = JsonParser.parseString(responseFindCard.getBody().asString()).getAsJsonObject();
        String balance = responseBody.get("balance").getAsString();
        Assertions.assertEquals(balance ,updateBalance);
    }

    @Test
    public void testUpdateCardBalanceFail() {
        testSaveCard();
        String endpointUpdateCardBalance = "cards/" + cardId;
        String url = baseUrl + endpointUpdateCardBalance;

        String requestBody =
                "{\"balance\": -1000.0 }";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .put(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(400, statusCode);
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

