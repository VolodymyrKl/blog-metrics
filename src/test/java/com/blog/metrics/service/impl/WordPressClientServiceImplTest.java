package com.blog.metrics.service.impl;

import com.blog.metrics.model.Post;
import com.blog.metrics.service.WordPressClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

class WordPressClientServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    private WordPressClientService wordPressClientService;

    @Value("${wordpress.api.url}")
    private String API_URL;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        wordPressClientService = new WordPressClientServiceImpl(restTemplateBuilder, API_URL);
    }

    @Test
    void shouldReturnPostByFetchingPosts() {
        Post[] posts = new Post[]{new Post(), new Post()};
        ResponseEntity<Post[]> response = ResponseEntity.ok(posts);
        when(restTemplate.getForEntity(
                eq(API_URL + "wp-json/wp/v2/posts"),
                eq(Post[].class)
        )).thenReturn(response);

        Set<Post> result = wordPressClientService.fetchRecentPosts();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnEmptyByFetchingPostsWhenUrlIsNotProvided() {
        Post[] emptyPosts = new Post[]{};
        ResponseEntity<Post[]> response = ResponseEntity.ok(emptyPosts);
        when(restTemplate.getForEntity(
                anyString(),
                eq(Post[].class)
        )).thenReturn(response);

        Set<Post> result = wordPressClientService.fetchRecentPosts();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}