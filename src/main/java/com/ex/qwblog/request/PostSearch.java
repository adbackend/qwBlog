package com.ex.qwblog.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class PostSearch {

    private static final int MAX_PAGE_SIZE = 2000;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 10;

    public long getOffset(){
        return (long)(Math.max(1, page)-1)*Math.min(size, MAX_PAGE_SIZE);
    }

}
