package com.blog.metrics.service.impl;

import com.blog.metrics.model.Post;
import com.blog.metrics.model.ValueHelp;
import com.blog.metrics.service.WordPressClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WordCountProcessorServiceImplTest {

    @Mock
    private WordPressClientService wordPressClientService;

    private WordCountProcessorServiceImpl wordCountProcessor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        wordCountProcessor = new WordCountProcessorServiceImpl(wordPressClientService);
    }

    @Test
    void shouldCountWordsWhenProcessingPostsWithNewPosts() {
        Post post = new Post();
        post.setContent(new ValueHelp("Hello world Hello"));
        when(wordPressClientService.fetchRecentPosts()).thenReturn(Set.of(post));

        Map<String, Integer> result = wordCountProcessor.processPosts();

        assertEquals(2, result.get("Hello"));
        assertEquals(1, result.get("world"));
    }

    @Test
    void shouldReturnEmptyMapWhenProcessingPostsIsEmpty() {
        when(wordPressClientService.fetchRecentPosts()).thenReturn(Set.of());

        Map<String, Integer> result = wordCountProcessor.processPosts();

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldBeAbleToProceedWithMultiplePosts() {
        Post post1 = new Post();
        post1.setContent(new ValueHelp("first post"));
        Post post2 = new Post();
        post2.setContent(new ValueHelp("second post"));
        when(wordPressClientService.fetchRecentPosts())
                .thenReturn(Set.of(post1, post2));

        Map<String, Integer> firstResult = wordCountProcessor.processPosts();
        Map<String, Integer> secondResult = wordCountProcessor.processPosts();

        assertNotEquals(firstResult, secondResult);
        verify(wordPressClientService, times(1)).fetchRecentPosts();
    }
}