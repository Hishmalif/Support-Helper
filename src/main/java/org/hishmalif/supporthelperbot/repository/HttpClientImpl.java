package org.hishmalif.supporthelperbot.repository;

import org.springframework.stereotype.Repository;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.reactive.function.client.WebClient;
import org.hishmalif.supporthelperbot.data.HttpRequest;

import java.net.URI;

@Repository
public class HttpClientImpl implements HttpClient {
    private final WebClient webClient;

    public HttpClientImpl() {
        this.webClient = WebClient.builder().build();
    }

    @Override
    public String getResponse(HttpRequest httpRequest) {
        WebClient.RequestHeadersSpec<?> clientRequest;

        if (httpRequest.getMethod().equals(HttpMethod.GET)) {
            clientRequest = webClient.get().uri(getUri(httpRequest));
        } else {
            clientRequest = webClient.post()
                    .uri(getUri(httpRequest))
                    .bodyValue(httpRequest.getBody());
        }
        httpRequest.getHeaders().forEach(clientRequest::header);
        return clientRequest.retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public URI getUri(HttpRequest httpRequest) {
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(httpRequest.getUrl());
        httpRequest.getParams().forEach(uri::queryParam);
        return uri.build(true).toUri();
    }
}