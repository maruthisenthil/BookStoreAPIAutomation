package com.bookstore.api.manager;
import com.bookstore.api.client.RestClient;
import com.bookstore.api.constants.AuthType;
import com.bookstore.api.pojo.LoginCredentials;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class TokenManager {

    public static String refreshToken() {
        LoginCredentials creds = LoginCredentials.builder()
                .email(ConfigManagerOne.get("bookstore_email"))
                .password(ConfigManagerOne.get("bookstore_password"))
                .build();

        RestClient client = new RestClient();

        Response response = client.post(
                ConfigManagerOne.get("base_url"),
                "/login",
                creds,
                null,
                null,
                AuthType.NO_AUTH,
                ContentType.JSON
        );

        if (response.statusCode() != 200) {
            throw new RuntimeException(" Failed to refresh token! Login StatusStatus: " + response.statusCode());
        }

        String token = response.jsonPath().getString("access_token");
//        System.out.println("Refreshed Token Value: " + token);
        return token;
    }
    
	
	public static LoginCredentials getLoginCredentials() {
		LoginCredentials creds = LoginCredentials.builder()
				.email(ConfigManagerOne.get("bookstore_email"))
				.password(ConfigManagerOne.get("bookstore_password"))
				.build();
		return creds;
	}
}
