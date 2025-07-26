package com.bookstore.api.tests.book;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.bookstore.api.base.BaseTest;
import com.bookstore.api.constants.AuthType;
import com.bookstore.api.model.BookTestDataFactory;
import com.bookstore.api.pojo.Book;
import com.bookstore.api.utils.AssertionUtils;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CreateBookTest extends BaseTest {

	@Description("CreateBook the Book... ")
	@Owner("Senthil AutoTest")
	@Severity(SeverityLevel.CRITICAL)
	@Test(priority =1, groups="positive")
	public void createABookTest() {
		String bookId;
		// 1. POST: CreateBook 
		Book book = BookTestDataFactory.createValidBook();

		Response responsePost = restClient.post(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT, book, null, null,
				AuthType.BEARER_TOKEN, ContentType.JSON);
		Book bookResponsePost = responsePost.as(Book.class);
		
		
		System.out.println(" input_book_Name: " + book.getName());
		Assert.assertEquals(responsePost.getHeader("Content-Type"), "application/json");
		Assert.assertEquals(bookResponsePost.getName(), book.getName());
		Assert.assertNotNull(bookResponsePost.getId());
		bookId = responsePost.jsonPath().getString("id");
		// 2. GET: Fetch the same book with book id

		Response responseGet = restClient.get(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT + bookId, null, null,
				AuthType.BEARER_TOKEN, ContentType.JSON);
		Book bookResponseGet = responseGet.as(Book.class);
		
		Assert.assertEquals(responseGet.getHeader("Content-Type"), "application/json");
		Assert.assertTrue(responseGet.statusLine().contains("OK"));
		Assert.assertEquals(bookResponseGet.getId(), book.getId());
	}
		
	// Due to time constraints only few negative cases scripted - Please find the EOF for remaining negative scripts planned
	
	
	
	/// As of now API accepting Empty Data- Actually it is a BUG ========= :) not sure it may be the requirement. Thought to cover here.
	@Test(priority =2, groups="negative")
	public void createABookTestInvalidPayloadTest() {

		// 1. POST: CreateBook 
		Book book = BookTestDataFactory.createInvalidBook();
		

		Response responsePost = restClient.post(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT, book, null, null,
				AuthType.BEARER_TOKEN, ContentType.JSON);
		
		System.out.println(" input_book_Name: " + book.getName());
		Assert.assertEquals(responsePost.getHeader("Content-Type"), "application/json");
		Assert.assertEquals(responsePost.jsonPath().getString("name"), book.getName());
		Assert.assertNotNull(responsePost.jsonPath().getString("id"));
		
		
	}

	@Test(priority =2, groups="negative")
	public void createInvalidBookTest() {
		// 1. POST: CreateBook 
		Book book = BookTestDataFactory.createInvalidBookPayload();

		Response responsePost = restClient.post(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT, book, null, null,
				AuthType.BEARER_TOKEN, ContentType.JSON);
		
		Assert.assertEquals(responsePost.getHeader("Content-Type"), "text/plain; charset=utf-8");
		Assert.assertTrue(responsePost.statusLine().contains("Internal Server Error"));
		
	}
	
	@Test(priority =2, groups="negative")
	public void createValidBookExpiredTokenTest() {
		// 1. POST: CreateBook 
		Book book = BookTestDataFactory.createValidBook();

		Response responsePost = restClient.post(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT, book, null, null,
				AuthType.EXPIRED_TOKEN, ContentType.JSON);
		AssertionUtils.assertForbiddenTokenResponse(responsePost);
		
		
	}
	
	@Test(priority =2, groups="negative")
	public void createValidBookNoAuthTokenTest() {
		// 1. POST: CreateBook 
		Book book = BookTestDataFactory.createValidBook();

		Response responsePost = restClient.post(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT, book, null, null,
				AuthType.NO_AUTH, ContentType.JSON);
		
		AssertionUtils.assertNotAuthenticatedResponse(responsePost);
		
		
	}
	
	// Planned Negative test cases
	// 1. Create book with missing required field (e.g., name)	400 Bad Request with validation message
	// 2. Create book with invalid data (e.g., negative price, non-integer pages)	422 Unprocessable Entity
	// 3. Create book with duplicate ID (if manual ID allowed)	409 Conflict or 400 Bad Request
	// 4. Create book without Auth token	401 Unauthorized
	// 5. Create book with invalid/expired token	403 Forbidden
	// 6. Send invalid JSON (broken format)	400 Bad Request
	// 7. Create book with extremely large input (name/description > limit)	413 Payload Too Large or 422	
	
	
}
