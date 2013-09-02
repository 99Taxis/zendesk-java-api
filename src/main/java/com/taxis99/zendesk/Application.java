package com.taxis99.zendesk;

import java.util.Set;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.taxis99.zendesk.config.GsonInstanceHolder;
import com.taxis99.zendesk.config.ZendeskConfigModule;
import com.taxis99.zendesk.exceptions.ZendeskException;
import com.taxis99.zendesk.model.Ticket;
import com.taxis99.zendesk.model.TicketFieldSpec;

public class Application {

  public static void main(String[] args) {
    final Injector injector = Guice.createInjector(new ZendeskConfigModule(), new AbstractModule() {
      @Override protected void configure() {
        bind(Gson.class).toInstance(GsonInstanceHolder.getGsonInstance());
      }
    });
    final ZendeskExampleApp zendeskExampleApp = injector.getInstance(ZendeskExampleApp.class);
    zendeskExampleApp.printTicket();
    zendeskExampleApp.printTicketsList();
    zendeskExampleApp.printTicketFieldSpec();
  }
}

class ZendeskExampleApp {

  private final ZendeskApi zendeskApi;
  private final Gson gson = GsonInstanceHolder.getDefaultBuilder().setPrettyPrinting().create();

  @Inject ZendeskExampleApp(ZendeskApi zendeskApi) {
    this.zendeskApi = zendeskApi;
  }

  void printTicket() {
    try {
      Ticket ticket = zendeskApi.getTicketById(1);
      System.out.println(gson.toJson(ticket));
    } catch (ZendeskException e) {
      e.printStackTrace();
    }
  }

  void printTicketsList() {
    try {
      Set<Ticket> tickets = zendeskApi.getRecentTickets();
      System.out.println(gson.toJson(tickets));
    } catch (ZendeskException e) {
      e.printStackTrace();
    }
  }
  
  void printTicketFieldSpec() {
    try {
      TicketFieldSpec ticket = zendeskApi.getTicketFieldById(1);
      System.out.println(gson.toJson(ticket));
    } catch (ZendeskException e) {
      e.printStackTrace();
    }
  }
}

