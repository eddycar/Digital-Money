package com.dh.accountservice.RestAssure;

import com.dh.accountservice.entities.AccountTransactionsDTO;
import com.dh.accountservice.entities.Transaction;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestTransactionRestAssured {

    String baseUrl = "http://localhost:8084/";

    @Test
    public void testFindAllTransactionsByAccountId(){
        String endpointGetAllActivities = "accounts/1/activity";
        String url = baseUrl + endpointGetAllActivities;
        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200, statusCode);

        AccountTransactionsDTO account = response.as(AccountTransactionsDTO.class);
        List<Transaction> transactions = account.getTransactions();

        Assertions.assertTrue(transactions.size() > 0);

    }

    @Test
    public void testFindAllTransactionsByAccountIdFail(){
        String accountID = "999";
        String endpointGetAllActivities = "accounts/"+ accountID+ "/activity";
        String url = baseUrl + endpointGetAllActivities;
        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    public void testFindTransactionByAccountId(){
        String accountId = "1";
        String transactionId = "11";
        String endpointFindTransactionByAccountId = "accounts/" + accountId + "/activity/" + transactionId;
        String url = baseUrl + endpointFindTransactionByAccountId;
        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200, statusCode);

        JsonObject responseBody = JsonParser.parseString(response.getBody().asString()).getAsJsonObject();
        String accountIdToTest = responseBody.get("id").getAsString();
        Assertions.assertEquals(accountIdToTest, accountId);

        JsonObject transaction = responseBody.getAsJsonObject("transaction");
        String transactionIdToTest = transaction.get("id").getAsString();
        Assertions.assertEquals(transactionIdToTest, transactionId);
    }

    @Test
    public void testFindTransactionByAccountIdFail() {
        String accountId = "1";
        String transactionId = "999";
        String endpointFindTransactionByAccountId = "accounts/" + accountId + "/activity/" + transactionId;
        String url = baseUrl + endpointFindTransactionByAccountId;
        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(404, statusCode);
    }

    @Test
    public void testFilterActivityByAproximateAmount(){
        String endpointFilterActivityByAproximateAmount = "accounts/1/activity";
        String rangeA = "?rangeA=0&";
        String rangeB = "rangeB=1000";
        String url = baseUrl + endpointFilterActivityByAproximateAmount+ rangeA + rangeB;
        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(200, statusCode);

        AccountTransactionsDTO account = response.as(AccountTransactionsDTO.class);
        List<Transaction> transactions = account.getTransactions();

        Assertions.assertTrue(transactions.size() > 0 && transactions.size() < 6);

    }

    @Test
    public void testFilterActivityByAproximateAmountFail(){
        String accountId = "999";
        String endpointFilterActivityByAproximateAmount = "accounts/" + accountId + "/activity";
        String rangeA = "?rangeA=0&";
        String rangeB = "rangeB=1000";
        String url = baseUrl + endpointFilterActivityByAproximateAmount+ rangeA + rangeB;
        Response response = RestAssured.get(url);
        int statusCode = response.getStatusCode();
        Assertions.assertEquals(404, statusCode);

    }
}
