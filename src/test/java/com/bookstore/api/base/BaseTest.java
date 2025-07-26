package com.bookstore.api.base;


import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;

import com.aventstack.chaintest.plugins.ChainTestListener;
import com.bookstore.api.client.RestClient;
import com.bookstore.api.constants.AuthType;
import com.bookstore.api.manager.ConfigManagerOne;
import com.bookstore.api.manager.TokenManager;


import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@Listeners(ChainTestListener.class)
public class BaseTest {

	protected RestClient restClient;
	
	//===========************Book Store*****************==============//
	
	// ****** API BASE URLs **********/
	protected static final String BASE_URL_BOOKSTORE="http://127.0.0.1:8000";
	
	// ********API ENDPoints**************//
	protected static final String BOOKSTORE_BOOKS_ENDPOINT = "/books/";
	protected static final String BOOKSTORE_LOGIN_ENDPOINT="/login";	
	protected static final String BOOKSTORE_SIGNUP_ENDPOINT="/signup"; 
	protected static final String BOOKSTORE_HEALTH_ENDPOINT="/health"; 
		
	@BeforeSuite
	public void setUpAllureReport() {
		RestAssured.filters(new AllureRestAssured());
	}
	
	// Parallel Execution section: 
	
	protected String tokenId;
	private static boolean isParallelMode;
	
	static {
		isParallelMode = Boolean.parseBoolean(ConfigManagerOne.get("parallel.token.mode"));
	}
	
	@BeforeTest
	public void setUpTokenOnce() {
		if(!isParallelMode) {
			 ChainTestListener.log("====================BaseMethod:setUp()====================");
			 restClient = new RestClient();
			 doLogin();
		}
	}

	@BeforeMethod
	public void setUpTokenPerMethod() {

		 if(!isParallelMode) {
			 ChainTestListener.log("====================BaseMethod:setUp()====================");
			 restClient = new RestClient();
			doLogin();
		 }
	}
	
	protected void doLogin() {
		System.out.println("<-----Do Login First----->");
		Response response = restClient.post(BASE_URL_BOOKSTORE, BOOKSTORE_LOGIN_ENDPOINT, 
				TokenManager.getLoginCredentials(), null, null, AuthType.NONE, ContentType.JSON);
		ConfigManagerOne.set("bookstore_bearertoken", response.jsonPath().getString("access_token"));
	}

	@AfterMethod
	public void cleanUpAfterMethod() {
		
	}
	
	@AfterTest
	public void cleanUpAfterTest() {
		
	}
	
	
}
