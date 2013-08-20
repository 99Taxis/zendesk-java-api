package com.taxis99.zendesk.config;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class ZendeskConfigModule extends AbstractModule {
  @Override protected void configure() {
    bind(ZendeskConfig.class)
      .annotatedWith(Names.named("Authorized"))
        .to(ZendeskConfigFromProperties.class);
    /// .to(ZendeskConfigImpl.class);
    bind(Gson.class).toInstance(GsonInstanceHolder.getGsonInstance());
  }
}

//class ZendeskConfigImpl implements ZendeskConfig {
//  @Override public String getAuth() {
//    return "user@domain.com/token:AuTh_T0K3N-Fr0M_Z3nDesK";
//  }
//  @Override public String getSubdomain() {
//    return "subdomain";
//  }
//}