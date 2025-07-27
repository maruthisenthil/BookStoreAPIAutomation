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

@Epic("EPIC 101: BookStore UpdateBookTest Feature ")
@Story("User Story: features - bookstore - updateBookAPI ")
public class UpdateBookTest extends BaseTest {

	@Test(priority =1, groups="positive")
	public void updateBookTest() {
	// 1.create book - POST
		Book book = BookTestDataFactory.createValidBook();

		Response responsePost = restClient.post(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT, 
				book, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(responsePost.getHeader("Content-Type"), "application/json");
		Assert.assertEquals(responsePost.jsonPath().getString("name"), book.getName());
		Assert.assertNotNull(responsePost.jsonPath().getString("id"));
		
		// fetch bookId:
		Integer bookId = responsePost.jsonPath().getInt("id");
		System.out.println("BookId : "+bookId);
		
	// 2. GET: Fetch the same book with
		
		Response responseGet = restClient.get(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT+bookId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(responseGet.getHeader("Content-Type"), "application/json");
		Assert.assertTrue(responseGet.statusLine().contains("OK"));
		Assert.assertEquals(responseGet.jsonPath().getInt("id"), bookId);
		
	// 3. Update the user using the same book ID
		book.setBook_summary("Updated Book 1102 Summary");
		book.setPublished_year(2001);
		book.setAuthor("Senthil");
		Response responsePut = restClient.put(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT+bookId, book, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(responsePut.getHeader("Content-Type"), "application/json");
		Assert.assertTrue(responsePut.statusLine().contains("OK"));
		Assert.assertEquals(responsePut.jsonPath().getInt("id"), bookId);
		Assert.assertEquals(responsePut.jsonPath().getString("name"), book.getName());
		Assert.assertEquals(responsePut.jsonPath().getString("book_summary"), book.getBook_summary());
			
	// 4. GET: Fetch the same book with Id
		responseGet = restClient.get(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT+bookId, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		Assert.assertEquals(responseGet.getHeader("Content-Type"), "application/json");
		Assert.assertTrue(responseGet.statusLine().contains("OK"));
		Assert.assertEquals(responseGet.jsonPath().getInt("id"), bookId);
		Assert.assertEquals(responseGet.jsonPath().getString("name"), book.getName());
		Assert.assertEquals(responseGet.jsonPath().getString("book_summary"), book.getBook_summary());
	}
	
	@Test(priority =2, groups="negative")
	public void updateBookInvalidBookIDTest() {
		Integer InvalidBookId = -100;
		
		Book book = BookTestDataFactory.createValidBook();
		book.setId(InvalidBookId);
		Response responsePut = restClient.put(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT+InvalidBookId, book, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		AssertionUtils.assertBookNotFoundResponse(responsePut);
	}
	
	@Test(priority =2, groups="negative")
	public void updateBookExpiredTokenTest() {
		
		Book book = BookTestDataFactory.createValidBook();
		Integer validBookId = book.getId();

		Response responsePut = restClient.put(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT+validBookId, 
									book, null, null, AuthType.EXPIRED_TOKEN, ContentType.JSON);
		AssertionUtils.assertForbiddenTokenResponse(responsePut);
		
	}
	
	@Test(priority =2, groups="negative")
	public void updateBookNoAuthTokenTest() {
		
		Book book = BookTestDataFactory.createValidBook();
		Integer validBookId = book.getId();

		Response responsePut = restClient.put(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT+validBookId, 
									book, null, null, AuthType.NO_AUTH, ContentType.JSON);
		AssertionUtils.assertNotAuthenticatedResponse(responsePut);
		
	}
	
	
	
}
