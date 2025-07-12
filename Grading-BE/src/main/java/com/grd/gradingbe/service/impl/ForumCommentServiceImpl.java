package com.grd.gradingbe.service.impl;

import com.grd.gradingbe.dto.request.CreateCommentRequest;
import com.grd.gradingbe.dto.response.CommentResponse;
import com.grd.gradingbe.dto.response.PageResponse;
import com.grd.gradingbe.exception.ResourceNotFoundException;
import com.grd.gradingbe.model.ForumComment;
import com.grd.gradingbe.model.ForumPost;
import com.grd.gradingbe.model.User;
import com.grd.gradingbe.repository.ForumCommentRepository;
import com.grd.gradingbe.repository.ForumPostRepository;
import com.grd.gradingbe.repository.UserRepository;
import com.grd.gradingbe.service.ForumCommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumCommentServiceImpl implements ForumCommentService
{
    private final ForumCommentRepository forumCommentRepository;
    private final UserRepository userRepository;
    private final ForumPostRepository forumPostRepository;

    public ForumCommentServiceImpl(ForumCommentRepository forumCommentRepository, UserRepository userRepository, ForumPostRepository forumPostRepository) {
        this.forumCommentRepository = forumCommentRepository;
        this.userRepository = userRepository;
        this.forumPostRepository = forumPostRepository;
    }

    public PageResponse<CommentResponse> getComments(int page, int size, String sortBy, String sortDir, String search)
    {
        Sort sorter = sortBy.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sorter);
        Page<ForumComment> commentPages = forumCommentRepository.findAll(pageable);
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


    public CommentResponse createComment(Integer userId, CreateCommentRequest request)
    {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));

        ForumPost post = forumPostRepository.findById(request.getPost_id())
                .orElseThrow(() -> new ResourceNotFoundException("Post", "Id", request.getPost_id().toString()));

        ForumComment parentComment = forumCommentRepository.findById(request.getParent_id())
                .orElse(null);

        ForumComment comment = ForumComment.builder()
                .content(request.getContent())
                .like_count(0L)
                .forumPost(post)
                .creator(user)
                .parentComment(parentComment)
                .build();

        return responseMapping(forumCommentRepository.save(comment));
    }

    public void deleteComment(Integer userId, Long id)
    {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));

        ForumComment comment = forumCommentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", id.toString()));

        if (!comment.getCreator().equals(user))
        {
            throw new IllegalArgumentException("Comment's creator mismatch");
        }

        forumCommentRepository.deleteById(id);
    }

    public void likeComment(Long id)
    {
        ForumComment comment = forumCommentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Comment", "Id", id.toString()));

        comment.setLike_count(comment.getLike_count()+1);

        forumCommentRepository.save(comment);
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

        return CommentResponse.builder()
                .id(forumComment.getId())
                .post_id(forumComment.getForumPost().getId())
                .creator_id(forumComment.getCreator().getId())
                .replies(responseMapping(replies))
                .content(forumComment.getContent())
                .like_count(forumComment.getLike_count())
                .createdAt(String.valueOf(forumComment.getCreatedAt()))
                .createdBy(forumComment.getCreatedBy())
                .updatedAt(String.valueOf(forumComment.getUpdatedAt()))
                .updatedBy(forumComment.getUpdatedBy())
                .build();
    }
}
