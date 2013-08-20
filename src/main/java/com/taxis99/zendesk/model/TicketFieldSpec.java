package com.taxis99.zendesk.model;

import java.util.List;

public class TicketFieldSpec {
  int id;
  String title;
  List<TicketFieldOption> customFieldOptions;
}

class TicketFieldOption {
  String name;
  String value;
}