package com.grd.gradingbe.service.impl;

import com.grd.gradingbe.dto.request.CreatePostRequest;
import com.grd.gradingbe.dto.request.UpdatePostRequest;
import com.grd.gradingbe.dto.response.PageResponse;
import com.grd.gradingbe.dto.response.PostResponse;
import com.grd.gradingbe.exception.ResourceNotFoundException;
import com.grd.gradingbe.model.ForumChannel;
import com.grd.gradingbe.model.ForumPost;
import com.grd.gradingbe.model.User;
import com.grd.gradingbe.repository.ForumChannelRepository;
import com.grd.gradingbe.repository.ForumCommentRepository;
import com.grd.gradingbe.repository.ForumPostRepository;
import com.grd.gradingbe.repository.UserRepository;
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

    public ForumPostServiceImpl(ForumPostRepository forumPostRepository, ForumCommentRepository forumCommentRepository, ForumChannelRepository forumChannelRepository, UserRepository userRepository) {
        this.forumPostRepository = forumPostRepository;
        this.forumCommentRepository = forumCommentRepository;
        this.forumChannelRepository = forumChannelRepository;
        this.userRepository = userRepository;
    }

    public PageResponse<PostResponse> getPosts(int page, int size, String sortBy, String sortDir, String search)
    {
        Sort sorter = sortBy.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sorter);
        Page<ForumPost> postPages = forumPostRepository.findAll(pageable);
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

    public PostResponse createPost(Integer userId, CreatePostRequest request)
    {
        ForumChannel channel = forumChannelRepository.findById(request.getChannel_id())
                .orElseThrow(() -> new ResourceNotFoundException("Forum channel", "Id", request.getChannel_id().toString()));

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

        return responseMapping(forumPostRepository.save(post));
    }

    public PostResponse updatePost(Integer userId, Long id, UpdatePostRequest request)
    {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));

        ForumPost post = forumPostRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", id.toString()));

        if (!post.getCreator().equals(user))
        {
            throw new IllegalArgumentException("Post's creator mismatch");
        }

        Optional.ofNullable(request.getTitle()).ifPresent(post::setTitle);
        Optional.ofNullable(request.getContent()).ifPresent(post::setContent);
        Optional.ofNullable(request.getIs_locked()).ifPresent(post::setIs_locked);
        Optional.ofNullable(request.getIs_pinned()).ifPresent(post::setIs_pinned);
        Optional.ofNullable(request.getLike_count()).ifPresent(post::setLike_count);

        return responseMapping(forumPostRepository.save(post));
    }

    public void deletePost(Integer userId, Long id)
    {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));

        ForumPost post = forumPostRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", id.toString()));

        if (!post.getCreator().equals(user))
        {
            throw new IllegalArgumentException("Post's creator mismatch");
        }

        forumPostRepository.deleteById(id);
    }

    public void likePost(Long id)
    {
        ForumPost post = forumPostRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "Id", id.toString()));

        post.setLike_count(post.getLike_count()+1);

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
        long commentCount = forumCommentRepository.countByForumPost(forumPost);

        return PostResponse.builder()
                .id(forumPost.getId())
                .channel_id(forumPost.getForumChannel().getId())
                .creator_id(forumPost.getCreator().getId())
                .title(forumPost.getTitle())
                .content(forumPost.getContent())
                .is_pinned(forumPost.getIs_pinned())
                .is_locked(forumPost.getIs_locked())
                .like_count(forumPost.getLike_count())
                .comment_count(commentCount)
                .createdAt(String.valueOf(forumPost.getCreatedAt()))
                .createdBy(forumPost.getCreatedBy())
                .updatedAt(String.valueOf(forumPost.getUpdatedAt()))
                .updatedBy(forumPost.getUpdatedBy())
                .build();
    }
}
