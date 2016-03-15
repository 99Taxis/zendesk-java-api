package com.taxis99.zendesk;

import static java.lang.String.format;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
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

  private Gson gson;

  private final String authEncoded;
  private final String zendeskHost;
  private final int connTimeout;

  private final Function<String, Ticket>           parseJsonToTicket          = result -> gson.fromJson(result, TicketContainer.class).getTicket();
  private final Function<String, Set<Ticket>>      parseJsonToTicketSet       = result -> gson.fromJson(result, TicketSetContainer.class).getTickets();
  private final Function<String, Optional<Ticket>> parseJsonSearchToTicket    = result -> gson.fromJson(result, TicketSearchResult.class).getFirstTicket();
  private final Function<String, User>             parseJsonToUser            = result -> gson.fromJson(result, UserContainer.class).getUser();
  private final Function<String, TicketFieldSpec>  parseJsonToTicketFieldSpec = result -> gson.fromJson(result, TicketFieldSpecContainer.class).getTicketFieldSpec();

  @Inject public ZendeskApi(@Nonnull Gson gson, @Named("Authorized") final ZendeskConfig config) {
    this.gson = gson;
    this.authEncoded = new String(Base64.encodeBase64(config.getAuth().getBytes()), StandardCharsets.US_ASCII);
    this.zendeskHost = format("https://%s.zendesk.com", config.getSubdomain());
    this.connTimeout = config.getConnTimeout();
  }

  public Ticket postTicket(final Ticket ticket) throws ZendeskException {
    Preconditions.checkArgument(ticket.getId() == null, "Cannot create ticket with previously set id");
    return post("/api/v2/tickets.json", gson.toJson(new TicketContainer(ticket)), parseJsonToTicket);
  }

  public Ticket updateTicket(Ticket ticket) throws ZendeskException {
    Preconditions.checkArgument(ticket.getId() != null, "Cannot update ticket without previously set id");
    return put(format("/api/v2/tickets/%d.json", ticket.getId()), gson.toJson(new TicketContainer(ticket)),
      parseJsonToTicket);
  }

  public Ticket getTicketById(final Long ticketId) throws ZendeskException {
    return get(format("/api/v2/tickets/%d.json",ticketId), parseJsonToTicket);
  }

  public Optional<Ticket> findTicketByFieldValue(final String searchTerm) throws ZendeskException {
    return get(format("/api/v2/search.json?query=type:ticket%%20fieldvalue:%s", searchTerm), parseJsonSearchToTicket);
  }

  public Optional<Ticket> findTicketByExternalId(final String externalId) throws ZendeskException {
    return get(format("/api/v2/search.json?query=type:ticket%%20external_id:%s", externalId), parseJsonSearchToTicket);
  }

  public User getUserById(final Long userId) throws ZendeskException {
    return get(format("/api/v2/users/%s.json", userId), parseJsonToUser);
  }

  public Set<Ticket> getRecentTickets() throws ZendeskException {
    return get("/api/v2/tickets/recent.json", parseJsonToTicketSet);
  }

  public Set<Ticket> getTicketsById(Collection<Long> ticketIds) throws ZendeskException {
    Joiner commaJoiner = Joiner.on(",").skipNulls();
    return get(format("/api/v2/tickets/show_many.json?ids=%s", commaJoiner.join(ticketIds)), parseJsonToTicketSet);
  }

  public TicketFieldSpec getTicketFieldById(final int ticketFieldId) throws ZendeskException {
    return get(format("/api/v2/ticket_fields/%d.json", ticketFieldId), parseJsonToTicketFieldSpec);
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

class TicketSearchResult {
  private List<Ticket> results;
  private Integer count;
  Optional<Ticket> getFirstTicket() {
    if (count != null && count > 0) {
      return Optional.of(results.get(0));
    }
    return Optional.empty();
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
