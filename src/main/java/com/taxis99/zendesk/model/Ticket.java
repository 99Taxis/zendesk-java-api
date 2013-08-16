package com.taxis99.zendesk.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

/**
 * http://developer.zendesk.com/documentation/rest_api/tickets.html#json-format
 * 
 * @author miguel
 *
 */
public class Ticket {

  /**
   * The numeric ID of the user asking for support through the ticket.
   */
  Integer requesterId;

  @Nullable @ReadOnly Integer id;

  /**
   * Automatically assigned when creating tickets
   */
  public Integer getId() {
    return id;
  }

  /**
   * The API url of this ticket
   */
  @Nullable @ReadOnly String url;
  
  /**
   * A unique external id, you can use this to link Zendesk tickets to local records
   */
  @Nullable String externalId;
  
  /**
   * This object explains how the ticket was created
   */
  @Nullable @ReadOnly TicketVia via;

  /**
   * What agent is currently assigned to the ticket
   */
  @Nullable Integer assigneeId;
  
  /**
   * The state of the ticket
   */
  @Nullable TicketStatus status;
  
  /**
   * The type of this ticket
   */
  @Nullable TicketType type;
  
  /**
   * Priority, defines the urgency with which the ticket should be addressed
   */
  @Nullable TicketPriority priority;
  
  /**
   * The problem this incident is linked to, if any
   */
  @Nullable Integer problemId;
  
  /**
   * Is true of this ticket has been marked as a problem, false otherwise
   */
  @Nullable @ReadOnly Boolean hasIncidents;
  
  /**
   * When this record was created
   */
  @Nullable @ReadOnly Date createdAt;
  
  /**
   * When this record last got updated
   */
  @Nullable @ReadOnly Date updatedAt;
  
  /**
   * If this is a ticket of type "task" it has a due date
   */
  @Nullable Date dueAt; // for tasks
  
  /**
   * The array of tags applied to this ticket
   */
  Set<String> tags;

  /**
   * Check if the array of tags of this ticket contains the given tag
   * @param tag 
   * @return true if the ticket contains this tag, false otherwise
   */
  public boolean hasTag(String tag) {
    return tags.contains(tag);
  }

  /**
   * The custom fields of the ticket
   */
  List<String> customFields;
  
  /**
   * The value of the subject field for this ticket
   */
  @Nullable String subject;
  
  // For ticket creation
  
  /**
   * An object that adds a comment to the ticket.
   */
  @Nullable TicketComment comment;
  
  /**
   * Requesters can explicitly be created handling tickets
   */
  @Nullable TicketRequester requester;

}