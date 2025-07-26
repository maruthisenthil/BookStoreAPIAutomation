package com.bookstore.api.model;


import java.util.concurrent.ThreadLocalRandom;

import com.bookstore.api.pojo.Book;

public class BookTestDataFactory {

	public static void main(String[] as ) {
		System.out.println(createValidBook());
	}
	
	public static Book createValidBook() {
		return Book.builder()
                .id(ThreadLocalRandom.current().nextInt(300, 1000)) // integer only
                .name("Book " + ThreadLocalRandom.current().nextInt(1000, 9999))
                .author("Author" + ThreadLocalRandom.current().nextInt(1000, 9999))
                .published_year(ThreadLocalRandom.current().nextInt(1990, 2025))
                .book_summary("This is a summary of the book for test purposes.")
                .build();
		
//        return Book.builder()
//                .id(ThreadLocalRandom.current().nextInt(300, 1000)) // integer only
//                .name("Book " + ThreadLocalRandom.current().nextInt(1000, 9999))
//                .author("Author " + ThreadLocalRandom.current().nextInt(1000, 9999))
//                .published_year(ThreadLocalRandom.current().nextInt(1990, 2025))
//                .book_summary("This is a summary of the book for test purposes.")
//                .build();
    }

    public static Book createBookWithoutId() {
        return Book.builder()
                .name("Book " + ThreadLocalRandom.current().nextInt(1000, 9999))
                .author("Author " + ThreadLocalRandom.current().nextInt(1000, 9999))
                .published_year(ThreadLocalRandom.current().nextInt(1990, 2025))
                .book_summary("Summary without ID")
                .build();
    }

    public static Book createInvalidBook() {
        return Book.builder()
                .id(null)
                .name("") // Invalid: required field
                .author("") // Invalid: required field
                .published_year(-1) // Invalid year
                .book_summary("") // Invalid
                .build();
    }
    
    public static Book createInvalidBookPayload() {
        return Book.builder()
                .id(null)
                .name("") // Invalid: required field
                .book_summary("") // Invalid
                .build();
    }
    
}
	
