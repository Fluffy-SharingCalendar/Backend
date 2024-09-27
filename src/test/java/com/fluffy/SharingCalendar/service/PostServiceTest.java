package com.fluffy.SharingCalendar.service;

import com.fluffy.SharingCalendar.domain.Post;
import com.fluffy.SharingCalendar.dto.request.ModifyPostRequestDto;
import com.fluffy.SharingCalendar.dto.response.PostDetailResponseDto;
import com.fluffy.SharingCalendar.dto.request.RegisterPostRequestDto;
import com.fluffy.SharingCalendar.repository.PostImageRepository;
import com.fluffy.SharingCalendar.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostImageRepository postImageRepository;

    int[] imageIds = {21, 22, 23};
    RegisterPostRequestDto postRequest;
    int userId = 1;

    @BeforeEach
    void setup() {
        postRequest = RegisterPostRequestDto.builder()
                .eventId(1)
                .content("test")
                .imageIds(imageIds)
                .eventDate("2024.09.26")
                .build();
    }
    // 게시글 등록 테스트
    @Test
    void registerPost() {
        //given

        //when
        Post post = postRepository.save(postRequest.toEntity(userId));
        postService.updateImageByPostId(postRequest.getImageIds(),post.getId());

        //then
        assertEquals("test", post.getContent());
        IntStream.range(0,imageIds.length)
                .forEach(i -> assertEquals(postImageRepository.findById(imageIds[i]).get().getPostId()
                        ,post.getId()));
    }

    // 게시글 수정 테스트
    @Test
    void updatePost() {
        //given
        Post post = postRepository.save(postRequest.toEntity(userId));
        postService.updateImageByPostId(postRequest.getImageIds(),post.getId());


        //when
        int postId = post.getId();
        int[] imageIds = new int[] {};
        ModifyPostRequestDto updateRequest = ModifyPostRequestDto.builder()
                .content("test")
                .imageIds(imageIds)
                .build();

        postService.update(updateRequest, postId, userId);

        Optional<Post> updatedPost = postRepository.findById(postId);
        assertTrue(updatedPost.isPresent());
        IntStream.range(0,imageIds.length)
                .forEach(i -> assertNull(postImageRepository.findById(imageIds[i]).get().getPostId()));
    }

    // 게시글 삭제 테스트
    @Test
    void deletePost() {
        // given
        Post post = postRepository.save(postRequest.toEntity(userId));
        postService.updateImageByPostId(postRequest.getImageIds(),post.getId());
        int postId = post.getId();

        // when
        postService.delete(postId, userId);

        // then
        Optional<Post> deletedPost = postRepository.findById(post.getId());
        assertFalse(deletedPost.isPresent());
        IntStream.range(0,imageIds.length)
                .forEach(i -> assertNull(postImageRepository.findById(imageIds[i]).get().getPostId()));
    }

    // 게시글 목록 조회 테스트
    @Test
    void readPostList() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        for (int i = 1; i <= 5; i++) {
            RegisterPostRequestDto postRequest = RegisterPostRequestDto.builder()
                    .eventId(1)
                    .content("test" + i)
                    .imageIds(imageIds)
                    .eventDate("2024.09.2"+i)
                    .build();
            postService.register(postRequest, userId);
        }

        // when
        Page<PostDetailResponseDto> result = postService.readPostList(pageable);

        //then
        assertNotNull(result);
//        assertEquals(5, result.getTotalElements()); // 총 5개의 게시글이 등록되었는지 확인
        result.getContent().forEach(postDetailResponseDto -> {
            System.out.println("+++++++++"+postDetailResponseDto.toString());
            assertNotNull(postDetailResponseDto.getAuthorId());
            assertNotNull(postDetailResponseDto.getContent());
        });
    }
}
