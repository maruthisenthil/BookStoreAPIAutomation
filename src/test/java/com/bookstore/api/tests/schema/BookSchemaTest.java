package com.bookstore.api.tests.schema;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.bookstore.api.base.BaseTest;
import com.bookstore.api.constants.AuthType;
import com.bookstore.api.utils.SchemaValidator;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class BookSchemaTest extends BaseTest{
	
	@Test(priority =1, groups="positive")
	public void getBookSchemaTest() {
		
		Integer bookid=1102;
		Response responseGet = restClient.get(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT+bookid, null, null, 
				AuthType.BEARER_TOKEN, ContentType.ANY);
		Assert.assertTrue(SchemaValidator.validateSchema(responseGet, "schema/book_schema.json"));
		
	}
	
	@Test(priority =1, groups="positive")
	public void getAllBooksSchemaTest() {
		
		Response responseGet = restClient.get(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		
		Assert.assertTrue(SchemaValidator.validateSchema(responseGet, "schema/books_schema.json"));
		
	}
	
}
