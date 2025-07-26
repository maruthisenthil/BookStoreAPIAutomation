package com.bookstore.api.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
	private Integer id;
	private String name;
	private String author;
	private int published_year;
	private String book_summary;

}
