package com.taxis99.zendesk.model;

import javax.annotation.Nullable;

class TicketRequester {
  @Nullable final Integer locale_id;
  final String name;
  final String email;
  @Nullable final String phone;

  TicketRequester(final String name, final String email, final String phone) {
    this.locale_id = 8;
    this.name = name;
    this.email = email;
    this.phone = phone;
  }

  TicketRequester(final String name, final String email) {
    this(name, email, null);
  }
}