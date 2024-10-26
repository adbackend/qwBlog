package com.ex.qwblog.service;

import com.ex.qwblog.domain.Post;
import com.ex.qwblog.exception.PostNotFound;
import com.ex.qwblog.repository.PostRepository;
import com.ex.qwblog.request.PostCreate;
import com.ex.qwblog.request.PostEdit;
import com.ex.qwblog.request.PostSearch;
import com.ex.qwblog.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean(){
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1(){

        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목 입니다.")
                .content("내용 입니다.")
                .build();

        // when
        postService.write(postCreate);

        // then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);

        assertEquals("제목 입니다.", post.getTitle());
        assertEquals("내용 입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2(){

        // given
        Post requestPost = Post.builder()
                .title("푸") 
                .content("바오")
                .build();

        postRepository.save(requestPost);

        // when
        PostResponse response = postService.get(requestPost.getId());


        assertNotNull(response);
        assertEquals(1L, postRepository.count());
        assertEquals("푸", response.getTitle());
        assertEquals("바오", response.getContent());
    }

    @Test
    @DisplayName("글 목록 조회")
    void test3(){

        // given
        Post requestPost1 = Post.builder()
                .title("푸")
                .content("바오")
                .build();

        Post requestPost2 = Post.builder()
                .title("햄")
                .content("버거")
                .build();

        postRepository.saveAll(List.of(requestPost1, requestPost2));

        // when
        List<PostResponse> posts = postService.getList();


        // then
        assertEquals(2L, posts.size());
        assertEquals(2L, postRepository.count());

    }

    @Test
    @DisplayName("글 첫 1페이지 조회")
    void test33(){

        // given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i->{
                    return Post.builder()
                            .title("제목 - " + i)
                            .content("내용 - " + i)
                            .build();
                })
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // when
        Pageable pageable = PageRequest.of(0,5, Sort.by(Sort.Direction.DESC, "id"));
        List<PostResponse> posts = postService.getList(pageable);

        // then
        Assertions.assertEquals(5L, posts.size());
        Assertions.assertEquals("제목 - 30", posts.get(0).getTitle());
    }

    @Test
    @DisplayName("글 첫 1페이지 조회")
    void test4(){

        // given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i->{
                    return Post.builder()
                            .title("제목 - " + i)
                            .content("내용 - " + i)
                            .build();
                })
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // when
        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();

        List<PostResponse> posts = postService.getList3(postSearch);

        // then
        Assertions.assertEquals(10L, posts.size());
        Assertions.assertEquals("제목 - 19", posts.get(0).getTitle());
    }

    @Test
    @DisplayName("글제목 수정")
    void test5(){

        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("내용수정")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post chagedPost = postRepository.findById(post.getId())
                .orElseThrow(()-> new IllegalArgumentException("글이 존재하지 않습니다. id = " + post.getId()));

        Assertions.assertEquals("제목", chagedPost.getTitle());
        Assertions.assertEquals("내용수정", chagedPost.getContent());

    }

    @Test
    @DisplayName("게시글 삭제")
    void test6(){

        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        Assertions.assertEquals(0, postRepository.count());

    }


    @Test
    @DisplayName("글 1개 조회 - 예외처리")
    void test7(){

        // given
        Post post = Post.builder()
                .title("푸")
                .content("바오")
                .build();

        postRepository.save(post);

        // expected
        Assertions.assertThrows(PostNotFound.class, () -> {
            postService.get(post.getId()+1L);
        });

    }

    @Test
    @DisplayName("게시글 삭제 - 예외처리")
    void test8(){

        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(post);

        // expected
        Assertions.assertThrows(PostNotFound.class, () -> {
            postService.delete(post.getId()+1L);
        });

    }

    @Test
    @DisplayName("글제목 수정 - 예외처리")
    void test9(){

        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("내용수정")
                .build();


        // expected
        Assertions.assertThrows(PostNotFound.class, () -> {
            postService.edit(post.getId()+1L, postEdit);
        });

    }

}