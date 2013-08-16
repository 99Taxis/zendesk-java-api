package com.taxis99.zendesk;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.taxis99.zendesk.config.ZendeskConfig;
import com.taxis99.zendesk.exceptions.ZendeskException;
import com.taxis99.zendesk.model.Ticket;

public class ZendeskApi {
  private static final Logger logger = LoggerFactory.getLogger(ZendeskApi.class);

  private final Gson gson;

  private final String authEncoded;
  private final String organization;

  @Inject public ZendeskApi(final Gson gson, @Named("Authorized") final ZendeskConfig config) {
    this.gson = gson;
    this.authEncoded = new String(Base64.encodeBase64(config.getAuth().getBytes()), StandardCharsets.US_ASCII);
    this.organization = config.getOrganizaton();
  }

  public Ticket getTicketById(final String ticketId) throws ZendeskException {
    return get("/api/v2/tickets/" + ticketId + ".json", new Function<String, Ticket>() {
      @Override public Ticket apply(final String result) {
        return gson.fromJson(result, TicketContainer.class).getTicket();
      }
    });
  }
  
  public List<Ticket> getRecentTickets() throws ZendeskException {
    return get("/api/v2/tickets/recent.json", new Function<String, List<Ticket>>() {
      @Override public List<Ticket> apply(final String result) {
        return gson.fromJson(result, TicketListContainer.class).getTickets();
      }
    });
  }

  private <E> E get(String apiStr, Function<String, E> fn) throws ZendeskException {
    try {
      HttpResponse response = Request
        .Get("https://" + organization + ".zendesk.com" + apiStr)
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Basic " + authEncoded)
        .execute().returnResponse();
      String result = EntityUtils.toString(response.getEntity());
      logger.debug(result);
      return fn.apply(result);
    } catch (IOException | RuntimeException e) {
      logger.error("Unable to get Zendesk tickets", e);
      throw new ZendeskException("Unable to get Zendesk tickets", e);
    }
  }
}

class TicketContainer {
  private Ticket ticket;
  Ticket getTicket() {
    return ticket;
  }
}

class TicketListContainer {
  private List<Ticket> tickets;
  List<Ticket> getTickets() {
    return tickets;
  }
}
