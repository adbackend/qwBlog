package com.ex.qwblog.controller;

import com.ex.qwblog.domain.Post;
import com.ex.qwblog.domain.PostEditor;
import com.ex.qwblog.repository.PostRepository;
import com.ex.qwblog.request.PostCreate;
import com.ex.qwblog.request.PostEdit;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean(){
        postRepository.deleteAll();
    }

//    @Test
//    @DisplayName("/posts 요청시 Hello world 출력한다")
//    void test() throws Exception {
//
//        // given
//        PostCreate request = PostCreate.builder()
//                .title("제목입니다.")
//                .content("내용입니다.")
//                .build();
//
//
//        String jsonContent = objectMapper.writeValueAsString(request);
//
//        // expectd
//        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonContent))
//                .andExpect(status().isOk())
//                .andExpect(content().string("{\"postId\":1}"))
//                .andDo(MockMvcResultHandlers.print());
//
//    }

//    @Test
//    @DisplayName("/posts 요청시 title값은 필수다")
//    void test2() throws Exception{
//
//        // given
//        PostCreate request = PostCreate.builder()
//                .title(null)
//                .content("내용입니다.")
//                .build();
//
//        String jsonContent = objectMapper.writeValueAsString(request);
//
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(jsonContent))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.code").value("400"))
//                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
//                .andExpect(jsonPath("$.validation.title").value("제목을 입력해주세요."))
//                .andDo(MockMvcResultHandlers.print());
//    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다.")
    void test3() throws Exception {

        Map<String, String> maps = new HashMap<>();
        maps.put("title", "제목 입니다.");
        maps.put("content", "내용 입니다.");

        ObjectMapper objectMapper = new ObjectMapper();
        String value = objectMapper.writeValueAsString(maps);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(value))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // then
        Assertions.assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        Assertions.assertEquals("제목 입니다.", post.getTitle());
        Assertions.assertEquals("내용 입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {

        // given
        Post post = Post.builder()
                .title("12345")
                .content("바오")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", post.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("12345"))
                .andExpect(jsonPath("$.content").value("바오"))
                .andDo(MockMvcResultHandlers.print());
    }

//    @Test
//    @DisplayName("글 목록 조회")
//    void test5() throws Exception{
//
//        // given
//        Post requestPost1 = Post.builder()
//                .title("title_test1")
//                .content("content_test1")
//                .build();
//
//        Post requestPost2 = Post.builder()
//                .title("title_test2")
//                .content("content_test2")
//                .build();
//
//        postRepository.save(requestPost1);
//        postRepository.save(requestPost2);
//
//        // expected
//        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()", Matchers.is(2)))
//                .andExpect(jsonPath("$[0].id").value(requestPost1.getId()))
//                .andExpect(jsonPath("$[0].title").value("title_test1"))
//                .andExpect(jsonPath("$[0].content").value("content_test1"))
//                .andExpect(jsonPath("$[1].id").value(requestPost2.getId()))
//                .andExpect(jsonPath("$[1].title").value("title_test2"))
//                .andExpect(jsonPath("$[1].content").value("content_test2"))
//                .andDo(MockMvcResultHandlers.print());
//    }

    @Test
    @DisplayName("글 목록 조회")
    void test55() throws Exception{

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

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=1&size=10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("$.[0].title").value("제목 - 19"))
                .andExpect(jsonPath("$.[0].content").value("내용 - 19"))
                .andDo(MockMvcResultHandlers.print());
    }

//    @Test
//    @DisplayName("페이지를 0으로 요청하면 첫페이지를 가져온다.")
//    void test6() throws Exception{
//
//        // given
//        List<Post> requestPosts = IntStream.range(0, 20)
//                .mapToObj(i->{
//                    return Post.builder()
//                            .title("제목 - " + i)
//                            .content("내용 - " + i)
//                            .build();
//                })
//                .collect(Collectors.toList());
//
//        postRepository.saveAll(requestPosts);
//
//        // expected
//        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=0&size=10")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()", Matchers.is(10)))
//                .andExpect(jsonPath("$.[0].title").value("제목 - 19"))
//                .andExpect(jsonPath("$.[0].content").value("내용 - 19"))
//                .andDo(MockMvcResultHandlers.print());
//    }

//    @Test
//    @DisplayName("글 제목 수정")
//    void test7() throws Exception{
//
//        // given
//        Post post = Post.builder()
//                .title("햄버거")
//                .content("햄버거 먹을래")
//                .build();
//
//        postRepository.save(post);
//
//        // when
//        PostEditor postEditor = PostEditor.builder()
//                .title("햄버거 안먹어")
//                .content("햄버거 먹을래")
//                .build();
//
//        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", post.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(postEditor)))
//                .andExpect(status().isOk())
//                .andDo(MockMvcResultHandlers.print());
//
//    }

    @Test
    @DisplayName("글 삭제")
    void test8() throws Exception {

        // given
        Post post = Post.builder()
                .title("햄버거")
                .content("햄버거 먹을래")
                .build();

        postRepository.save(post);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}", post.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test9() throws Exception{

        // expected
        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/{postId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void test10() throws Exception{

        // given
        PostEdit postEdit = PostEdit.builder()
                .title("햄버거")
                .content("햄버거 먹을래")
                .build();


        // expected
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

    }

//    @Test
//    @DisplayName("/posts 요청시 title값은 필수다")
//    void test11() throws Exception{
//
//        // given
//        PostCreate request = PostCreate.builder()
//                .title(null)
//                .content("내용입니다.")
//                .build();
//
//        String jsonContent = objectMapper.writeValueAsString(request);
//
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonContent))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.code").value("400"))
//                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
//                .andExpect(jsonPath("$.validation.title").value("제목을 입력해주세요."))
//                .andDo(MockMvcResultHandlers.print());
//    }

    @Test
    @DisplayName("게시글 작성시 제목에 '바보'가 포함될수 없습니다.")
    void test12() throws Exception{

        // given
        PostCreate request = PostCreate.builder()
                .title("바보야")
                .content("강아지")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }





}