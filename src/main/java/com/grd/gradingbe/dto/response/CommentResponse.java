package com.grd.gradingbe.dto.response;

import com.grd.gradingbe.dto.request.ForumMediaRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(name = "CommentResponse", description = "Response payload containing post's comments")
public record CommentResponse(
        @Schema(description = "Unique identifier of the comment", example = "1")
        Long id,

        @Schema(description = "Id of the comment's post", example = "1")
        Long postId,

        @Schema(description = "Id of the comment's creator", example = "1")
        Integer creatorId,

        @Schema(description = "Content of the comment", example = "Idk man")
        String content,

        @Schema(description = "List of comment's media")
        List<ForumMediaRequest> mediaList,

        @Schema(description = "Number of comment's like", example = "0")
        Long likeCount,

        @Schema(description = "This comment's replies", example = "[]")
        List<CommentResponse> replies,

        @Schema(description = "Creation timestamp", example = "2024-01-01T10:00:00")
        String createdAt,

        @Schema(description = "User who created this comment", example = "admin")
        String createdBy,

        @Schema(description = "Last update timestamp", example = "2024-01-01T10:00:00")
        String updatedAt,

        @Schema(description = "User who last updated this comment", example = "admin")
        String updatedBy
)
{ }
