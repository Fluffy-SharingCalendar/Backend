package com.fluffy.SharingCalendar.service;

import com.fluffy.SharingCalendar.domain.Post;
import com.fluffy.SharingCalendar.domain.User;
import com.fluffy.SharingCalendar.dto.request.ModifyPostRequestDto;
import com.fluffy.SharingCalendar.dto.request.RegisterPostRequestDto;
import com.fluffy.SharingCalendar.dto.response.PagedPostResponse;
import com.fluffy.SharingCalendar.exception.CustomException;
import com.fluffy.SharingCalendar.repository.EventRepository;
import com.fluffy.SharingCalendar.repository.PostImageRepository;
import com.fluffy.SharingCalendar.repository.PostQDslRepository;
import com.fluffy.SharingCalendar.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.IntStream;

import static com.fluffy.SharingCalendar.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostQDslRepository postQDslRepository;
    private final EventRepository eventRepository;
    private final PostImageRepository postImageRepository;
    private final S3Service s3Service;
    private final UserService userService;

    /*
    이미지 분리 게시글 등록
     */
//    @Transactional
//    public void register(int eventId, RegisterPostRequestDto request, String nickname) {
//        checkEventId(eventId);
//
//        User user = userService.findByNickname(nickname);
//
//        Post post = request.toEntity(user, eventId);
//
//        postRepository.save(post);
//
//        updateImageByPostId(request.getImageIds(), post.getId());
//    }

    /*
    이미지 동시 게시글 등록
     */
    @Transactional
    public void register(int eventId, RegisterPostRequestDto request, MultipartFile[] files, String nickname) {
        checkEventId(eventId);

        User user = userService.findByNickname(nickname);
        Post post = request.toEntity(user, eventId);
        postRepository.save(post);

        if (files != null) {
            IntStream.range(0, files.length).forEach(i -> {
                s3Service.upload(files[i], post.getId(), i + 1);
            });
        }
    }

    @Transactional(readOnly = true)
    public PagedPostResponse readPostList(int eventId, Pageable page) {
        return new PagedPostResponse(postQDslRepository.findPostList(eventId, page));
    }

    /*
    이미지 분리 수정
     */
//    @Transactional
//    public void update(int postId, ModifyPostRequestDto request, String nickname) {
//        Post post = findByPostId(postId);
//        User user = userService.findByNickname(nickname);
//        validateAccess(post.getAuthor().getId(), user.getId());
//
//        post.update(request.getContent());
//
//        postRepository.save(post);
//
//        updateImageByPostId(request.getImageIds(), postId);
//
//    }

    /*
    이미지 동시 수정
     */
    @Transactional
    public void update(int postId, ModifyPostRequestDto request, MultipartFile[] files, String nickname) {
        Post post = findByPostId(postId);
        User user = userService.findByNickname(nickname);
        validateAccess(post.getAuthor().getId(), user.getId());

        post.update(request.getContent());
        postImageRepository.updateAllByPostId(postId);
        postRepository.save(post);

        if (files != null) {
            IntStream.range(0, files.length).forEach(i -> {
                s3Service.upload(files[i], postId, i + 1);
            });
        }
    }

    @Transactional
    public void delete(int postId, String nickname) {
        Post post = findByPostId(postId);
        User user = userService.findByNickname(nickname);
        validateAccess(post.getAuthor().getId(), user.getId());

        postImageRepository.updateAllByPostId(postId);

        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public Post findByPostId(int postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));
    }

    /*
    이미지 분리 수정
     */
//    @Transactional
//    public void updateImageByPostId(int[] imageIds, Integer postId) {
//        if (imageIds != null) {
//            IntStream.range(0, imageIds.length)
//                    .forEach(i -> postImageRepository.updateImageByPostId(postId, imageIds[i], i + 1));
//            return;
//        }
//        postImageRepository.updateAllByPostId(postId);
//    }

    private void validateAccess(long registerId, long userId) {
        if (registerId != userId) {
            throw new CustomException(NO_PERMISSION_FOR_MODIFICATION);
        }
    }

    private void checkEventId(int eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new CustomException(EVENT_NOT_FOUND);
        }
    }

}
