package com.ex.qwblog.response;

import com.ex.qwblog.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;

    @Builder
    public PostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0, Math.min(title.length(), 10));
        this.content = content;
    }

    public PostResponse(Post post){

        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}


