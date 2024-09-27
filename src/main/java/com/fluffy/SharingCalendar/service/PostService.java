package com.fluffy.SharingCalendar.service;

import com.fluffy.SharingCalendar.domain.Post;
import com.fluffy.SharingCalendar.dto.PostDetailResponseDto;
import com.fluffy.SharingCalendar.dto.PostRequest;
import com.fluffy.SharingCalendar.exception.CustomException;
import com.fluffy.SharingCalendar.repository.EventRepository;
import com.fluffy.SharingCalendar.repository.PostImageRepository;
import com.fluffy.SharingCalendar.repository.PostQDslRepository;
import com.fluffy.SharingCalendar.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

import static com.fluffy.SharingCalendar.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostQDslRepository postQDslRepository;
    private final EventRepository eventRepository;
    private final PostImageRepository postImageRepository;

    @Transactional
    public Page<PostDetailResponseDto> readPostList(Pageable page) {
        return postQDslRepository.findPostList(page);
    }

    @Transactional
    public void register(PostRequest request, int userId) {
        checkEventId(request.getEventId());

        Post post = request.toEntity(userId);

        postRepository.save(post);

        updateImageByPostId(request.getImageIds(), post.getId());
    }


    @Transactional
    public void update(PostRequest request, int postId, int userId) {
        checkEventId(request.getEventId());

        Post post = findByPostId(postId);

        validateAccess(post.getAuthorId(), userId);

        post.update(request.getContent());

        postRepository.save(post);

        updateImageByPostId(request.getImageIds(), postId);

    }

    @Transactional
    public void delete(int postId, int userId) {
        Post post = findByPostId(postId);
        validateAccess(post.getAuthorId(), userId);
        postImageRepository.updateAllByPostId(postId);
        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public Post findByPostId(int postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));
    }

    @Transactional
    public void updateImageByPostId(int[] imageIds, Integer postId) {
        if (imageIds != null) {
            IntStream.range(0, imageIds.length)
                    .forEach(i -> postImageRepository.updateImageByPostId(postId, imageIds[i], i + 1));
            return;
        }
        postImageRepository.updateAllByPostId(postId);
    }

    private void validateAccess(int registerId, int userId) {
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
