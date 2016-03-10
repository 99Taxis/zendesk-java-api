package com.taxis99.zendesk.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ZendeskConfigFromProperties implements ZendeskConfig {

  private final String subdomain;
  private final String authUser;
  private final String authToken;
  private final int connTimeout;

  public ZendeskConfigFromProperties() {
    try (InputStream resource = ZendeskConfigFromProperties.class.getResourceAsStream("/zendesk.properties")) {
      final Properties properties = new Properties();
      properties.load(resource);
      this.subdomain = properties.getProperty("subdomain");
      this.authUser = properties.getProperty("authUser");
      this.authToken = properties.getProperty("authToken");
      this.connTimeout = Integer.parseInt(properties.getProperty("connTimeout"), 10);
    } catch (IOException e) {
      throw new RuntimeException("Unable to read Zendesk config properties file", e);
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