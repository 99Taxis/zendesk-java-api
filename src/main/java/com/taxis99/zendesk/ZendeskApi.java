package com.taxis99.zendesk;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.StringEntity;
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
import com.taxis99.zendesk.model.TicketFieldSpec;
import com.taxis99.zendesk.model.User;

public class ZendeskApi {
  private static final Logger logger = LoggerFactory.getLogger(ZendeskApi.class);

  private final Gson gson;

  private final String authEncoded;
  private final String zendeskHost;
  private final int connTimeout;

  @Inject public ZendeskApi(final Gson gson, @Named("Authorized") final ZendeskConfig config) {
    this.gson = gson;
    this.authEncoded = new String(Base64.encodeBase64(config.getAuth().getBytes()), StandardCharsets.US_ASCII);
    this.zendeskHost = "https://" + config.getSubdomain() + ".zendesk.com";
    this.connTimeout = config.getConnTimeout();
  }

  public void postTicket(final Ticket ticket) throws ZendeskException {
    post("/api/v2/tickets.json", gson.toJson(new TicketContainer(ticket)));
  }

  public Ticket getTicketById(final int ticketId) throws ZendeskException {
    return get("/api/v2/tickets/" + ticketId + ".json", new Function<String, Ticket>() {
      @Override public Ticket apply(final String result) {
        return gson.fromJson(result, TicketContainer.class).getTicket();
      }
    });
  }
  
  public User getUserById(final int userId) throws ZendeskException {
    return get("/api/v2/users/" + userId + ".json", new Function<String, User>() {
      @Override public User apply(final String result) {
        return gson.fromJson(result, UserContainer.class).getUser();
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
  
  public TicketFieldSpec getTicketFieldById(final int ticketFieldId) throws ZendeskException {
    return get("/api/v2/ticket_fields/" + ticketFieldId + ".json", new Function<String, TicketFieldSpec>() {
      @Override public TicketFieldSpec apply(final String result) {
        return gson.fromJson(result, TicketFieldSpecContainer.class).getTicketFieldSpec();
      }
    });
  }

  private <E> E get(String apiStr, Function<String, E> fn) throws ZendeskException {
    try {
      final Request request = Request
        .Get(zendeskHost + apiStr)
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Basic " + authEncoded)
        .connectTimeout(connTimeout);
      try {
        HttpResponse response = request
          .execute()
          .returnResponse();
        String result = EntityUtils.toString(response.getEntity());
        logger.debug(result);
        return fn.apply(result);
      } catch (IOException e) {
        request.abort();
        throw logException("get", e);
      }
    } catch (RuntimeException e) {
      throw logException("get", e);
    }
  }

  private void post(String apiStr, String ticketStr) throws ZendeskException {
    try {
      final Request request = Request
        .Post(zendeskHost + apiStr)
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Basic " + authEncoded)
        .body(new StringEntity(ticketStr, StandardCharsets.UTF_8))
        .connectTimeout(connTimeout);
      try {
  	    HttpResponse response = request
          .execute()
          .returnResponse();
        String result = EntityUtils.toString(response.getEntity());
        logger.debug(result);
      } catch (IOException e) {
        request.abort();
        throw logException("post", e);
      }
    } catch (RuntimeException e) {
      throw logException("post", e);
    }
  }

  private ZendeskException logException(String method, Exception e) throws ZendeskException {
    logger.error("Unable to {} Zendesk ticket", method, e);
    return new ZendeskException("Unable to " + method + " Zendesk ticket", e);
  }
}

class UserContainer {
  private User user;
  public UserContainer(User user) {
    this.user = user;
  }
  User getUser() {
    return user;
  }
}

class TicketContainer {
  private Ticket ticket;
  public TicketContainer(Ticket ticket) {
    this.ticket = ticket;
  }
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

class TicketFieldSpecContainer {
  private TicketFieldSpec ticketField;
  public TicketFieldSpec getTicketFieldSpec() {
    return ticketField;
  }
}
