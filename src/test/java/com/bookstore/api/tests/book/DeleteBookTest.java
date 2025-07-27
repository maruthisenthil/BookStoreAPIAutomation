package com.bookstore.api.tests.book;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.bookstore.api.base.BaseTest;
import com.bookstore.api.constants.AuthType;
import com.bookstore.api.model.BookTestDataFactory;
import com.bookstore.api.pojo.Book;
import com.bookstore.api.utils.AssertionUtils;

import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@Epic("EPIC 103: BookStore GetBookTest Feature ")
@Story("User Story: features - bookstore - deleteBookAPI ")
public class DeleteBookTest extends BaseTest{
	
	@Test(priority =1, groups="positive")
	public void deleteBookTest() {
		// 1.create book - POST
			Book book = BookTestDataFactory.createValidBook();
			
			Response responsePost = restClient.post(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT, 
					book, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
			Book bookResponsePost = responsePost.as(Book.class);
			
			Assert.assertEquals(responsePost.getHeader("Content-Type"), "application/json");
			Assert.assertEquals(bookResponsePost.getName(), book.getName());
			Assert.assertNotNull(bookResponsePost.getId());
			
			// fetch bookId:
			Integer bookId = bookResponsePost.getId();
			System.out.println("BookId : "+bookId);
			
		// 2. GET: Fetch the same book with book id
			
			Response responseGet = restClient.get(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT+bookId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
			Book bookResponseGet = responsePost.as(Book.class);
			
			Assert.assertEquals(responseGet.getHeader("Content-Type"), "application/json");
			Assert.assertTrue(responseGet.statusLine().contains("OK"));
			Assert.assertEquals(bookResponseGet.getId(), bookId);
			Assert.assertNotNull(bookResponseGet.getId());
			
		// 3. delete the book using the same book ID
			
			Response responseDelete = restClient.delete(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT+bookId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
			Assert.assertEquals(responseDelete.getHeader("Content-Type"), "application/json");
			Assert.assertTrue(responseDelete.statusLine().contains("OK"));
			Assert.assertEquals(responseDelete.jsonPath().getString("message"), "Book deleted successfully");
			
				
		// 4. GET: Fetch the deleted book using the same book id
			
			responseGet = restClient.get(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT+bookId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
			Assert.assertEquals(responseGet.getHeader("Content-Type"), "application/json");
			Assert.assertTrue(responseGet.statusLine().contains("Not Found"));
			Assert.assertEquals(responseGet.statusCode(), 404);
			Assert.assertEquals(responseGet.jsonPath().getString("detail"), "Book not found");
			
		}
	
		
	@Test(priority =2, groups="negative")
	public void deleteBookInvalidTest() {
			Integer bookId = 999999999;
			Response responseDelete = restClient.delete(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT+bookId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
			AssertionUtils.assertBookNotFoundResponse(responseDelete);
	}
	
	@Test(priority =2, groups="negative")
	public void deleteBookInvalidAlphaNumericTest() {
			String bookId = "asd123";

			Response responseDelete = restClient.delete(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT+bookId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
			Assert.assertEquals(responseDelete.getHeader("Content-Type"), "application/json");
			Assert.assertTrue(responseDelete.statusLine().contains("Unprocessable Content"));
				
			Assert.assertTrue(responseDelete.jsonPath().getString("detail[0].msg")
				    .contains("Input should be a valid integer"));
	}
	
	@Test(priority =2, groups="negative")
	public void deleteBookExpiredTokenTest() {
			Integer bookId = 1;

			Response responseDelete = restClient.delete(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT+bookId, null, null, AuthType.EXPIRED_TOKEN, ContentType.JSON);
			AssertionUtils.assertForbiddenTokenResponse(responseDelete);
				    
	}
	
	@Test(priority =2, groups="negative")
	public void deleteBookNoAuthTest() {
			Integer bookId = 1;

			Response responseDelete = restClient.delete(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT+bookId, null, null, AuthType.NO_AUTH, ContentType.JSON);
			AssertionUtils.assertNotAuthenticatedResponse(responseDelete);
			
				    
	}

}
