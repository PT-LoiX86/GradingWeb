package com.grd.gradingbe.dto.request;

import com.grd.gradingbe.dto.enums.MediaType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class ForumMediaRequest
{
    @Schema(description = "Url address of the media")
    @NotBlank(message = "Url address cannot be blank")
    private String url;

    @Schema(description = "Type of the media", example = "IMAGE")
    @NotBlank(message = "Type cannot be blank")
    private MediaType mediaType;
}
