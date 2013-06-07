package com.apiomat.signup.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("signUpData")
public interface SignUpDataService extends RemoteService {
	String userData(String email_input, String name_input) throws IllegalArgumentException;
}
