package com.bookstore.api.tests.user;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.bookstore.api.base.BaseTest;
import com.bookstore.api.constants.AuthType;
import com.bookstore.api.manager.ConfigManagerOne;
import com.bookstore.api.manager.TokenManager;
import com.bookstore.api.model.UserTestDataFactory;
import com.bookstore.api.pojo.User;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class LoginUserTest extends BaseTest{
	
	@Test(priority =1, groups="positive")
	public void loginUserTest() {
		
		
		Response responsePost = restClient.post(BASE_URL_BOOKSTORE, BOOKSTORE_LOGIN_ENDPOINT, 
				TokenManager.getLoginCredentials(), null, null, AuthType.NO_AUTH, ContentType.JSON);
		Assert.assertEquals(responsePost.getHeader("Content-Type"), "application/json");
		Assert.assertEquals(responsePost.statusCode(), 200);
		Assert.assertTrue(responsePost.statusLine().contains("OK"));
		Assert.assertEquals(responsePost.jsonPath().getString("token_type"), "bearer");
		ConfigManagerOne.set("bookstore_bearertoken", responsePost.jsonPath().getString("access_token"));
	}

	
	@Test(priority =2, groups="negative")
	public void loginUserInvalidUserTest() {
		
		User user= UserTestDataFactory.createValidNewUser();
		
		Response responsePost = restClient.post(BASE_URL_BOOKSTORE, BOOKSTORE_LOGIN_ENDPOINT, 
				user, null, null, AuthType.NO_AUTH, ContentType.JSON);
		
		Assert.assertEquals(responsePost.getHeader("Content-Type"), "application/json");
		Assert.assertEquals(responsePost.statusCode(), 400);
		Assert.assertTrue(responsePost.statusLine().contains("Bad Request"));
		Assert.assertEquals(responsePost.jsonPath().getString("detail"), "Incorrect email or password");
	}
	
}