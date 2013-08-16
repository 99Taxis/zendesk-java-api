package com.taxis99.zendesk;

import java.util.List;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.taxis99.zendesk.config.GsonInstanceHolder;
import com.taxis99.zendesk.config.ZendeskConfigModule;
import com.taxis99.zendesk.exceptions.ZendeskException;
import com.taxis99.zendesk.model.Ticket;

public class Application {

  public static void main(String[] args) {
    final Injector injector = Guice.createInjector(new ZendeskConfigModule());
    final ZendeskExampleApp zendeskExampleApp = injector.getInstance(ZendeskExampleApp.class);
    zendeskExampleApp.printTicket();
    zendeskExampleApp.printTicketsList();
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
      Ticket ticket = zendeskApi.getTicketById("1");
      System.out.println(gson.toJson(ticket));
    } catch (ZendeskException e) {
      e.printStackTrace();
    }
  }

  void printTicketsList() {
    try {
      List<Ticket> tickets = zendeskApi.getRecentTickets();
      System.out.println(gson.toJson(tickets));
    } catch (ZendeskException e) {
      e.printStackTrace();
    }
  }
}

