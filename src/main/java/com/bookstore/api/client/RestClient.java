package com.bookstore.api.client;

import java.util.Map;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

import com.aventstack.chaintest.plugins.ChainTestListener;
import com.bookstore.api.constants.AuthType;
import com.bookstore.api.exceptions.APIException;
import com.bookstore.api.manager.ConfigManagerOne;
import static io.restassured.RestAssured.expect;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class RestClient {

	// define Response specs : to validate response code of an API
	ResponseSpecification responseSpec200 = expect().statusCode(200);
	ResponseSpecification responseSpec201 = expect().statusCode(201);
	ResponseSpecification responseSpec204 = expect().statusCode(204);
	ResponseSpecification responseSpec400 = expect().statusCode(400);
	ResponseSpecification responseSpec200or201 = expect().statusCode(anyOf(equalTo(200), equalTo(201)));
	ResponseSpecification responseSpec200or500 = expect().statusCode(anyOf(equalTo(200), equalTo(500)));
	ResponseSpecification responseSpec200or404 = expect().statusCode(anyOf(equalTo(200), equalTo(404)));
	ResponseSpecification responseSpec200or404or403 = expect().statusCode(anyOf(equalTo(200), equalTo(404),equalTo(403)));
	ResponseSpecification responseSpec200or404or422 = expect().statusCode(anyOf(equalTo(200), equalTo(404),equalTo(422)));
	ResponseSpecification responseSpec200or400or500 = expect().statusCode(anyOf(equalTo(200), equalTo(400), equalTo(500)));
	ResponseSpecification responseSpec200or400or403or500 = expect().statusCode(anyOf(equalTo(200), equalTo(400), equalTo(403), equalTo(500)));
	ResponseSpecification responseSpec200or404or403or422 = expect().statusCode(anyOf(equalTo(200), equalTo(404),equalTo(403),equalTo(422)));

	// setup request object with required specification like BaseURL,AuthType and ContentType

	// Authorization:  
	private RequestSpecification setUpRequest(String baseURL, AuthType authType, ContentType contentType) {
		ChainTestListener.log("BookStore API_BaseURL: "+baseURL);
		ChainTestListener.log("AuthType: "+authType.toString());
		ChainTestListener.log("ContentType: "+contentType.toString());
		RequestSpecification request = RestAssured.given().log().all().baseUri(baseURL).contentType(contentType)
				.accept(contentType);

		switch (authType) {
		case BEARER_TOKEN:
			request.header("Authorization", "Bearer " + ConfigManagerOne.get("bookstore_bearertoken")); 
			break;
		case OAUTH2:
			request.header("Authorization", "Bearer " + "sometoken");
			break;
		case API_KEY:
			request.header("x-api-key", "api key");
			break;
		case BASIC_AUTH:
			request.header("Authorization", "Basic " + "BasicAuthToken");
			break;
		case NO_AUTH:
			System.out.println("Authorization is NOT supported/ NOT Required ");
			break;
		case EXPIRED_TOKEN:
			System.out.println("Setting the Expired Token From the Proerpties file - Param-");
			request.header("Authorization", "Bearer " + ConfigManagerOne.get("bookstore_expiredtoken"));
			break;
		case NONE:
			System.out.println("Auth is not Required for UserLogin - valid credentials: ");
			break;
		default:
			System.out.println("this auth is not supported.. please pass the right AuthType...");
			throw new APIException("======Invalid Auth=====");
		}
		return request;

	}

	private void applyParams(RequestSpecification request, Map<String, String> queryParams,
			Map<String, String> pathParams) {
		ChainTestListener.log("QueryParam: "+queryParams);
		ChainTestListener.log("PathParam: "+queryParams);

		if (queryParams != null) {
			request.queryParams(queryParams);
		}

		if (pathParams != null) {
			request.pathParams(pathParams);
		}
	}

	// BookStore CRUD
	// get:
	/**
	 * This method used to call Get APIs
	 * 
	 * @param baseUrl
	 * @param endPoint
	 * @param body
	 * @param queryParams
	 * @param pathParams
	 * @param authType
	 * @param contentType
	 * @return it returns GET API call response
	 */
	public Response get(String baseUrl, String endPoint, Map<String, String> queryParams,
			Map<String, String> pathParams, AuthType authType, ContentType contentType) {
		
		RequestSpecification request = setUpRequest(baseUrl, authType, contentType);

		applyParams(request, queryParams, pathParams);

		Response response = request.get(endPoint).then().log().all().spec(responseSpec200or404or403or422).extract().response();
		return response;

	}
	

	// Add Book to BookStore : POST 
	// T is reserved keyword in Java - Userd fo Generic Type Parameter – allows the
	// method to accept any type of inputPayload (like Book, User, etc..,)
	public <T> Response post(String baseUrl, String endPoint, T inputPayload, Map<String, String> queryParams,
			Map<String, String> pathParams, AuthType authType, ContentType contentType) {
		RequestSpecification request = setUpRequest(baseUrl, authType, contentType);

		applyParams(request, queryParams, pathParams);

		Response response = request.body(inputPayload).post(endPoint).then().log().all().spec(responseSpec200or400or403or500).extract()
				.response();
		return response;
	}

	// Login
	// POST
	public Response post(String baseUrl, String endPoint, Map<String, String> body, Map<String, String> queryParams,
			Map<String, String> pathParams, AuthType authType, ContentType contentType) {
		RequestSpecification request = setUpRequest(baseUrl, authType, contentType);
		applyParams(request, queryParams, pathParams);

		Response response = request.body(body).post(endPoint).then().spec(responseSpec200).extract().response();
		response.prettyPrint();
		return response;

	}

	public <T> Response put(String baseUrl, String endPoint, T inputPayload, Map<String, String> queryParams,
			Map<String, String> pathParams, AuthType authType, ContentType contentType) {
		RequestSpecification request = setUpRequest(baseUrl, authType, contentType);

		applyParams(request, queryParams, pathParams);

		Response response = request.body(inputPayload).put(endPoint).then().log().all().spec(responseSpec200or404or403).extract()
				.response();
		return response;
	}
	
	public Response delete(String baseUrl, String endPoint, Map<String, String> queryParams,
			Map<String, String> pathParams, AuthType authType, ContentType contentType) {
		RequestSpecification request = setUpRequest(baseUrl, authType, contentType);

		applyParams(request, queryParams, pathParams);

		Response response = request.delete(endPoint).then().log().all().spec(responseSpec200or404or403or422).extract()
				.response();
		return response;
	}

}
