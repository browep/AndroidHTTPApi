package com.github.browep.httpapi;

import org.apache.http.HttpRequest;

public interface ApiAuthenticator {
    public void authenticate(HttpRequest httpMessage);

}
