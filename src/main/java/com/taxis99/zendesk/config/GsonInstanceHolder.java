package com.taxis99.zendesk.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.taxis99.zendesk.model.TicketPriority;
import com.taxis99.zendesk.model.TicketStatus;
import com.taxis99.zendesk.model.TicketType;
import com.taxis99.zendesk.model.UserRole;
import com.taxis99.zendesk.serialize.EnumUpperCaseTypeAdapter;

public class GsonInstanceHolder {
  private static final GsonBuilder defaultBuilder = new GsonBuilder()
    .registerTypeAdapter(TicketStatus.class, EnumUpperCaseTypeAdapter.of(TicketStatus.class))
    .registerTypeAdapter(TicketType.class, EnumUpperCaseTypeAdapter.of(TicketType.class))
    .registerTypeAdapter(TicketPriority.class, EnumUpperCaseTypeAdapter.of(TicketPriority.class))
    .registerTypeAdapter(UserRole.class, EnumUpperCaseTypeAdapter.of(UserRole.class))
    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX") // ISO 8601
    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);

  private static final Gson gson = defaultBuilder.create();

  public static GsonBuilder getDefaultBuilder() {
    return defaultBuilder;
  }

  public static Gson getGsonInstance() {
    return gson;
  }
}
