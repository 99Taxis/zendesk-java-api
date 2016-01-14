package com.taxis99.zendesk;

import com.google.gson.Gson;
import com.taxis99.zendesk.config.GsonInstanceHolder;
import com.taxis99.zendesk.config.ZendeskConfigFromProperties;
import com.taxis99.zendesk.exceptions.ZendeskException;
import com.taxis99.zendesk.model.Ticket;
import com.taxis99.zendesk.model.TicketFieldSpec;
import com.taxis99.zendesk.model.User;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;

import static org.junit.Assert.*;

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
  public void testGetTicketByCustomField() throws ZendeskException{
    String customValue = "1515125";
    final Ticket ticketByCustomField = zendeskApi.getTicketByCustomField(customValue);
    assertEquals("testGetTicketByCustomField", ticketByCustomField.getSubject());
  }

  @Test
  public void testGetUserById() throws ZendeskException{
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
