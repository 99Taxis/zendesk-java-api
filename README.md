zendesk-java-api
================

[![Build Status](https://travis-ci.org/99taxis/zendesk-java-api.svg?branch=master)](https://travis-ci.org/99taxis/zendesk-java-api) [![Codacy Badge](https://api.codacy.com/project/badge/grade/6cddab4d424143e78b31f34935f16f45)](https://www.codacy.com/app/99taxis/zendesk-java-api) [![Coverage Status](https://coveralls.io/repos/github/99taxis/zendesk-java-api/badge.svg?branch=master)](https://coveralls.io/github/99taxis/zendesk-java-api?branch=master "Coveralls") [![Dependencies](https://app.updateimpact.com/badge/704215565069324288/zendesk-java-api.svg?config=test)](https://app.updateimpact.com/latest/704215565069324288/zendesk-java-api) [![Dependency Status](https://www.versioneye.com/user/projects/56e7b4de96f80c0054235070/badge.svg?style=flat)](https://www.versioneye.com/user/projects/56e7b4de96f80c0054235070) [![Join the chat at https://gitter.im/99taxis/zendesk-java-api](https://badges.gitter.im/99taxis/zendesk-java-api.svg)](https://gitter.im/99taxis/zendesk-java-api?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Zendesk Java API is a simple client library for Java that provides an interface to the [Zendesk Core API](https://developer.zendesk.com/rest_api/docs/core/introduction).

_Note: zendesk-java-api is a partial implementation of the Zendesk Core API. The Zendesk Core API and the Zendesk Java API are both under development, and this library is subject to frequent change._

[![License](http://img.shields.io/:license-MIT-blue.svg)](https://github.com/99taxis/zendesk-java-api/blob/master/LICENSE "MIT Licence") [![GitHub tag](https://img.shields.io/github/tag/99taxis/zendesk-java-api.svg)](https://github.com/99taxis/zendesk-java-api/tags) [![Maintenance](https://img.shields.io/maintenance/yes/2016.svg)](https://github.com/99taxis/awsscala/commits/master)


### Supported Methods

Most of the [tickets](https://developer.zendesk.com/rest_api/docs/core/tickets) and [search](https://developer.zendesk.com/rest_api/docs/core/search) APIs, and some of the [users](https://developer.zendesk.com/rest_api/docs/core/users) and [ticket_fields](https://developer.zendesk.com/rest_api/docs/core/ticket_fields) APIs.


Download
--------

Zendesk Java API is not yet listed on Maven Central nor any real maven repository, but we use [our raw Github public maven repo](https://github.com/99taxis/maven-public). So, to use it, add the repository to your `pom.xml` file:
```
<repository>
  <id>99taxis-github-maven-repo</id>
  <url>https://raw.github.com/99taxis/maven-public/master/releases</url>
  <snapshots>
    <enabled>false</enabled>
  </snapshots>
</repository>
```
and the `zendesk-java-api` lib

```
<dependency>
  <groupId>com.taxis99</groupId>
  <artifactId>zendesk-java-api</artifactId>
  <version>LATEST_VERSION</version>
</dependency>
```

`zendesk-java-api` supports Java 8.


Usage
-----

First, set the credentials in a properties file (see [zendesk-sample.properties](https://github.com/99taxis/zendesk-java-api/blob/master/src/main/resources/zendesk-sample.properties)), set some environment variables, or extend `ZendeskConfig` yourself and bind the config and a default `Gson` instance.

```java
import com.google.gson.Gson;
import com.taxis99.zendesk.config.GsonInstanceHolder;
import com.taxis99.zendesk.config.ZendeskConfig;
import com.taxis99.zendesk.config.ZendeskConfigFromEnvironment;
import com.taxis99.zendesk.config.ZendeskConfigFromProperties;

...

  final Gson gson;
  final ZendeskConfig config;

  gson = GsonInstanceHolder.getDefaultBuilder().setPrettyPrinting().create();
  if (ZendeskApiTest.class.getResource("/zendesk.properties") != null) {
    config = new ZendeskConfigFromProperties();
  } else {
    config = new ZendeskConfigFromEnvironment();
  }

```

After that you instantiate a ZendeskApi object.
```java
import com.taxis99.zendesk.ZendeskApi;

...

  final ZendeskApi zendeskApi = new ZendeskApi(gson, config);
```

You can then use Zendesk API methods:
```java
import com.taxis99.zendesk.ZendeskApi;
import com.taxis99.zendesk.model.Ticket;

...

  final Ticket ticket = zendeskApi.getTicketById(46239L);
```


License
-------

`zendesk-java-api` is an open source software released under the MIT License.

See the [LICENSE](https://github.com/99taxis/zendesk-java-api/blob/master/LICENSE) file for details.
