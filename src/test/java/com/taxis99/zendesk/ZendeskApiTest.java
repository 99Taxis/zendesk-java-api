package com.taxis99.zendesk;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;

import com.google.gson.Gson;
import com.taxis99.zendesk.config.GsonInstanceHolder;
import com.taxis99.zendesk.config.ZendeskConfigFromProperties;
import com.taxis99.zendesk.exceptions.ZendeskException;
import com.taxis99.zendesk.model.Ticket;
import com.taxis99.zendesk.model.TicketFieldSpec;
import com.taxis99.zendesk.model.User;

public class ZendeskApiTest {
  private final ZendeskApi zendeskApi;

  private final Gson gson = GsonInstanceHolder.getDefaultBuilder().setPrettyPrinting().create();
  public ZendeskApiTest() {
    zendeskApi = new ZendeskApi(gson, new ZendeskConfigFromProperties());
  }


  @Test
  public void testPostTicket() throws ZendeskException {
    Ticket newTicket = Ticket.Builder().withSubject("testPostTicket").withDescription("testPostTicket").build();
    final Ticket ticketCreated = zendeskApi.postTicket(newTicket);

    assertNotNull(ticketCreated);
    assertEquals("testPostTicket", ticketCreated.getSubject());
  }

  @Test
  public void testPostTicketWithHTML() throws ZendeskException {
    Ticket newTicket = Ticket.Builder().withSubject("testPostTicketWithHtml")
            .withDescription("testPostTicketWithHtml")
            .withHtmlComment("<a href='http://www.99taxis.com'>99Taxis</a>")
            .build();
    final Ticket ticketCreated = zendeskApi.postTicket(newTicket);

    assertNotNull(ticketCreated);
  }

  @Test
  public void testPostTicketWithGroupId() throws ZendeskException {
    Ticket newTicket = Ticket.Builder()
            .withSubject("testPostTicket")
            .withDescription("testPostTicket")
            .belongsToGroupId(28113417L)
            .build();

    final Ticket ticketCreated = zendeskApi.postTicket(newTicket);
    assertNotNull(ticketCreated);
    assertEquals(Long.valueOf(28113417L), ticketCreated.getGroupId());
  }

  @Test
  public void testUpdateTicket() throws ZendeskException {
    Ticket newTicket = Ticket.Builder().withSubject("testUpdateTicket").withDescription("testUpdateTicket").build();
    final Ticket ticketCreated = zendeskApi.postTicket(newTicket);

    ticketCreated.setSubject("testUpdateTicket 2");
    final Ticket ticketUpdated = zendeskApi.updateTicket(ticketCreated);

    assertNotNull(ticketUpdated);
    assertEquals("testUpdateTicket 2", ticketCreated.getSubject());

  }

  @Test
  public void testGetTicketById() throws ZendeskException {
    final Ticket ticketById = zendeskApi.getTicketById(1L);
    assertNotNull(ticketById);
    assertEquals(ticketById.getId(), new Long(1L));
  }

  @Test
  public void testFindTicketByFieldValue() throws ZendeskException {
    String customValue = "1515125";
    final Ticket ticketByFieldValue = zendeskApi.findTicketByFieldValue(customValue).get();
    assertEquals(1, ticketByFieldValue.getCustomFields().stream()
      .filter(field -> field.getValue() != null)
      .map(field -> field.getValue())
      .filter(fieldValue -> fieldValue.equalsIgnoreCase(customValue))
      .count());
  }

  @Test
  public void testFindTicketByFieldValueWithNullResult() throws ZendeskException {
    String fieldValue = "whatever";
    final Optional<Ticket> ticketByFieldValue = zendeskApi.findTicketByFieldValue(fieldValue);
    assertEquals(Optional.<Ticket> empty(), ticketByFieldValue);
  }

  @Test
  public void testFindTicketByExternalId() throws ZendeskException, InterruptedException {
    final Optional<Ticket> ticketByExternalId = zendeskApi.findTicketByExternalId("unitTest74");
    assertNotEquals(Optional.<Ticket> empty(), ticketByExternalId);
    assertEquals("testGetTicketByExternalId", ticketByExternalId.get().getSubject());
  }

  @Test
  public void testGetTicketByExternalIdWithNullResult() throws ZendeskException, InterruptedException {
    final Optional<Ticket> ticketByExternalId = zendeskApi.findTicketByExternalId("whatever");
    assertEquals(Optional.<Ticket> empty(), ticketByExternalId);
  }

  @Test
  public void testGetUserById() throws ZendeskException {
    final User userById = zendeskApi.getUserById(4081484028L);
    assertNotNull(userById);
    assertEquals("Pedro Somma", userById.getName());
  }

  @Test
  public void testGetRecentTickets() throws ZendeskException {
    final Set<Ticket> recentTickets = zendeskApi.getRecentTickets();
    assertNotNull(recentTickets);
    assertTrue("recent tickets should not be null", !recentTickets.isEmpty());
  }

  @Test
  public void testGetTicketsById() throws ZendeskException {
    Long[] ids = {1L,2L};

    final Set<Ticket> ticketsById = zendeskApi.getTicketsById(Arrays.asList(ids));
    assertNotNull(ticketsById);
    assertTrue("Should be size 2", ticketsById.size() == 2);

  }

  @Test
  public void testGetTicketFieldById() throws ZendeskException {
    final TicketFieldSpec ticketFieldById = zendeskApi.getTicketFieldById(29872748);
    assertNotNull(ticketFieldById);
  }


}
