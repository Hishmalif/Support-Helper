package org.hishmalif.supporthelperbot.repository;

import org.hishmalif.supporthelperbot.data.HttpRequest;

import java.net.URI;

public interface HttpClient {
    String getResponse(HttpRequest httpRequest);
    URI getUri(HttpRequest httpRequest);
}