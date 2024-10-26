package com.ex.qwblog.service;

import com.ex.qwblog.domain.Post;
import com.ex.qwblog.domain.PostEditor;
import com.ex.qwblog.exception.PostNotFound;
import com.ex.qwblog.repository.PostRepository;
import com.ex.qwblog.request.PostCreate;
import com.ex.qwblog.request.PostEdit;
import com.ex.qwblog.request.PostSearch;
import com.ex.qwblog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    public Long write(PostCreate postCreate){

        Post post = Post.builder()
                        .title(postCreate.getTitle())
                        .content(postCreate.getContent())
                        .build();

        Post result = postRepository.save(post);

        return result.getId();
    }

    public PostResponse get(Long id){

        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        PostResponse response = PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        return response;
    }



    // 페이징 처리
    // 글이 너무 많은 경우 -> 비용, 리소스가 많이 든다
    // 글이 -> 100,000,000 -> DB글 모두 조회하는 경우 -> DB가 뻗을 수 있다
    // DB -> 애플리케이션 서버로 전달하는 시간, 트래픽비용 등이 많이 발생
    public List<PostResponse> getList(){
        return postRepository.findAll().stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    // 페이징 처리
    public List<PostResponse> getList(Pageable pageable){

        return postRepository.findAll(pageable).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getList3(PostSearch postSearch){

        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long id, PostEdit postEdit){

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFound());

        PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();

        PostEditor postEditor = postEditorBuilder.title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);
    }

    public void delete(Long id){

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFound());

        postRepository.delete(post);
    }
}
