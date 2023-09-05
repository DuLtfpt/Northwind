package com.northwind.identity.ulti;

import java.util.Map;

public interface TokenExtractor {
    Map<String, String> extract(String subject);
}
