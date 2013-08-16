package com.taxis99.zendesk.model;

import javax.annotation.Nullable;

class TicketRequester {
  @Nullable final Integer locale_id;
  final String name;
  final String email;

  TicketRequester(final String name, final String email) {
    this.locale_id = 8;
    this.name = name;
    this.email = email;
  }
}