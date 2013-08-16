package com.taxis99.zendesk.model;

public class TicketVia {

  /**
   * This tells you how the ticket or event was created
   */
  String channel;
  
  /**
   * For some channels a source object gives more information about how or why
   * the ticket or event was created
   */
  Object source;
}
