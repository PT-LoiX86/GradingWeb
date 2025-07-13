package com.grd.gradingbe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCommentRequest
{
    @Schema(description = "Id of the post", example = "1")
    @Pattern(regexp = "^[0-9]+$", message = "Wrong id format")
    @NotBlank(message = "Post id of the comment cannot be null")
    private Long post_id;

    @Schema(description = "Id of the comment (if is a reply)", example = "1")
    @Pattern(regexp = "^[0-9]+$", message = "Wrong id format")
    private Long parent_id;

    @Schema(description = "Content of the post", example = "Any ideas of how to be rich?")
    @NotBlank(message = "Content cannot be blank")
    private String content;
}
