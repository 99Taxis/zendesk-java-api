package com.taxis99.zendesk.exceptions;

public class ZendeskException extends Exception {
  
  public ZendeskException() {
    super();
  }

  public ZendeskException(Throwable cause) {
    super(cause);
  }

  public ZendeskException(String message, Throwable cause) {
    super(message, cause);
  }

}
