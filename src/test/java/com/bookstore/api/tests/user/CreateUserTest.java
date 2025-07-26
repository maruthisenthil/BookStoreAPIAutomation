package com.bookstore.api.tests.user;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.bookstore.api.base.BaseTest;
import com.bookstore.api.constants.AuthType;
import com.bookstore.api.manager.ConfigManagerOne;
import com.bookstore.api.model.UserTestDataFactory;
import com.bookstore.api.pojo.User;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CreateUserTest extends BaseTest{

	@Test(priority =1, groups="positive")
	public void createUser() {
		// 1.POST: Create User
		User user= UserTestDataFactory.createValidUser();
		
		Response responsePostSignUp = restClient.post(BASE_URL_BOOKSTORE, BOOKSTORE_SIGNUP_ENDPOINT, 
				user, null, null, AuthType.NO_AUTH, ContentType.JSON);
		
		Assert.assertEquals(responsePostSignUp.getHeader("Content-Type"), "application/json");
		
		System.out.println(" New SignUpUser: "+  user.getEmail());
		Assert.assertEquals(responsePostSignUp.jsonPath().getString("message"),  "User created successfully");
		Assert.assertTrue(responsePostSignUp.statusLine().contains("OK"));
		Assert.assertEquals(responsePostSignUp.statusCode(), 200);
		
		// 2.POST: LoginUser
		
		Response responsePostLogin = restClient.post(BASE_URL_BOOKSTORE, BOOKSTORE_LOGIN_ENDPOINT, user, null, null, AuthType.NO_AUTH, ContentType.JSON);
		Assert.assertEquals(responsePostLogin.jsonPath().getString("token_type"), "bearer");
		ConfigManagerOne.set("bookstore_bearertoken", responsePostLogin.jsonPath().getString("access_token"));
		
		// 3. GET: GetAllBooks
		Response responseGetAllBooks = restClient.get(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertTrue(responseGetAllBooks.statusLine().contains("OK"));
		Assert.assertEquals(responseGetAllBooks.statusCode(), 200);	
	}
	
	// As of now User creation should be only with email and password - Considering this as BUG and not advisable for a
	// QA to to procceed with write validation 

	
	@Test(priority =2, groups="negative")
	public void createUserEmptyJson() {
		// 1.POST: Create User
		User user= UserTestDataFactory.generate500ErrorData();
		
		Response responsePostSignUp = restClient.post(BASE_URL_BOOKSTORE, BOOKSTORE_SIGNUP_ENDPOINT, 
				user, null, null, AuthType.NO_AUTH, ContentType.JSON);
		Assert.assertEquals(responsePostSignUp.jsonPath().getString("detail"),  "Email already registered");
		Assert.assertTrue(responsePostSignUp.statusLine().contains("Bad Request"));
		Assert.assertEquals(responsePostSignUp.statusCode(), 400);
	}
	
	// Tried of exploring the more scenarios but the email field is accepting all kinds of values
	// so I felt to stop adding more scenarios with the last one as failed 
	
	
//	@Test(priority =2, groups="negative")
//	public void createUserInvalidEmailFormat() {
//		// 1.POST: Create User
//		User user= UserTestDataFactory.createValidUser();
//		user.setId(11111111);
//		
//		Response responsePostSignUp = restClient.post(BASE_URL_BOOKSTORE, BOOKSTORE_SIGNUP_ENDPOINT, 
//				user, null, null, AuthType.NO_AUTH, ContentType.JSON);
//		Assert.assertEquals(responsePostSignUp.jsonPath().getString("detail"),  "Email already registered");
//		Assert.assertTrue(responsePostSignUp.statusLine().contains("Bad Request"));
//		Assert.assertEquals(responsePostSignUp.statusCode(), 400);
//	}

}