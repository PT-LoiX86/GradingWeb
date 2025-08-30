package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.request.ForumMediaRequest;
import com.grd.gradingbe.model.ForumComment;
import com.grd.gradingbe.model.ForumMedia;
import com.grd.gradingbe.model.ForumPost;

import java.util.List;

public interface ForumMediaService
{
    List<ForumMediaRequest> mediaResponseMapping(List<ForumMedia> media);

    ForumMediaRequest mediaResponseMapping(ForumMedia media);

    void updatePostMedia(ForumPost post, List<ForumMediaRequest> newMediaList);

    void updateCommentMedia(ForumComment comment, List<ForumMediaRequest> newMediaList);

}
