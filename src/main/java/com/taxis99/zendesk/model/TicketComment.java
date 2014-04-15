package com.taxis99.zendesk.model;

import com.google.gson.annotations.SerializedName;

class TicketComment {
  @SerializedName("public") final boolean _public;
  final String body;

  TicketComment(final boolean _public, final String body) {
    this._public = _public;
    this.body = body;
  }
}