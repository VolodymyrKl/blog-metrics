package com.blog.metrics.controller;

import com.blog.metrics.service.impl.WordCountProcessorServiceImpl;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class BlogController {
    private final WordCountProcessorServiceImpl wordCountProcessorService;

    public BlogController(WordCountProcessorServiceImpl wordCountProcessorService) {
        this.wordCountProcessorService = wordCountProcessorService;
    }

    @Scheduled(fixedDelay = 5000)
    @MessageMapping("/check-updates")
    @SendTo("/api/word-count")
    public Map<String, Integer> checkUpdates() {
        return wordCountProcessorService.processPosts();
    }
}
