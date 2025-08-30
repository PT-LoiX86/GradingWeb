package com.grd.gradingbe.dto.response;

import com.grd.gradingbe.dto.request.ForumMediaRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(name = "PostResponse", description = "Response payload containing post information")
public record PostResponse(
        @Schema(description = "Unique identifier of the post", example = "1")
        Long id,

        @Schema(description = "Id of the post's category", example = "1")
        Long channelId,

        @Schema(description = "Id of the post's creator", example = "1")
        Integer creatorId,

        @Schema(description = "Title of the post", example = "How to become a billionaire?")
        String title,

        @Schema(description = "Content of the post", example = "Any ideas of how to be rich?")
        String content,

        @Schema(description = "List of comment's media")
        List<ForumMediaRequest> mediaList,

        @Schema(description = "Is the post pinned", example = "true")
        Boolean isPinned,

        @Schema(description = "Is the post locked", example = "true")
        Boolean isLocked,

        @Schema(description = "Number of likes of the post", example = "0")
        Long likeCount,

        @Schema(description = "Number of comments of the post", example = "0")
        Long commentCount,

        @Schema(description = "Creation timestamp", example = "2024-01-01T10:00:00")
        String createdAt,

        @Schema(description = "User who created this post", example = "admin")
        String createdBy,

        @Schema(description = "Last update timestamp", example = "2024-01-01T10:00:00")
        String updatedAt,

        @Schema(description = "User who last updated this post", example = "admin")
        String updatedBy
)
{ }
