package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.request.ForumCommentRequest;
import com.grd.gradingbe.dto.response.CommentResponse;
import com.grd.gradingbe.dto.response.PageResponse;

public interface ForumCommentService
{
    PageResponse<CommentResponse> getComments(int page, int size, String sortBy, String sortDir, String search);

    CommentResponse createComment(Integer userId, ForumCommentRequest request);

    void deleteComment(Integer userId, Long id);

    void likeComment(Long id, int like);

    CommentResponse updateComment(Integer userId, Long id, ForumCommentRequest request);
}
