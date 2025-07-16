package com.grd.gradingbe.service.impl;

import com.grd.gradingbe.dto.request.ForumMediaRequest;
import com.grd.gradingbe.dto.request.ForumPostRequest;
import com.grd.gradingbe.dto.response.PageResponse;
import com.grd.gradingbe.dto.response.PostResponse;
import com.grd.gradingbe.exception.ResourceNotFoundException;
import com.grd.gradingbe.model.ForumChannel;
import com.grd.gradingbe.model.ForumMedia;
import com.grd.gradingbe.model.ForumPost;
import com.grd.gradingbe.model.User;
import com.grd.gradingbe.repository.*;
import com.grd.gradingbe.service.ForumMediaService;
import com.grd.gradingbe.service.ForumPostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ForumPostServiceImpl implements ForumPostService
{
    private final ForumPostRepository forumPostRepository;
    private final ForumCommentRepository forumCommentRepository;
    private final ForumChannelRepository forumChannelRepository;
    private final UserRepository userRepository;
    private final ForumMediaRepository forumMediaRepository;
    private final ForumMediaService forumMediaService;

    public ForumPostServiceImpl(ForumPostRepository forumPostRepository, ForumCommentRepository forumCommentRepository, ForumChannelRepository forumChannelRepository, UserRepository userRepository, ForumMediaRepository forumMediaRepository, ForumMediaService forumMediaService) {
        this.forumPostRepository = forumPostRepository;
        this.forumCommentRepository = forumCommentRepository;
        this.forumChannelRepository = forumChannelRepository;
        this.userRepository = userRepository;
        this.forumMediaRepository = forumMediaRepository;
        this.forumMediaService = forumMediaService;
    }

    public PageResponse<PostResponse> getPosts(int page, int size, String sortBy, String sortDir, String search)
    {
        Sort sorter = sortBy.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sorter);
        Page<ForumPost> postPages = forumPostRepository.findAllByName(pageable, search);
        List<ForumPost> postContents = postPages.getContent();

        return PageResponse.<PostResponse>builder()
                .content(responseMapping(postContents))
                .page(postPages.getNumber())
                .size(postPages.getSize())
                .totalElements((int) postPages.getTotalElements())
                .totalPages(postPages.getTotalPages())
                .last(postPages.isLast())
                .build();
    }

    public PostResponse getPost(Long id)
    {
        ForumPost post = forumPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Forum post", "Id", id.toString()));

        return responseMapping(post);
    }

    public PostResponse createPost(Integer userId, ForumPostRequest request)
    {
        ForumChannel channel = forumChannelRepository.findById(request.getChannelId())
                .orElseThrow(() -> new ResourceNotFoundException("Forum channel", "Id", request.getChannelId().toString()));

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));

        ForumPost post = ForumPost.builder()
                .forumChannel(channel)
                .creator(user)
                .title(request.getTitle())
                .content(request.getContent())
                .is_pinned(false)
                .is_locked(false)
                .like_count(0L)
                .build();

        ForumPost savedPost = forumPostRepository.save(post);

        if (request.getMediaList() != null)
        {
            for (ForumMediaRequest mediaReq : request.getMediaList()) {
                ForumMedia forumMedia = ForumMedia.builder()
                        .url(mediaReq.getUrl())
                        .type(mediaReq.getMediaType())
                        .forumPost(savedPost)
                        .build();

                forumMediaRepository.save(forumMedia);
            }
        }

        return responseMapping(savedPost);
    }

    public PostResponse updatePost(Integer userId, Long id, ForumPostRequest request)
    {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));

        ForumPost post = forumPostRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", id.toString()));

        if (!post.getCreator().getId().equals(user.getId()))
        {
            throw new IllegalArgumentException("Post's creator mismatch");
        }

        forumMediaService.updatePostMedia(post, request.getMediaList());

        Optional.ofNullable(request.getTitle()).ifPresent(post::setTitle);
        Optional.ofNullable(request.getContent()).ifPresent(post::setContent);
        Optional.ofNullable(request.getIsLocked()).ifPresent(post::setIs_locked);
        Optional.ofNullable(request.getIsPinned()).ifPresent(post::setIs_pinned);
        Optional.ofNullable(request.getLikeCount()).ifPresent(post::setLike_count);

        return responseMapping(forumPostRepository.save(post));
    }

    public void deletePost(Integer userId, Long id)
    {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));

        ForumPost post = forumPostRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", id.toString()));

        if (!post.getCreator().getId().equals(user.getId()))
        {
            throw new IllegalArgumentException("Post's creator mismatch");
        }

        forumPostRepository.deleteById(id);
    }

    public void deletePost(Long id)
    {
        forumPostRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", id.toString()));

        forumPostRepository.deleteById(id);
    }

    public void likePost(Long id, int like)
    {
        ForumPost post = forumPostRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", id.toString()));

        post.setLike_count(post.getLike_count() + like);

        forumPostRepository.save(post);
    }

    private List<PostResponse> responseMapping(List<ForumPost> postContents)
    {
        return postContents.stream()
                .map(this::responseMapping)
                .toList();
    }

    private PostResponse responseMapping(ForumPost forumPost)
    {
        Long commentCount = forumCommentRepository.countByForumPost(forumPost);

        List<ForumMedia> mediaList = forumMediaRepository.findAllByForumPostId(forumPost.getId());

        return PostResponse.builder()
                .id(forumPost.getId())
                .channelId(forumPost.getForumChannel().getId())
                .creatorId(forumPost.getCreator().getId())
                .title(forumPost.getTitle())
                .content(forumPost.getContent())
                .mediaList(forumMediaService.mediaResponseMapping(mediaList))
                .isPinned(forumPost.getIs_pinned())
                .isLocked(forumPost.getIs_locked())
                .likeCount(forumPost.getLike_count())
                .commentCount(commentCount)
                .createdAt(String.valueOf(forumPost.getCreatedAt()))
                .createdBy(forumPost.getCreatedBy())
                .updatedAt(String.valueOf(forumPost.getUpdatedAt()))
                .updatedBy(forumPost.getUpdatedBy())
                .build();
    }
}
