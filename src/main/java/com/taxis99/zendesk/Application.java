package com.taxis99.zendesk;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.taxis99.zendesk.config.GsonInstanceHolder;
import com.taxis99.zendesk.config.ZendeskConfig;
import com.taxis99.zendesk.exceptions.ZendeskException;
import com.taxis99.zendesk.model.Ticket;

public class Application {

  public static void main(String[] args) {
    final Injector injector = Guice.createInjector(new AbstractModule() {
      @Override protected void configure() {
        bind(ZendeskConfig.class)
          .annotatedWith(Names.named("Authorized"))
            .to(ZendeskConfigFromProperties.class);
        /// .to(ZendeskConfigImpl.class);
        bind(Gson.class).toInstance(GsonInstanceHolder.getGsonInstance());
      }
    });

    final ZendeskApi zendeskApi = injector.getInstance(ZendeskApi.class);
    final Gson gson = GsonInstanceHolder.getDefaultBuilder().setPrettyPrinting().create();
    try {
      Ticket ticket = zendeskApi.getTicketById("1");
      System.out.println(gson.toJson(ticket));
    } catch (ZendeskException e) {
      e.printStackTrace();
    }

    try {
      List<Ticket> tickets = zendeskApi.getRecentTickets();
      System.out.println(gson.toJson(tickets));
    } catch (ZendeskException e) {
      e.printStackTrace();
    }
  }
}

//class ZendeskConfigImpl implements ZendeskConfig {
//
//  @Override public String getAuth() {
//    return "user@domain.com/token:AuTh_T0K3N-Fr0M_Z3nDesK";
//  }
//
//  @Override public String getOrganizaton() {
//    return "organization";
//  }
//}

class ZendeskConfigFromProperties implements ZendeskConfig {

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
