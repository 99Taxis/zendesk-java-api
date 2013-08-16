package com.taxis99.zendesk.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ZendeskConfigFromProperties implements ZendeskConfig {

  private final String organization;
  private final String authUser;
  private final String authToken;

  ZendeskConfigFromProperties() {
    try (InputStream resource = ZendeskConfigFromProperties.class.getResourceAsStream("/zendesk.properties")) {
      final Properties properties = new Properties();
      properties.load(resource);
      this.organization = properties.getProperty("organization");
      this.authUser = properties.getProperty("authUser");
      this.authToken = properties.getProperty("authToken");
    } catch (IOException e) {
      throw new RuntimeException("Unable to read ZenDesk config properties file", e);
    }
  }

  @Override public String getAuth() {
    return authUser + "/token:" + authToken;
  }

  @Override public String getOrganizaton() {
    return organization;
  }
}