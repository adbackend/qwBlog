package com.ex.qwblog.repository;

import com.ex.qwblog.domain.Post;
import com.ex.qwblog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
