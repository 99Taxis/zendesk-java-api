package com.taxis99.zendesk.model;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class TicketFieldSpec {
  private int id;
  private String title;
  private List<TicketFieldOption> customFieldOptions;

  public int getId() {
    return id;
  }
  public String getTitle() {
    return title;
  }
  public List<TicketFieldOption> getCustomFieldsOptions() {
    return ImmutableList.copyOf(customFieldOptions);
  }

  public static class TicketFieldOption {
    private String name;
    private String value;

    public String getName() {
      return name;
    }
    public String getValue() {
      return value;
    }
  }
}