package com.taxis99.zendesk.model;

public enum TicketStatus {
  /**
   * Ticket is awaiting assignment to a help desk agent
   */
  NEW,

  /**
   * Help desk staff is working on the ticket
   */
  OPEN,

  /**
   * Help desk staff is waiting for the requester to reply
   */
  PENDING,

   /**
    * Help desk staff is waiting for a third party
    */
  HOLD,

  /**
   * The ticket has been solved
   */
  SOLVED,

 /**
  * 
  */
  CLOSED;
}