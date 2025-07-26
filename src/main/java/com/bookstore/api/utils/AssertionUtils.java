package com.bookstore.api.utils;

import io.restassured.response.Response;
import org.testng.Assert;

public class AssertionUtils {

    public static void assertForbiddenTokenResponse(Response response) {
        Assert.assertEquals(response.getHeader("Content-Type"), "application/json");
        Assert.assertEquals(response.statusCode(), 403);
        Assert.assertTrue(response.statusLine().contains("Forbidden"));
        Assert.assertEquals(response.jsonPath().getString("detail"), "Invalid token or expired token");
    }
    
    public static void assertNotAuthenticatedResponse(Response response) {
        Assert.assertEquals(response.getHeader("Content-Type"), "application/json");
        Assert.assertEquals(response.statusCode(), 403);
        Assert.assertTrue(response.statusLine().contains("Forbidden"));
        Assert.assertEquals(response.jsonPath().getString("detail"), "Not authenticated");
    }
    
    public static void assertBookNotFoundResponse(Response response) {
        Assert.assertEquals(response.getHeader("Content-Type"), "application/json");
        Assert.assertTrue(response.statusLine().contains("Not Found"));
        Assert.assertEquals(response.jsonPath().getString("detail"), "Book not found");
    }

}
