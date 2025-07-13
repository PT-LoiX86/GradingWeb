package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.request.CreatePostRequest;
import com.grd.gradingbe.dto.request.UpdatePostRequest;
import com.grd.gradingbe.dto.response.PageResponse;
import com.grd.gradingbe.dto.response.PostResponse;

public interface ForumPostService
{
    PageResponse<PostResponse> getPosts(int page, int size, String sortBy, String sortDir, String search);

    PostResponse createPost(Integer userId, CreatePostRequest request);

    PostResponse updatePost(Integer userId, Long id, UpdatePostRequest request);

    void deletePost(Integer userId, Long id);

    void likePost(Long id);
}
