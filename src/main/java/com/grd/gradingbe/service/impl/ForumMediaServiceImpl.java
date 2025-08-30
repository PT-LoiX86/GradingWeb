package com.grd.gradingbe.service.impl;

import com.grd.gradingbe.dto.request.ForumMediaRequest;
import com.grd.gradingbe.model.ForumComment;
import com.grd.gradingbe.model.ForumMedia;
import com.grd.gradingbe.model.ForumPost;
import com.grd.gradingbe.repository.ForumMediaRepository;
import com.grd.gradingbe.service.ForumMediaService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ForumMediaServiceImpl implements ForumMediaService
{
    private final ForumMediaRepository forumMediaRepository;

    public ForumMediaServiceImpl(ForumMediaRepository forumMediaRepository) {
        this.forumMediaRepository = forumMediaRepository;
    }

    public List<ForumMediaRequest> mediaResponseMapping(List<ForumMedia> media)
    {
        return media.stream()
                .map(this::mediaResponseMapping)
                .toList();
    }

    public ForumMediaRequest mediaResponseMapping(ForumMedia media)
    {
        return ForumMediaRequest.builder()
                .url(media.getUrl())
                .mediaType(media.getType())
                .build();
    }

    public void updatePostMedia(ForumPost post, List<ForumMediaRequest> newMediaList)
    {
        if (newMediaList == null)
        {
            newMediaList = Collections.emptyList();
        }

        List<ForumMedia> currentMediaList = forumMediaRepository.findAllByForumPostId(post.getId());

        updateMedia(
                currentMediaList,
                newMediaList,
                urls -> forumMediaRepository.deleteByPostIdAndUrl(post.getId(), urls),
                req -> ForumMedia.builder()
                        .url(req.getUrl())
                        .type(req.getMediaType())
                        .forumPost(post)
                        .build());
    }

    public void updateCommentMedia(ForumComment comment, List<ForumMediaRequest> newMediaList)
    {
        if (newMediaList == null)
        {
            newMediaList = Collections.emptyList();
        }

        List<ForumMedia> currentMediaList = forumMediaRepository.findAllByForumPostId(comment.getId());
        updateMedia(
                currentMediaList,
                newMediaList,
                urls -> forumMediaRepository.deleteByCommentIdAndUrl(comment.getId(), urls),
                req -> ForumMedia.builder()
                        .url(req.getUrl())
                        .type(req.getMediaType())
                        .forumComment(comment)
                        .build()
        );
    }

    private void updateMedia(
            List<ForumMedia> currentMediaList,
            List<ForumMediaRequest> newMediaList,
            Consumer<List<String>> bulkDelete,
            Function<ForumMediaRequest, ForumMedia> entityBuilder)
    {
        Set<ForumMediaRequest> currentMediaSet = new HashSet<>(mediaResponseMapping(currentMediaList));
        Set<ForumMediaRequest> newMediaSet = new HashSet<>(newMediaList);

        Set<ForumMediaRequest> toDelete = new HashSet<>(currentMediaSet);
        toDelete.removeAll(newMediaSet);

        Set<ForumMediaRequest> toAdd = new HashSet<>(newMediaSet);
        toAdd.removeAll(currentMediaSet);

        if (!toDelete.isEmpty()) {
            List<String> urlsToDelete = toDelete.stream()
                    .map(ForumMediaRequest::getUrl)
                    .collect(Collectors.toList());
            bulkDelete.accept(urlsToDelete);
        }
        if (!toAdd.isEmpty()) {
            List<ForumMedia> toAddList = toAdd.stream()
                    .map(entityBuilder)
                    .collect(Collectors.toList());
            forumMediaRepository.saveAll(toAddList);
        }
    }
}
