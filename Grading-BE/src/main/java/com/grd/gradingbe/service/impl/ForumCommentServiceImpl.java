package com.grd.gradingbe.service.impl;

import com.grd.gradingbe.dto.request.ForumCommentRequest;
import com.grd.gradingbe.dto.request.ForumMediaRequest;
import com.grd.gradingbe.dto.response.CommentResponse;
import com.grd.gradingbe.dto.response.PageResponse;
import com.grd.gradingbe.exception.ResourceNotFoundException;
import com.grd.gradingbe.model.ForumComment;
import com.grd.gradingbe.model.ForumMedia;
import com.grd.gradingbe.model.ForumPost;
import com.grd.gradingbe.model.User;
import com.grd.gradingbe.repository.ForumCommentRepository;
import com.grd.gradingbe.repository.ForumMediaRepository;
import com.grd.gradingbe.repository.ForumPostRepository;
import com.grd.gradingbe.repository.UserRepository;
import com.grd.gradingbe.service.ForumCommentService;
import com.grd.gradingbe.service.ForumMediaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ForumCommentServiceImpl implements ForumCommentService
{
    private final ForumCommentRepository forumCommentRepository;
    private final UserRepository userRepository;
    private final ForumPostRepository forumPostRepository;
    private final ForumMediaRepository forumMediaRepository;
    private final ForumMediaService forumMediaService;

    public ForumCommentServiceImpl(ForumCommentRepository forumCommentRepository, UserRepository userRepository, ForumPostRepository forumPostRepository, ForumMediaRepository forumMediaRepository, ForumMediaService forumMediaService) {
        this.forumCommentRepository = forumCommentRepository;
        this.userRepository = userRepository;
        this.forumPostRepository = forumPostRepository;
        this.forumMediaRepository = forumMediaRepository;
        this.forumMediaService = forumMediaService;
    }

    public PageResponse<CommentResponse> getComments(int page, int size, String sortBy, String sortDir, String search)
    {
        Sort sorter = sortBy.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sorter);
        Page<ForumComment> commentPages = forumCommentRepository.findAllRootComment(pageable);
        List<ForumComment> commentContents = commentPages.getContent();

        return PageResponse.<CommentResponse>builder()
                .content(responseMapping(commentContents))
                .page(commentPages.getNumber())
                .size(commentPages.getSize())
                .totalElements((int) commentPages.getTotalElements())
                .totalPages(commentPages.getTotalPages())
                .last(commentPages.isLast())
                .build();
    }


    public CommentResponse createComment(Integer userId, ForumCommentRequest request)
    {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));

        ForumPost post = forumPostRepository.findById(request.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post", "Id", request.getPostId().toString()));

        Long parentId = request.getParentId();
        if (parentId == null)
        {
            parentId = 0L;
        }

        ForumComment parentComment = forumCommentRepository.findById(parentId)
                .orElse(null);

        ForumComment comment = ForumComment.builder()
                .content(request.getContent())
                .like_count(0L)
                .forumPost(post)
                .creator(user)
                .parentComment(parentComment)
                .build();

        ForumComment savedComment = forumCommentRepository.save(comment);

        if (request.getMediaList() != null)
        {
            for (ForumMediaRequest mediaReq : request.getMediaList()) {
                ForumMedia forumMedia = ForumMedia.builder()
                        .url(mediaReq.getUrl())
                        .type(mediaReq.getMediaType())
                        .forumComment(savedComment)
                        .build();

                forumMediaRepository.save(forumMedia);
            }
        }

        return responseMapping(savedComment);
    }

    public void deleteComment(Integer userId, Long id)
    {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));

        ForumComment comment = forumCommentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", id.toString()));

        if (!comment.getCreator().getId().equals(user.getId()))
        {
            throw new IllegalArgumentException("Comment's creator mismatch");
        }

        forumCommentRepository.deleteById(id);
    }

    public void likeComment(Long id, int like)
    {
        ForumComment comment = forumCommentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", id.toString()));

        comment.setLike_count(comment.getLike_count() + like);

        forumCommentRepository.save(comment);
    }

    public CommentResponse updateComment(Integer userId, Long id, ForumCommentRequest request)
    {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));

        ForumComment comment = forumCommentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", id.toString()));

        if (!comment.getCreator().getId().equals(user.getId()))
        {
            throw new IllegalArgumentException("Comment's creator mismatch");
        }

        forumMediaService.updateCommentMedia(comment, request.getMediaList());

        Optional.ofNullable(request.getContent()).ifPresent(comment::setContent);

        return responseMapping(forumCommentRepository.save(comment));
    }

    private List<CommentResponse> responseMapping(List<ForumComment> commentContent)
    {
        return commentContent.stream()
                .map(this::responseMapping)
                .toList();
    }

    private CommentResponse responseMapping(ForumComment forumComment)
    {
        List<ForumComment> replies = forumCommentRepository.findByParentComment(forumComment);

        List<ForumMedia> mediaList = forumMediaRepository.findAllByForumCommentId(forumComment.getId());

        return CommentResponse.builder()
                .id(forumComment.getId())
                .postId(forumComment.getForumPost().getId())
                .creatorId(forumComment.getCreator().getId())
                .replies(responseMapping(replies))
                .content(forumComment.getContent())
                .mediaList(forumMediaService.mediaResponseMapping(mediaList))
                .likeCount(forumComment.getLike_count())
                .createdAt(String.valueOf(forumComment.getCreatedAt()))
                .createdBy(forumComment.getCreatedBy())
                .updatedAt(String.valueOf(forumComment.getUpdatedAt()))
                .updatedBy(forumComment.getUpdatedBy())
                .build();
    }
}
