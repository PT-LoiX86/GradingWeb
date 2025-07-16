package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.request.ForumPostRequest;
import com.grd.gradingbe.dto.response.PageResponse;
import com.grd.gradingbe.dto.response.PostResponse;

public interface ForumPostService
{
    PageResponse<PostResponse> getPosts(int page, int size, String sortBy, String sortDir, String search);

    PostResponse createPost(Integer userId, ForumPostRequest request);

    PostResponse updatePost(Integer userId, Long id, ForumPostRequest request);

    void deletePost(Integer userId, Long id);

    void deletePost(Long id);

    void likePost(Long id, int like);

    PostResponse getPost(Long id);
}
