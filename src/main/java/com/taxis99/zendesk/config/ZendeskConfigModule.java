package com.taxis99.zendesk.config;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class ZendeskConfigModule extends AbstractModule {
  @Override protected void configure() {
    bind(ZendeskConfig.class)
      .annotatedWith(Names.named("Authorized"))
        .to(ZendeskConfigFromProperties.class);
  }
}
