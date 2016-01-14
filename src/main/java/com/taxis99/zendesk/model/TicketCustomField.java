package com.taxis99.zendesk.model;

public class TicketCustomField {
  private int id;
  private String value;

  public TicketCustomField(int id, String value) {
    this.id = id;
    this.value = value;
  }

  public TicketCustomField(){};

  public int getId() {
    return id;
  }
  public String getValue() {
    return value;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
