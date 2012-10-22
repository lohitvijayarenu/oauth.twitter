package org.example.oauth.twitter;

import java.io.IOException;
import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class ExampleRegister {
	String tUserName;
	SimpleAccessTokenDB tokenDB;
	
	private int registerUser(String apiKey, String apiSecret) 
			throws IOException {
		Scanner in = new Scanner(System.in);
		System.out.print("Please enter your Twitter username >> ");
		tUserName = in.nextLine();
		
		// Authorize App for user
		OAuthService service = new ServiceBuilder()
	        .provider(TwitterApi.class)
	        .apiKey(apiKey)
	        .apiSecret(apiSecret)
	        .build();
	    Token requestToken = service.getRequestToken();
	    String authUrl = service.getAuthorizationUrl(requestToken);
	    
	    System.out.println("Authorize this App for you Twitter account by opening link in browser");
	    System.out.println(authUrl);
	    System.out.print("Enter verification code >> ");
	    String verification = in.nextLine();
	    Verifier verifier = new Verifier(verification);
	    Token accessToken = service.getAccessToken(requestToken, verifier);
	    
	    // Test to see if we can access user timeline
	    OAuthRequest request = new OAuthRequest(Verb.GET, ResourceURLs.USER_TIMELINE);
	    service.signRequest(accessToken, request);
	    Response response = request.send();
	    if (response.isSuccessful()) {
	    	// Persist accessToken for future use
	    	// Just dump to file for now
	    	tokenDB = new SimpleAccessTokenDB();
	    	tokenDB.addToken(tUserName, accessToken);
	    	tokenDB.writeAccessDB();
	    	System.out.println("Successfully registered " + tUserName);
	    	return 0;
	    } else {
	    	System.err.println("Unable to register " + tUserName);
	    	return 1;
	    }
	}
	public static void main(String[] args) throws IOException {
		ExampleRegister registerApp = new ExampleRegister();
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
		System.exit(registerApp.registerUser(apiKey, apiSecret));
	}
}
