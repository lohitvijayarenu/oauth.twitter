package org.example.oauth.twitter;

import java.io.IOException;
import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class ExampleApp {
	SimpleAccessTokenDB accessTokenDB;
	void init() throws IOException {
		accessTokenDB = new SimpleAccessTokenDB();
	}
	
	int run(String apiKey, String apiSecret) {
		Scanner in = new Scanner(System.in);
		System.out.print("Please enter Twitter username >> ");
		String user = in.nextLine();
		
		// Lookup for stored access tokens
		Token userToken = accessTokenDB.getToken(user);
		if (userToken == null) {
			System.out.println("Unknown user. Aborting...");
			return 1;
		}
		
		 OAuthService service = new ServiceBuilder()
	        .provider(TwitterApi.class)
	        .apiKey(apiKey)
	        .apiSecret(apiSecret)
	        .build();
		 
		OAuthRequest request = new OAuthRequest(Verb.GET, ResourceURLs.SEARCH);
		request.addQuerystringParameter("q", "hadoop");
	    service.signRequest(userToken, request);
	    Response response = request.send();
	    System.out.println(response.getBody());
		return 0;
	}
	
	public static void main(String[] args) throws IOException {
		ExampleApp app = new ExampleApp();
		app.init();
		String apiKey, apiSecret;
		if (args.length == 2) {
			apiKey = args[0];
			apiSecret = args[1];
		} else {
			Scanner in = new Scanner(System.in);
			System.out.print("Enter API Key >> ");
			apiKey = in.nextLine();
			System.out.print("Enter API Secret >> ");
			apiSecret = in.nextLine();
		}
		System.exit(app.run(apiKey, apiSecret));
	}
}
