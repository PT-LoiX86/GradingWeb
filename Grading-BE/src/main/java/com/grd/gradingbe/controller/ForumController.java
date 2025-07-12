package com.grd.gradingbe.controller;

import com.grd.gradingbe.dto.request.CreateCommentRequest;
import com.grd.gradingbe.dto.request.CreatePostRequest;
import com.grd.gradingbe.dto.request.UpdatePostRequest;
import com.grd.gradingbe.dto.response.*;
import com.grd.gradingbe.service.ForumChannelService;
import com.grd.gradingbe.service.ForumCommentService;
import com.grd.gradingbe.service.ForumPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(path = "api/forums", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Forum Management", description = "API endpoints for managing forums")
public class ForumController
{
    private final ForumChannelService forumChannelService;
    private final ForumCommentService forumCommentService;
    private final ForumPostService forumPostService;

    public ForumController(ForumChannelService forumChannelService, ForumCommentService forumCommentService, ForumPostService forumPostService) {
        this.forumChannelService = forumChannelService;
        this.forumCommentService = forumCommentService;
        this.forumPostService = forumPostService;
    }

    @Operation(
            summary = "Get all forum's categories",
            description = "Retrieve a paginated list of all categories"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved all categories",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            )
    })
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<PageResponse<ChannelResponse>>> getChannels(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field", example = "name")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir,
            @Parameter(description = "Search keyword for major name or code")
            @RequestParam(required = false) String search
    )
    {
        PageResponse<ChannelResponse> channelResponse = forumChannelService.getChannels(page, size, sortBy, sortDir, search);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(
                "Successfully retrieved all categories", channelResponse
        ));
    }

    @Operation(
            summary = "Get all forum's posts",
            description = "Retrieve a paginated list of all posts with optional search and sorting capabilities"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved all posts",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters"
            )
    })
    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<PageResponse<PostResponse>>> getPosts(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field", example = "name")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir,
            @Parameter(description = "Search keyword for major name or code")
            @RequestParam(required = false) String search)
    {
        PageResponse<PostResponse> postResponse = forumPostService.getPosts(page, size, sortBy, sortDir, search);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(
                "Successfully retrieved all posts", postResponse
        ));
    }

    @Operation(
            summary = "Create a post",
            description = "Create a post with required information"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Successfully created a post",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Categories not found"
            )
    })
    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            Principal principal,
            @Parameter(description = "Post creating request data", required = true)
            @Valid @RequestBody CreatePostRequest request)
    {
        PostResponse postResponse = forumPostService.createPost(Integer.parseInt(principal.getName()), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                "Successfully created a post", postResponse
        ));
    }

    @Operation(
            summary = "Edit a post",
            description = "Edit information of the provided post"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully edit the post",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Post not found"
            )
    })
    @PutMapping("/posts/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            Principal principal,
            @Parameter(description = "Post ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Post editing request data", required = true)
            @Valid @RequestBody UpdatePostRequest request)
    {
        PostResponse postResponse = forumPostService.updatePost(Integer.parseInt(principal.getName()), id, request);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(
                "Successfully updated the post", postResponse
        ));
    }

    @Operation(
            summary = "Delete a post",
            description = "Delete the provided post"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "204",
                    description = "Successfully delete the post",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Post not found"
            )
    })
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            Principal principal,
            @Parameter(description = "Post ID", required = true, example = "1")
            @PathVariable Long id)
    {
        forumPostService.deletePost(Integer.parseInt(principal.getName()),  id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success(
                "Successfully deleted the post", null
        ));
    }

    @Operation(
            summary = "Like a post",
            description = "Add a like to the provided post"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Successfully like the post",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Post not found"
            )
    })
    @PostMapping("/posts/{id}/like")
    public ResponseEntity<ApiResponse<Void>> likePost(
            @Parameter(description = "Post ID", required = true, example = "1")
            @PathVariable Long id)
    {
        forumPostService.likePost(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                "Successfully added a like to the post", null
        ));
    }

    @Operation(
            summary = "Get all post's comments",
            description = "Retrieve a paginated list of all comments within the post"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved all comments",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            )
    })
    @GetMapping("/comments")
    public ResponseEntity<ApiResponse<PageResponse<CommentResponse>>> getComments(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field", example = "name")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir,
            @Parameter(description = "Search keyword for major name or code")
            @RequestParam(required = false) String search)
    {
        PageResponse<CommentResponse> commentResponse = forumCommentService.getComments(page, size, sortBy, sortDir, search);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(
                "Successfully retrieved all comments", commentResponse
        ));
    }

    @Operation(
            summary = "Post a comment",
            description = "Add a comment to provided post"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Successfully add the comment",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Post or parent comment not found"
            )
    })
    @PostMapping("/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> commentPost(
            Principal principal,
            @Parameter(description = "Commenting request data", required = true)
            @Valid @RequestBody CreateCommentRequest request)
    {
        CommentResponse commentResponse = forumCommentService.createComment(Integer.parseInt(principal.getName()), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                "Successfully created a comment", commentResponse
        ));
    }

    @Operation(
            summary = "Delete a comment",
            description = "Delete comment from the provided post"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "204",
                    description = "Successfully delete the comment",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Post not found"
            )
    })
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            Principal principal,
            @Parameter(description = "Comment ID", required = true, example = "1")
            @PathVariable Long id)
    {
        forumCommentService.deleteComment(Integer.parseInt(principal.getName()),  id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success(
                "Successfully deleted the comment", null
        ));
    }

    @Operation(
            summary = "Like a comment",
            description = "Add a like to the provided comment"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Successfully like the comment",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Comment not found"
            )
    })
    @PostMapping("/comments/{id}/like")
    public ResponseEntity<ApiResponse<Void>> likeComment(
            @Parameter(description = "Comment ID", required = true, example = "1")
            @PathVariable Long id)
    {
        forumCommentService.likeComment(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                "Successfully added a like to the comment", null
        ));
    }
}
