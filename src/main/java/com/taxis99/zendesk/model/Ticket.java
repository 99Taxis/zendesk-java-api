package com.taxis99.zendesk.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.taxis99.zendesk.config.GsonInstanceHolder;

/**
 * http://developer.zendesk.com/documentation/rest_api/tickets.html#json-format
 * 
 * @author miguel
 *
 */
public class Ticket implements Comparable<Ticket> {

  Integer requesterId;

  /**
   * The numeric ID of the user asking for support through the ticket.
   */
  public Integer getRequesterId() {
    return requesterId;
  }

  /**
   * The numeric ID of the user asking for support through the ticket.
   */
  public void setRequesterId(Integer requesterId) {
    this.requesterId = requesterId;
  }

  Integer id;

  /**
   * Automatically assigned when creating tickets
   */
  @Nullable public Integer getId() {
    return id;
  }

  String url;

  /**
   * The API url of this ticket
   */
  @Nullable public String getUrl() {
    return url;
  }

  String externalId;

  /**
   * A unique external id, you can use this to link Zendesk tickets to local records
   */
  @Nullable public String getExternalId() {
    return externalId;
  }

  /**
   * A unique external id, you can use this to link Zendesk tickets to local records
   */
  public void setExternalId(String externalId) {
    this.externalId = externalId;
  }

  String subject;

  /**
   * The value of the subject field for this ticket
   */
  @Nullable public String getSubject() {
    return subject;
  }

  /**
   * The value of the subject field for this ticket
   */
  public void setSubject(String subject) {
    this.subject = subject;
  }

  TicketVia via;

  /**
   * This object explains how the ticket was created
   */
  @Nullable public TicketVia getVia() {
    return via;
  }


  Integer assigneeId;

  /**
   * What agent is currently assigned to the ticket
   */
  @Nullable public Integer getAssigneeId() {
    return assigneeId;
  }

  /**
   * What agent is going to be assigned to the ticket
   */
  public void setAssigneeId(Integer assigneeId) {
    this.assigneeId = assigneeId;
  }

  TicketStatus status;

  /**
   * The state of the ticket
   */
  @Nullable public TicketStatus getStatus() {
    return status;
  }

  /**
   * The state of the ticket
   */
  public void setStatus(TicketStatus status) {
    this.status = status;
  }

  TicketType type;

  /**
   * The type of this ticket
   */
  @Nullable public TicketType getType() {
    return type;
  }

  /**
   * The type of this ticket
   */
  public void setType(TicketType type) {
    this.type = type;
  }

  TicketPriority priority;

  /**
   * Priority, defines the urgency with which the ticket should be addressed
   */
  @Nullable public TicketPriority getPriority() {
    return priority;
  }

  /**
   * Priority, defines the urgency with which the ticket should be addressed
   */
  public void setPriority(TicketPriority priority) {
    this.priority = priority;
  }
  

  Integer problemId;

  /**
   * The problem this incident is linked to, if any
   */
  @Nullable public Integer getProblemId() {
    return problemId;
  }

  /**
   * The problem this incident will be linked to
   */
  public void setProblemId(Integer problemId) {
    this.problemId = problemId;
  }

  Boolean hasIncidents;

  /**
   * Is true of this ticket has been marked as a problem, false otherwise
   */
  @Nullable public Boolean getHasIncidents() {
    return hasIncidents;
  }

  Date createdAt;

  /**
   * When this record was created
   */
  @Nullable public Date getCreatedAt() {
    return createdAt;
  }
  

  Date updatedAt;

  /**
   * When this record last got updated
   */
  @Nullable public Date getUpdatedAt() {
    return updatedAt;
  }
  

  Date dueAt; // for tasks

  /**
   * If this is a ticket of type "task" it has a due date
   */
  @Nullable public Date getDueAt() {
    return dueAt;
  }

  /**
   * Sets the due date of this ticket of type "task" 
   * 
   * @throws IllegalStateException if the ticket is not of the type "task"
   */
  public void setDueAt(Date dueAt) {
    if (this.type != TicketType.TASK) {
      throw new IllegalStateException("Only a ticket of type task can have a due date");
    }
    this.dueAt = dueAt;
  }
  

  Set<String> tags;

  /**
   * The array of tags applied to this ticket
   */
  public Set<String> getTags() {
    return tags;
  }

  /**
   * Sets a new array of tags applied to this ticket
   */
  public void setTags(Set<String> tags) {
    this.tags = tags;
  }

  /**
   * Check if the array of tags of this ticket contains the given tag
   * @param tag 
   * @return true if the ticket contains this tag, false otherwise
   */
  public boolean hasTag(String tag) {
    return tags.contains(tag);
  }

  
  List<TicketCustomField> customFields;

  /**
   * The custom fields of the ticket
   */
  public List<TicketCustomField> getCustomFields() {
    return customFields;
  }

  /**
   * The custom fields of the ticket
   */
  public void setCustomFields(List<TicketCustomField> customFields) {
    this.customFields = customFields;
  }

  @Override public int compareTo(Ticket other) {
    return this.id.compareTo(other.id);
  }

  private static final Gson gson = GsonInstanceHolder.getDefaultBuilder().setPrettyPrinting().create();
  
  @Override public String toString() {
    return "{ " + super.toString() + ": " + gson.toJson(this) + " }";
  }
  
  // For ticket creation

  @Nullable TicketComment comment;

  /**
   * An object that adds a comment to the ticket.
   */
  public void setComment(TicketComment comment) {
    this.comment = comment;
  }

  @Nullable TicketRequester requester;

  /**
   * Requesters can explicitly be created handling tickets
   */
  public void setRequester(TicketRequester requester) {
    this.requester = requester;
  }
  
  public static TicketBuilder Builder() {
    return new TicketBuilder();
  }
  
  public static class TicketBuilder {
    final Ticket instance = new Ticket();
    boolean mutable = true;
    private void checkState() {
      if (!mutable) {
        throw new IllegalStateException("Cannot change state after build");
      }
    }
    public TicketBuilder withSubject(String subject) {
      checkState();
      instance.setSubject(subject);
      return this;
    }
    public TicketBuilder withComment(String commentBody) {
      checkState();
      instance.setComment(new TicketComment(commentBody));
      return this;
    }
    public TicketBuilder byRequester(String name, String email) {
      checkState();
      instance.setRequester(new TicketRequester(name, email));
      return this;
    }
    public Ticket build() {
      mutable = false;
      return instance;
    }
  }
}