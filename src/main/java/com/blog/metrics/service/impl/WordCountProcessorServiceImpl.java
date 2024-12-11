package com.blog.metrics.service.impl;

import com.blog.metrics.model.Post;
import com.blog.metrics.service.WordCountProcessorService;
import com.blog.metrics.service.WordPressClientService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WordCountProcessorServiceImpl implements WordCountProcessorService {
    private final WordPressClientService wordPressClientService;
    private Set<Post> allPosts = new HashSet<>();

    public WordCountProcessorServiceImpl(WordPressClientService wordPressClientService) {
        this.wordPressClientService = wordPressClientService;
    }

    public Map<String, Integer> processPosts() {
        Map<String, Integer> wordCountByPost = new HashMap<>();
        Set<Post> postsToProcess = isAllPostsProceeded(allPosts)
                ? getNewPosts()
                : allPosts;

        postsToProcess.stream()
                .filter(post -> !post.isProceeded())
                .findFirst()
                .map(post -> {
                    post.setProceeded(true);
                    return post;
                })
                .map(this::processPostContent)
                .ifPresent(words -> {
                    countWords(words, wordCountByPost);
                });

        return wordCountByPost;
    }

    private Set<Post> getNewPosts() {
        allPosts.addAll(wordPressClientService.fetchRecentPosts());
        return allPosts;
    }

    private String[] processPostContent(Post post) {
        return post.getContent().getRendered()
                .replaceAll("[^a-zA-Z\\s]", "")
                .split("\\s+");
    }

    private void countWords(String[] words, Map<String, Integer> wordCount) {
        Arrays.stream(words)
                .forEach(word -> wordCount.merge(word, 1, Integer::sum));
    }

    private boolean isAllPostsProceeded(Set<Post> posts) {
        return posts.isEmpty() || posts.stream()
                .allMatch(Post::isProceeded);
    }
}