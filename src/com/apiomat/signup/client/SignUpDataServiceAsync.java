package com.apiomat.signup.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SignUpDataServiceAsync {

	void userData(String email_input, String name_input, AsyncCallback<String> callback)
			throws IllegalArgumentException;

}
