package com.grd.gradingbe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForumPostRequest
{
    @Schema(description = "Id of the post's category", example = "1")
    @NotNull(message = "Category of the post cannot be blank")
    private Long channelId;

    @Schema(description = "Title of the post", example = "How to be rich?")
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @Schema(description = "Content of the post", example = "Any ideas of how to be rich?")
    @NotBlank(message = "Content cannot be blank")
    private String content;

    @Schema(description = "List of media within the post")
    private List<ForumMediaRequest> mediaList;

    @Schema(description = "Like of the post", example = "0")
    private Long likeCount;

    @Schema(description = "Is the post pinned", example = "true")
    private Boolean isPinned;

    @Schema(description = "Is the post locked", example = "true")
    private Boolean isLocked;
}
