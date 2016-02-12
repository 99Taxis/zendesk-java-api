package com.taxis99.zendesk.model;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

class TicketComment {
  @SerializedName("public") final boolean _public;
  @Nullable final String body;
  @Nullable final String htmlBody;

  TicketComment(final boolean _public, final String body, final String htmlBody) {
    this._public = _public;
    this.body = body;
    this.htmlBody = htmlBody;
  }

  TicketComment(final boolean _public, final String body) {
    this(_public, body, null);
  }
}

class HtmlTicketComment extends TicketComment {

  HtmlTicketComment(boolean _public, String htmlBody) {
    super(_public, null, htmlBody);
  }
}