package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.request.CreateCommentRequest;
import com.grd.gradingbe.dto.response.CommentResponse;
import com.grd.gradingbe.dto.response.PageResponse;

public interface ForumCommentService
{
    PageResponse<CommentResponse> getComments(int page, int size, String sortBy, String sortDir, String search);

    CommentResponse createComment(Integer userId, CreateCommentRequest request);

    void deleteComment(Integer userId, Long id);

    void likeComment(Long id);
}
