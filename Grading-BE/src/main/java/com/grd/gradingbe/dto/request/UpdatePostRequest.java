package com.grd.gradingbe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatePostRequest
{
    @Schema(description = "Title of the post", example = "How to be rich?")
    private String title;

    @Schema(description = "Content of the post", example = "Any ideas of how to be rich?", required = true)
    private String content;

    @Schema(description = "Like of the post", example = "0")
    @Pattern(regexp = "^[0-9]+$", message = "Wrong number of likes format")
    private Long like_count;

    @Schema(description = "Is the post pinned", example = "true")
    @Pattern(regexp = "true|false", message = "Value must be 'true' or 'false'")
    private Boolean is_pinned;

    @Schema(description = "Is the post locked", example = "true")
    @Pattern(regexp = "true|false", message = "Value must be 'true' or 'false'")
    private Boolean is_locked;
}
