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
public class CreatePostRequest
{
    @Schema(description = "Id of the post's category", example = "1")
    @Pattern(regexp = "^[0-9]+$", message = "Wrong id format")
    @NotBlank(message = "Category of the post cannot be blank")
    private Long channel_id;

    @Schema(description = "Title of the post", example = "How to be rich?")
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @Schema(description = "Content of the post", example = "Any ideas of how to be rich?")
    @NotBlank(message = "Content cannot be blank")
    private String content;
}
