package org.example.oauth.twitter;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.scribe.model.Token;

public class SimpleAccessTokenDB {
	private static String filePath = "/tmp/org.example.oauth.twitter.db";
	private Properties accessDB = new Properties();
	
	SimpleAccessTokenDB() throws IOException {
		readAccessDB();
	}
	private void readAccessDB() throws IOException {
		File dbFile = new File(filePath);
		if (!dbFile.exists()) {
			System.out.println("Creating new accessdb...");
			if(!dbFile.createNewFile()) {
				throw new IOException("Could not create dbfile : " + filePath);
			}
		}
		
		if (dbFile.length() > 0) {
			accessDB.loadFromXML(new FileInputStream(dbFile));
		}
	}
	
	void writeAccessDB() throws IOException {
		File dbFile = new File(filePath);
		OutputStream out = new FileOutputStream(dbFile);
		
		//accessDB.store(out, "test");
		accessDB.storeToXML(out, "");
	}
	
	public Token getToken(String user) {
		String token = accessDB.getProperty(user);
		if (token == null)
			return null;
		String[] tokenParts = token.split(":");
		return new Token(tokenParts[0], tokenParts[1]);
	}
	
	public void addToken(String user, Token token) {
		accessDB.setProperty(user, token.getToken() + ":" + token.getSecret());
	}
}
