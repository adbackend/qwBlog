package com.ex.qwblog.request;

import com.ex.qwblog.exception.InvalidRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@ToString
@Setter
@Getter
public class PostCreate {

    @NotBlank(message = "제목을 입력해주세요.")
    @JsonProperty("title")
    public final String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @JsonProperty("content")
    public final String content;

    @Builder
    public PostCreate(@JsonProperty("title") String title,
                      @JsonProperty("content") String content) {
        this.title = title;
        this.content = content;
    }

    public PostCreate changeTitle(String title){
        return PostCreate.builder()
                .title(title)
                .content(this.content)
                .build();
    }

    public void validate(){
        if(this.title.contains("바보")){
            throw new InvalidRequest("title", "제목에 '바보'를 포함 할 수 없습니다.");
        }
    }

    
}
