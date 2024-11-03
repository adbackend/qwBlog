package com.ex.qwblog.controller;

import com.ex.qwblog.exception.InvalidRequest;
import com.ex.qwblog.request.PostCreate;
import com.ex.qwblog.request.PostEdit;
import com.ex.qwblog.request.PostSearch;
import com.ex.qwblog.response.PostResponse;
import com.ex.qwblog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {

        log.info("prams = {} ", request.toString());

        request.validate();

        postService.write(request);
    }


    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long id){

        PostResponse response = postService.get(id);

        return response;
    }

//    @GetMapping("/posts")
//    public List<PostResponse> getList(){
//
//        return postService.getList();
//    }

    // 페이징 처리
    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch){

        return postService.getList3(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable(name = "postId") Long postId, @RequestBody @Valid PostEdit request){
        postService.edit(postId, request);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable(name = "postId") Long postId){
        postService.delete(postId);
    }
}