# es-client

A Java library that lets you talk to **different versions of Elasticsearch** (8.5, 8.13, 9.1) using one set of APIs. Your app can switch versions by config instead of changing code.

---

## What it does

- You give it **connection details** (hosts, username, password, socket timeout) and an **ES version** (e.g. `8.5`).
- It gives you a client for that version. Search and index operations work the same way for all versions.
- Each version (8.5, 8.13, 9.1) is a separate Maven module so you only depend on the versions you need.

---

## Project layout

| Module      | Purpose |
|------------|---------|
| **es-common** | Shared interfaces, config types, and version enum. Every version module depends on this. |
| **es-8-5**   | Elasticsearch 8.5 client. |
| **es-8-13**  | Elasticsearch 8.13 client. |
| **es-9-1**   | Elasticsearch 9.1 client. |

---

## Setup

**Requirements:** Java 17, Maven.

Build and install all modules (so other projects can use them):

```bash
cd es-client
mvn clean install
```

To use in your app, add the version(s) you need. Example for 8.5 and 8.13:

```xml
<dependency>
    <groupId>com.meesho</groupId>
    <artifactId>es-8-5</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>com.meesho</groupId>
    <artifactId>es-8-13</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

---

## Connection config (what you must provide)

The library only needs four things per connection. Everything else uses Elasticsearch client defaults.

| Setting         | Meaning |
|----------------|--------|
| **hosts**      | List of `host:port` (e.g. `localhost:9200` or `es1.example.com:9200,es2.example.com:9200`). |
| **username**   | Optional. If set, basic auth is used. |
| **password**   | Optional. Used with username. |
| **socketTimeout** | Optional (milliseconds). How long to wait for a response. |

These are passed as `EsConnectionProperties` (from `es-common`).

---

## How to use it in your app

1. **Create and register repository per version at startup**  
   Build the version repository (`EsV8Repository`, `EsV813Repository`, `EsV91Repository`) and register it in `EsRepositoryFactory`.

2. **Create the ES client from repository**  
   For each enabled version, call `repository.createClient(connectionProperties)` and store the returned `EsClient` in your host service map.

3. **Use repository + client together**  
   For each request, pick the repository from `EsRepositoryFactory.getRepository(version)` and pass the pre-created client through `EsClientInfo`.

The **es-host-service** project is a small Spring Boot app that shows this full flow: config per version, registration, and search/index APIs.

---

## Run tests

```bash
mvn test
```

Run tests for a single module:

```bash
mvn test -pl es-common
```
