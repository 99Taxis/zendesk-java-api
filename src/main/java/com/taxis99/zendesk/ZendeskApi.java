package com.taxis99.zendesk;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
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

  public Ticket getTicketById(final int ticketId) throws ZendeskException {
    return get("/api/v2/tickets/" + ticketId + ".json", new Function<String, Ticket>() {
      @Override public Ticket apply(final String result) {
        return gson.fromJson(result, TicketContainer.class).getTicket();
      }
    });
  }

  private Function<String, Set<Ticket>> jsonToSetFn;
  private Function<String, Set<Ticket>> getJsonToSetFn() {
    if (jsonToSetFn == null) {
      jsonToSetFn = new Function<String, Set<Ticket>>() {
        @Override public Set<Ticket> apply(final String result) {
          return gson.fromJson(result, TicketSetContainer.class).getTickets();
        }
      };
    }
    return jsonToSetFn;
  }

  public Set<Ticket> getRecentTickets() throws ZendeskException {
    return get("/api/v2/tickets/recent.json", getJsonToSetFn());
  }

  private static Joiner commaJoiner = Joiner.on(",").skipNulls();

  public Set<Ticket> getTicketsById(Collection<Integer> ticketIds) throws ZendeskException {
    return get("/api/v2/tickets/show_many.json?ids=" + commaJoiner.join(ticketIds), getJsonToSetFn());
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

class TicketSetContainer {
  private Set<Ticket> tickets;
  Set<Ticket> getTickets() {
    return tickets;
  }
}
