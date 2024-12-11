package com.blog.metrics.service;

import com.blog.metrics.model.Post;

import java.util.Set;

public interface WordPressClientService {
    Set<Post> fetchRecentPosts();
}
