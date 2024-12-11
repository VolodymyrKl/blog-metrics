package com.blog.metrics.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Post {
    private Long id;
    @JsonProperty("title")
    private ValueHelp title;
    @Setter
    @JsonProperty("content")
    private ValueHelp content;
    @Setter
    @JsonProperty("isProceeded")
    private boolean isProceeded;
}
