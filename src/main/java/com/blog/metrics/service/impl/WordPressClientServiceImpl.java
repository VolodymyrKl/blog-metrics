package com.blog.metrics.service.impl;

import com.blog.metrics.model.Post;
import com.blog.metrics.service.WordPressClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Service
public class WordPressClientServiceImpl implements WordPressClientService {
    private final RestTemplate restTemplate;
    private final String apiUrl;

    public WordPressClientServiceImpl(RestTemplateBuilder restTemplateBuilder,
                                      @Value("${wordpress.api.url}") String apiUrl) {
        this.restTemplate = restTemplateBuilder.build();
        this.apiUrl = apiUrl;
    }

    public Set<Post> fetchRecentPosts() {
        ResponseEntity<Post[]> response = restTemplate.getForEntity(
                apiUrl + "wp-json/wp/v2/posts",
                Post[].class
        );
        return Set.of(response.getBody());
    }
}