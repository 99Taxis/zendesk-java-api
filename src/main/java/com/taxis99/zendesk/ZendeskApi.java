package com.taxis99.zendesk;

import com.taxis99.zendesk.config.ZendeskConfig;
import com.taxis99.zendesk.exceptions.ZendeskException;
import com.taxis99.zendesk.model.Ticket;
import com.taxis99.zendesk.model.TicketFieldSpec;
import com.taxis99.zendesk.model.User;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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

  private Function<String, Ticket> jsonToTicketFn;
  private Function<String, Ticket> getJsonToTicketFn() {
    if (jsonToTicketFn == null) {
      jsonToTicketFn = new Function<String, Ticket>() {
        @Override public Ticket apply(final String result) {
          return gson.fromJson(result, TicketContainer.class).getTicket();
        }
      };
    }
    return jsonToTicketFn;
  }

  private Function<String, Set<Ticket>> jsonToSetFn;

  private Function<String, Set<Ticket>> getJsonToSetFn() {
    if (jsonToSetFn == null) {
      jsonToSetFn = new Function<String, Set<Ticket>>() {
        @Override
        public Set<Ticket> apply(final String result) {
          return gson.fromJson(result, TicketSetContainer.class).getTickets();
        }
      };
    }
    return jsonToSetFn;
  }


  private Function<String, Ticket> jsonSearchToTicketFn;

  private Function<String, Ticket> getJsonSearchToTicketFn() {
    if (jsonSearchToTicketFn == null) {
      jsonSearchToTicketFn = new Function<String, Ticket>() {
        @Override
        public Ticket apply(final String result) {
          return gson.fromJson(result, TicketSearchResult.class).getTicket();
        }
      };
    }
    return jsonSearchToTicketFn;
  }

  public Ticket postTicket(final Ticket ticket) throws ZendeskException {
    if (ticket.getId() != null) {
      throw new IllegalArgumentException("Cannot create ticket with previously set id");
    }
    return post("/api/v2/tickets.json", gson.toJson(new TicketContainer(ticket)), getJsonToTicketFn());
  }

  public Ticket updateTicket(Ticket ticket) throws ZendeskException {
    if (ticket.getId() == null) {
      throw new IllegalArgumentException("Cannot update ticket without previously set id");
    }
    return put(String.format("/api/v2/tickets/%d.json", ticket.getId()), gson.toJson(new TicketContainer(ticket)),
        getJsonToTicketFn());
  }

  public Ticket getTicketById(final Long ticketId) throws ZendeskException {
    return get("/api/v2/tickets/" + ticketId + ".json", getJsonToTicketFn());
  }

  public Ticket getTicketByCustomField(final String searchTerm) throws ZendeskException {
    return get("/api/v2/search.json?query=type:ticket%20fieldvalue:" + searchTerm, getJsonSearchToTicketFn());
  }

  public User getUserById(final Long userId) throws ZendeskException {
    return get("/api/v2/users/" + userId + ".json", new Function<String, User>() {
      @Override public User apply(final String result) {
        return gson.fromJson(result, UserContainer.class).getUser();
      }
    });
  }

  public Set<Ticket> getRecentTickets() throws ZendeskException {
    return get("/api/v2/tickets/recent.json", getJsonToSetFn());
  }

  private static Joiner commaJoiner = Joiner.on(",").skipNulls();

  public Set<Ticket> getTicketsById(Collection<Long> ticketIds) throws ZendeskException {
    return get("/api/v2/tickets/show_many.json?ids=" + commaJoiner.join(ticketIds), getJsonToSetFn());
  }

  public TicketFieldSpec getTicketFieldById(final int ticketFieldId) throws ZendeskException {
    return get("/api/v2/ticket_fields/" + ticketFieldId + ".json", new Function<String, TicketFieldSpec>() {
      @Override public TicketFieldSpec apply(final String result) {
        return gson.fromJson(result, TicketFieldSpecContainer.class).getTicketFieldSpec();
      }
    });
  }

  public Long getAssigneeId(final String email) {
    // TODO implement this
    return 0L;
  }

  private <E> E get(String apiStr, Function<String, E> fn) throws ZendeskException {
    return fn.apply(get(apiStr));
  }

  private String get(String apiStr) throws ZendeskException {
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
        return result;
      } catch (IOException e) {
        request.abort();
        throw logException("get", e);
      }
    } catch (RuntimeException e) {
      throw logException("get", e);
    }
  }

  private <E> E post(String apiStr, String ticketStr, Function<String, E> fn) throws ZendeskException {
    return fn.apply(post(apiStr, ticketStr));
  }

  private String post(String apiStr, String ticketStr) throws ZendeskException {
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
        return result;
      } catch (IOException e) {
        request.abort();
        throw logException("post", e);
      }
    } catch (RuntimeException e) {
      throw logException("post", e);
    }
  }

  private <E> E put(String apiStr, String ticketStr, Function<String, E> fn) throws ZendeskException {
    return fn.apply(put(apiStr, ticketStr));
  }

  private String put(String apiStr, String ticketStr) throws ZendeskException {
    try {
      final Request request = Request
        .Put(zendeskHost + apiStr)
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
        return result;
      } catch (IOException e) {
        request.abort();
        throw logException("put", e);
      }
    } catch (RuntimeException e) {
      throw logException("put", e);
    }
  }

  private static ZendeskException logException(String method, Exception e) {
    logger.error("Unable to " + method + " Zendesk ticket" + e.getMessage());
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

class TicketSearchResult {
  private List<Ticket> results;
  private Integer count;
  Ticket getTicket() {
    if (count != null && count > 0) {
      return results.get(0);
    }
    return null;
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
