package com.apiomat.signup.server;

import java.util.ArrayList;

import com.apiomat.signup.client.SignUpDataService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class SignUpDataServiceImpl extends RemoteServiceServlet implements
		SignUpDataService {

	private ArrayList<String> emailaddress = new ArrayList<String>();
	private ArrayList<String> name = new ArrayList<String>();
		
	public String userData(String email_input, String name_input) throws IllegalArgumentException {
		
				
		email_input = escapeHtml(email_input);
		name_input = escapeHtml(name_input);
		
		if (emailaddress.contains(email_input))
			// If the input already exists, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException("Email already exists!");
		
		if (name.contains(name_input))
			// If the input already exists, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException("Username already exists!");
		
		emailaddress.add(email_input);
		name.add(name_input);
		
		return "Hello, " + name_input + "! You've been successfully registered!<br><br>Please verify you email address at "+ email_input + "<br>";
	}
	
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
}
