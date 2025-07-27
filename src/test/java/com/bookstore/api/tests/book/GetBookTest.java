package com.bookstore.api.tests.book;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.bookstore.api.base.BaseTest;
import com.bookstore.api.constants.AuthType;
import com.bookstore.api.model.BookTestDataFactory;
import com.bookstore.api.pojo.Book;
import com.bookstore.api.utils.AssertionUtils;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@Epic("EPIC 100: BookStore GetBookTest Feature ")
@Story("User Story: features - bookstore - getBookAPI ")
public class GetBookTest extends BaseTest{
	
	@Description("Getting all the Book... ")
	@Owner("Senthil AutoTest")
	@Severity(SeverityLevel.CRITICAL)
	@Test(priority =1, groups="positive")
	public void getAllBooksTest() {
		Response responseGet = restClient.get(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		
		Book[] books = responseGet.as(Book[].class);
		
		// Validated first object from the list of books & Response Payload
		Book firstBook = books[0];
		Assert.assertTrue(books.length > 0, "Books array should not be empty");
		Assert.assertNotNull(firstBook.getId());
		Assert.assertNotNull(firstBook.getName());
		Assert.assertNotNull(firstBook.getAuthor());
		Assert.assertTrue(firstBook.getPublished_year() > 1900);
		
		Assert.assertEquals(responseGet.getHeader("Content-Type"), "application/json");
		Assert.assertTrue(responseGet.statusLine().contains("OK"));
		Assert.assertEquals(responseGet.statusCode(), 200);
		
	}
	
	@DataProvider
	public Object[][] getUserData() {
		return new Object[][] {
			{1001, "George Orwell", "1984", 1949, "A dystopian novel depicting a society under total surveillance by a totalitarian regime."},
			{1002, "Harper Lee", "To Kill a Mockingbird", 1960, "A profound story exploring racial injustice and childhood innocence in the American South."},
			{1003, "J.K. Rowling", "Harry Potter and the Sorcerer's Stone", 1997, "A young wizard discovers his magical heritage and begins his journey at Hogwarts School."},
			{1004, "F. Scott Fitzgerald", "The Great Gatsby", 1925, "A tragic romance and critique of the American Dream set in the Roaring Twenties."},
			{1005, "J.R.R. Tolkien", "The Fellowship of the Ring", 1954, "An epic fantasy journey begins to destroy a powerful ring that threatens the world."}
		};
	}
	
	@Description("Getting the Book from Test Data... ")
	@Owner("Senthil AutoTest")
	@Severity(SeverityLevel.CRITICAL)
	@Test(priority =1, groups="positive", dataProvider ="getUserData")
	public void getABookTest(Integer bookId, String author, String bookName, int year, String bookSummary) {
		Book book = new Book(bookId, author, bookName,year, bookSummary);
		
		Integer bookid=book.getId();
		Response responseGet = restClient.get(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT+bookid, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		
		Book bookResponse = responseGet.as(Book.class);
 		Assert.assertEquals(responseGet.getHeader("Content-Type"), "application/json");
		Assert.assertTrue(responseGet.statusLine().contains("OK"));
		Assert.assertEquals(responseGet.statusCode(), 200);
		
		Assert.assertEquals(bookResponse.getId(), book.getId());
	}
		
	@Description("Getting the Book... ")
	@Owner("Senthil AutoTest")
	@Severity(SeverityLevel.CRITICAL)
	@Test(priority =1, groups="positive")
	public void getBookWithPathParamTest() {
		Integer bookid=1102;
		Response responseGet = restClient.get(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT+bookid, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		
		Book bookResponse = responseGet.as(Book.class);
 		Assert.assertEquals(responseGet.getHeader("Content-Type"), "application/json");
		Assert.assertTrue(responseGet.statusLine().contains("OK"));
		Assert.assertEquals(responseGet.statusCode(), 200);
		
		Assert.assertEquals(bookResponse.getId(), bookid);
	}
		
	// Due to time constraints only few negative cases scripted - Please find the EOF for remaining negative scripts planned
		
	@Test(priority=2, groups="negative")
	public void getBookWithInvalidBookIdTest() {
		
		String bookid="12";
		Response responseGet = restClient.get(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT+bookid, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		AssertionUtils.assertBookNotFoundResponse(responseGet);	
	}
	
	@Test(priority=2, groups="negative")
	public void getBookAlphaNumericTest() {

		String bookid="123abc";
		Response responseGet = restClient.get(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT+bookid, null, null, AuthType.BEARER_TOKEN, ContentType.JSON);
		
 		Assert.assertEquals(responseGet.getHeader("Content-Type"), "application/json");
		Assert.assertTrue(responseGet.statusLine().contains("Unprocessable Content"));
		Assert.assertEquals(responseGet.getStatusCode(), 422);
		
		Assert.assertTrue(responseGet.jsonPath().getString("detail[0].msg")
		    .contains("Input should be a valid integer"));
	}
	
	@Test(priority=2, groups="negative")
	public void getBookWithExpiredTokenTest() {
		
		Book book = BookTestDataFactory.createValidBook();
		Integer bookid=book.getId();
		Response responseGet = restClient.get(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT+bookid, null, null, 
												AuthType.EXPIRED_TOKEN, ContentType.JSON);	
		AssertionUtils.assertForbiddenTokenResponse(responseGet);
	}
	
	@Test(priority=2, groups="negative")
	public void getBookNoAuthTest() {
		
		String bookid="12";
		Response responseGet = restClient.get(BASE_URL_BOOKSTORE, BOOKSTORE_BOOKS_ENDPOINT+bookid, null, null, AuthType.NO_AUTH, ContentType.JSON);
		AssertionUtils.assertNotAuthenticatedResponse(responseGet);
		
	}
	
	
	// We can cover most of the negative cases like below due to time constraining focusing on the other parts of validation
	
	// GET /books/99999	Book ID that doesn’t exist
	// GET /books/-1	Negative book ID
	// GET /books/0	Edge-case ID (if 0 is invalid)
	// POST /books/{id}	Should return 405 Method Not Allowed
	// PUT /books/{id}	Should return 405 (if not supported)

	
}
