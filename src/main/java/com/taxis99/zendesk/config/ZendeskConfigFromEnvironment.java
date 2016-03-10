package com.taxis99.zendesk.config;

public class ZendeskConfigFromEnvironment implements ZendeskConfig {

  private final String subdomain;
  private final String authUser;
  private final String authToken;
  private final int connTimeout;

  public ZendeskConfigFromEnvironment() {
    try {
      this.subdomain = System.getenv("SUBDOMAIN");
      this.authUser = System.getenv("AUTH_USER");
      this.authToken = System.getenv("AUTH_TOKEN");
      this.connTimeout = Integer.parseInt(System.getenv("CONN_TIMEOUT"), 10);
    } catch (SecurityException | NumberFormatException e) {
      throw new RuntimeException("Unable to read Zendesk parameters from environment vars", e);
    }
  }

  @Override public String getAuth() {
    return authUser + "/token:" + authToken;
  }

  @Override public String getSubdomain() {
    return subdomain;
  }

  @Override public int getConnTimeout() {
    return connTimeout;
  }
}