package com.apiomat.signup.client;

import com.apiomat.signup.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SignUp implements EntryPoint {
  /**
   * The message displayed to the user when the server cannot be reached or
   * returns an error.
   */
  private static final String SERVER_ERROR = "An error occurred while "
      + "attempting to contact the server. Please check your network "
      + "connection and try again.";

  
  /**
   * Create a remote service proxy to talk to the server-side SignUpData service.
   * 
   */
  private SignUpDataServiceAsync signUpDataService = GWT.create(SignUpDataService.class);
 

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
   
    final Button signupButton = new Button("Sign up!");
    final TextBox nameField = new TextBox();
    final TextBox emailField = new TextBox();
    final PasswordTextBox passwordField = new PasswordTextBox();
    final PasswordTextBox confirmpasswordField = new PasswordTextBox();
    final Label errorLabel = new Label();
    //initialize array to store data
   

    emailField.setText("e-mail");
    nameField.setText("username");
    passwordField.setText("password");
    confirmpasswordField.setText("password");
    

    // We can add style names to widgets
    signupButton.addStyleName("signupButton");

    // Add the nameField and sendButton to the RootPanel
    // Use RootPanel.get() to get the entire body element
    RootPanel.get("signupButtonContainer").add(signupButton);
    //RootPanel.get("errorLabelContainer").add(errorLabel);

    // Create the popup dialog box
    final DialogBox dialogBox = new DialogBox();
    final Button sendButton = new Button("Send");
    final Button closeButton = new Button("Close");

    dialogBox.setText("Sign up for apiOmat!");
    dialogBox.setAnimationEnabled(true);

    // We can set the id of a widget by accessing its Element
    sendButton.getElement().setId("sendButton");
    closeButton.getElement().setId("closeButton");

    final Label emailToServerLabel = new Label();
    final Label nameToServerLabel = new Label();
    final Label passwordToServerLabel = new Label();
    final HTML serverResponseLabel = new HTML();

    VerticalPanel dialogVPanel = new VerticalPanel();

    dialogVPanel.addStyleName("dialogVPanel");

    dialogVPanel.add(new HTML("<p><b>email:</b></p>"));
    dialogVPanel.add(emailField);
    dialogVPanel.add(new HTML("<br><p><b>username:</b></p>"));
    dialogVPanel.add(nameField);
    dialogVPanel.add(new HTML("<br><p><b>password:</b></p>"));
    dialogVPanel.add(passwordField);
    dialogVPanel.add(new HTML("<br><p><b>confirm password:</b></p>"));
    dialogVPanel.add(confirmpasswordField);
    //dialogVPanel.add(serverResponseLabel);
    dialogVPanel.add(errorLabel);
    dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
    dialogVPanel.add(sendButton);
    dialogVPanel.add(closeButton);
    dialogBox.setWidget(dialogVPanel);

    // Add a handler to show the DialogBox
    signupButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        dialogBox.center();
        emailField.setFocus(true);
        emailField.selectAll();
        signupButton.setEnabled(false);
        signupButton.setFocus(false);
      }
    });

    // Add a handler to close the DialogBox
    closeButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        dialogBox.hide();
        signupButton.setEnabled(true);
        signupButton.setFocus(true);
      }
    });

    // Create a handler for the sendButton and data fields
    class MyHandler implements ClickHandler, KeyUpHandler {
      /**
       * Fired when the user clicks on the sendButton.
       */
      public void onClick(ClickEvent event) {
        sendDataToServer();
      }

      /**
       * Fired when the user types in the data fields.
       */
      public void onKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
          sendDataToServer();
        }
      }

      /**
       * Send the data from the fields to the server and wait for a response.
       */
      private void sendDataToServer() {
        // First, we validate the input.
        errorLabel.setText("");
        String emailToServer = emailField.getText();
        String nameToServer = nameField.getText();
        String passwordToServer = passwordField.getText();
        String confirmpasswordToServer = confirmpasswordField.getText();
        
        if (!FieldVerifier.isValidName(nameToServer)) {
          errorLabel.setText("Please enter at least a username with four characters!");
          return;
        }

        if (!FieldVerifier.isValidEmail(emailToServer)) {
          System.out.println(emailToServer);
          errorLabel.setText(emailToServer + "is no email address. Please enter a valid email address!");
          return;
        }
        
        if (!FieldVerifier.isValidPassword(passwordToServer)) {
          errorLabel.setText("The password must be a minimum of 8 characters \n and must have at least one number, uppercase and lowercase letters \n and one special character!");
          return;
        }

        if (!FieldVerifier.isValidConfirmedPassword(confirmpasswordToServer, passwordToServer)) {
          errorLabel.setText("Password not confirmed! Please type in the same password twice.");
          return;
        }

              
        // Then, we send the input to the server.
        sendButton.setEnabled(false);
        emailToServerLabel.setText(emailToServer);
        nameToServerLabel.setText(nameToServer);
        passwordToServerLabel.setText(passwordToServer);
        serverResponseLabel.setText("");
        
        
        signUpDataService.userData(emailToServer, nameToServer, new AsyncCallback<String>() {
            public void onFailure(Throwable caught) {
              // Show the RPC error message to the user
            	dialogBox.setText("Remote Procedure Call - Failure");
                serverResponseLabel.addStyleName("serverResponseLabelError");
                serverResponseLabel.setHTML(SERVER_ERROR);
                errorLabel.setText(caught.getMessage());
                sendButton.setEnabled(true);
                System.out.println(caught);
            }

            public void onSuccess(String result) {
              System.out.println(result);
              serverResponseLabel.removeStyleName("serverResponseLabelError");
              serverResponseLabel.setHTML(result);
              dialogBox.hide();
              //System.out.println(result);
              RootPanel.get("signupButtonContainer").remove(signupButton);
              RootPanel.get("signupButtonContainer").add(new HTML("<p> Remote Procedure Call</p>"));
              RootPanel.get("signupButtonContainer").add(serverResponseLabel);
              //RootPanel.get("signupButtonContainer").add(new HTML("<p> Welcome to apiOmat! Please go to <b>" + emailToServerLabel.getText() + "</b> and verify your account!</p>"));
              //dialogBox.center();
            }
          });
        

        
      }
    }

    // Add a handler to send the name to the server
    MyHandler handler = new MyHandler();
    sendButton.addClickHandler(handler);
    emailField.addKeyUpHandler(handler);
    nameField.addKeyUpHandler(handler);
    passwordField.addKeyUpHandler(handler);
    confirmpasswordField.addKeyUpHandler(handler);
  }
}