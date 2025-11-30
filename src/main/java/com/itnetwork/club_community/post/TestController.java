package com.itnetwork.club_community.post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {
    private final PostRepository postRepository;
    
    @PostMapping("/test/posts")
    public ResponseEntity<Post> createTestPost() {
        Post post = new Post();
        // 필수 필드 설정
        post.setMember_id(1);
        post.setCategory_id(1);
        post.setTitle("테스트 게시글");
        post.setContents("테스트 내용");
        post.setReg_id(1);
        
        Post savedPost = postRepository.save(post);
        return ResponseEntity.ok(savedPost);
    }

    @GetMapping("/test/posts")
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postRepository.findAll());
    }
}