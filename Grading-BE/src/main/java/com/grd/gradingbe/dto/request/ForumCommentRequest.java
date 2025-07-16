package com.grd.gradingbe.dto.request;

import com.grd.gradingbe.model.ForumMedia;
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
public class ForumCommentRequest
{
    @Schema(description = "Id of the post", example = "1")
    @NotNull(message = "Post id of the comment cannot be null")
    private Long postId;

    @Schema(description = "Id of the comment (if is a reply)", example = "1")
    private Long parentId;

    @Schema(description = "Content of the comment (text)", example = "Any ideas of how to be rich?")
    @NotBlank(message = "Content cannot be blank")
    private String content;

    @Schema(description = "List of media within the comment")
    private List<ForumMediaRequest> mediaList;
}
