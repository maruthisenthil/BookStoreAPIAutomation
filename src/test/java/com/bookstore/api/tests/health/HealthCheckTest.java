package com.bookstore.api.tests.health;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.bookstore.api.base.BaseTest;
import com.bookstore.api.constants.AuthType;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class HealthCheckTest extends BaseTest {

	@Test(priority =1, groups="positive")
	public void bookStoreUpHealthCheck() {
		Response responseGet = restClient.get(BASE_URL_BOOKSTORE, BOOKSTORE_HEALTH_ENDPOINT, null, null, AuthType.NO_AUTH, ContentType.JSON);
 		Assert.assertEquals(responseGet.getHeader("Content-Type"), "application/json");
		Assert.assertTrue(responseGet.statusLine().contains("OK"));
		Assert.assertEquals(responseGet.statusCode(), 200);
	}
}