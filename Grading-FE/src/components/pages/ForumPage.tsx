import React, { useState, useEffect } from "react";
import { forumAPI } from "../../services/api";
import type {
    ForumPost,
    ForumChannel,
    PaginationParams,
    CreatePostRequest,
    CreateCommentRequest,
    ForumComment,
} from "../../types/api";
import {
    Search,
    MessageSquare,
    ThumbsUp,
    MessageCircle,
    Plus,
    ChevronDown,
} from "lucide-react";
import Layout from "../Layout";
import CreatePostModal from "../forum/CreatePostModal";
import PostDetail from "../forum/PostDetail";
import { toast } from "react-hot-toast";

interface ForumPageProps {
    onLogout: () => Promise<void>;
}

const ForumPage: React.FC<ForumPageProps> = ({ onLogout }) => {
    const [posts, setPosts] = useState<ForumPost[]>([]);
    const [categories, setCategories] = useState<ForumChannel[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [searchTerm, setSearchTerm] = useState("");
    const [selectedCategory, setSelectedCategory] = useState<number | null>(
        null
    );
    const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);

    // Post detail state
    const [selectedPost, setSelectedPost] = useState<ForumPost | null>(null);
    const [isDetailOpen, setIsDetailOpen] = useState(false);
    const [postComments, setPostComments] = useState<ForumComment[]>([]);
    const [commentsLoading, setCommentsLoading] = useState(false);
    const [submitting, setSubmitting] = useState(false);

    // Pagination state
    const [pagination, setPagination] = useState<PaginationParams>({
        page: 0,
        size: 10,
        sort: "createdAt",
        direction: "desc",
    });

    const [totalPages, setTotalPages] = useState(0);

    // Fetch posts with current pagination and search params
    const fetchPosts = async () => {
        try {
            setLoading(true);
            const params: PaginationParams = {
                ...pagination,
                search: searchTerm,
            };

            if (selectedCategory) {
                params.channelId = selectedCategory;
            }

            const response = await forumAPI.getPosts(params);
            setPosts(response.content || []);
            setTotalPages(response.totalPages || 0);
        } catch (err) {
            setError("Failed to load forum posts");
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    // Fetch categories
    const fetchCategories = async () => {
        try {
            const response = await forumAPI.getChannels();
            setCategories(response);
        } catch (err) {
            setError("Failed to load categories");
            console.error(err);
        }
    };

    // Fetch comments for a specific post
    const fetchComments = async (postId: number) => {
        try {
            setCommentsLoading(true);
            const response = await forumAPI.getComments(postId);
            setPostComments(response);
        } catch (err) {
            console.error("Failed to load comments:", err);
        } finally {
            setCommentsLoading(false);
        }
    };

    useEffect(() => {
        const loadData = async () => {
            await Promise.all([fetchPosts(), fetchCategories()]);
        };
        loadData();
    }, [
        pagination.page,
        pagination.size,
        pagination.sort,
        pagination.direction,
        selectedCategory,
    ]);

    // Search effect
    useEffect(() => {
        const delayedSearch = setTimeout(() => {
            if (pagination.page === 0) {
                fetchPosts();
            } else {
                setPagination({ ...pagination, page: 0 });
            }
        }, 500);

        return () => clearTimeout(delayedSearch);
    }, [searchTerm]);

    // Create new post
    const handleCreatePost = async (postData: CreatePostRequest) => {
        try {
            setSubmitting(true);
            const response = await forumAPI.createPost(postData);

            // Add new post to the list if we're on the first page
            if (pagination.page === 0) {
                setPosts((prevPosts) => {
                    return [response, ...prevPosts];
                });
            } else {
                // If not on first page, go to first page to show the new post
                setPagination({
                    ...pagination,
                    page: 0,
                });
            }

            setIsCreateModalOpen(false);
            toast.success("Post created successfully!");
        } catch (err) {
            console.error("Failed to create post:", err);
            toast.error("Failed to create post");
        } finally {
            setSubmitting(false);
        }
    };

    // Open post detail
    const handleOpenPostDetail = (post: ForumPost) => {
        setSelectedPost(post);
        setIsDetailOpen(true);
        fetchComments(post.id);
    };

    // Like a post
    const handleLikePost = async (postId: number) => {
        try {
            await forumAPI.likePost(postId);

            // Update the post in the list
            setPosts((prevPosts) =>
                prevPosts.map((post) =>
                    post.id === postId
                        ? { ...post, likeCount: post.likeCount + 1 }
                        : post
                )
            );

            // Update selectedPost if it's the same post
            setSelectedPost((prevPost) =>
                prevPost && prevPost.id === postId
                    ? { ...prevPost, likeCount: prevPost.likeCount + 1 }
                    : prevPost
            );
        } catch (err) {
            console.error("Failed to like post:", err);
            toast.error("Failed to like post");
        }
    };

    // Add comment to post
    const handleAddComment = async (commentData: CreateCommentRequest) => {
        try {
            const response = await forumAPI.createComment(commentData);

            if (selectedPost) {
                // Add comment to the list
                setPostComments((prevComments) => {
                    return [...prevComments, response];
                });

                // Update comment count in selectedPost
                setSelectedPost((prevPost) =>
                    prevPost
                        ? {
                              ...prevPost,
                              commentCount: prevPost.commentCount + 1,
                          }
                        : null
                );

                // Update comment count in posts list
                setPosts((prevPosts) =>
                    prevPosts.map((post) =>
                        post.id === selectedPost.id
                            ? { ...post, commentCount: post.commentCount + 1 }
                            : post
                    )
                );
            }
        } catch (err) {
            console.error("Failed to add comment:", err);
            toast.error("Failed to add comment");
        }
    };

    // Like a comment
    const handleLikeComment = async (commentId: number) => {
        try {
            await forumAPI.likeComment(commentId);

            // Update the comment in the list
            setPostComments((prevComments) =>
                prevComments.map((comment) =>
                    comment.id === commentId
                        ? { ...comment, likeCount: comment.likeCount + 1 }
                        : comment
                )
            );
        } catch (err) {
            console.error("Failed to like comment:", err);
            toast.error("Failed to like comment");
        }
    };

    // Handle page change
    const handlePageChange = (newPage: number) => {
        setPagination({
            ...pagination,
            page: newPage,
        });
    };

    // Handle category filter
    const handleCategoryChange = (categoryId: number | null) => {
        setSelectedCategory(categoryId);
        setPagination({ ...pagination, page: 0 });
    };

    return (
        <Layout onLogout={onLogout}>
            <div className="max-w-6xl mx-auto">
                {/* Header */}
                <div className="mb-8">
                    <div className="flex justify-between items-center mb-6">
                        <h1 className="text-3xl font-bold text-gray-900">
                            Forum Discussion
                        </h1>
                        <button
                            onClick={() => setIsCreateModalOpen(true)}
                            className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors flex items-center space-x-2"
                        >
                            <Plus className="w-4 h-4" />
                            <span>New Post</span>
                        </button>
                    </div>

                    {/* Search and filters */}
                    <div className="flex flex-col sm:flex-row gap-4">
                        <div className="flex-1 relative">
                            <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
                            <input
                                type="text"
                                placeholder="Search posts..."
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                            />
                        </div>

                        <div className="relative">
                            <select
                                value={selectedCategory || ""}
                                onChange={(e) =>
                                    handleCategoryChange(
                                        e.target.value
                                            ? Number(e.target.value)
                                            : null
                                    )
                                }
                                className="appearance-none bg-white border border-gray-300 rounded-lg px-4 py-2 pr-8 focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                            >
                                <option value="">All Categories</option>
                                {categories.map((category) => (
                                    <option
                                        key={category.id}
                                        value={category.id}
                                    >
                                        {category.name}
                                    </option>
                                ))}
                            </select>
                            <ChevronDown className="absolute right-2 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4 pointer-events-none" />
                        </div>
                    </div>
                </div>

                {/* Error state */}
                {error && (
                    <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg mb-6">
                        {error}
                    </div>
                )}

                {/* Loading state */}
                {loading && (
                    <div className="text-center py-8">
                        <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
                        <p className="mt-2 text-gray-600">Loading posts...</p>
                    </div>
                )}

                {/* Posts list */}
                {!loading && (
                    <div className="space-y-4">
                        {posts.length === 0 ? (
                            <div className="text-center py-12">
                                <MessageSquare className="mx-auto h-12 w-12 text-gray-400 mb-4" />
                                <h3 className="text-lg font-medium text-gray-900 mb-2">
                                    No posts found
                                </h3>
                                <p className="text-gray-600">
                                    Be the first to start a discussion!
                                </p>
                            </div>
                        ) : (
                            posts.map((post) => (
                                <div
                                    key={post.id}
                                    className="bg-white rounded-lg shadow border border-gray-200 p-6 hover:shadow-md transition-shadow cursor-pointer"
                                    onClick={() => handleOpenPostDetail(post)}
                                >
                                    <div className="flex justify-between items-start mb-3">
                                        <div className="flex-1">
                                            <h3 className="text-lg font-semibold text-gray-900 mb-1">
                                                {post.title}
                                            </h3>
                                            <div className="flex items-center text-sm text-gray-600 space-x-4">
                                                <span>
                                                    by {post.authorName}
                                                </span>
                                                <span>•</span>
                                                <span>
                                                    {new Date(
                                                        post.createdAt
                                                    ).toLocaleDateString()}
                                                </span>
                                            </div>
                                        </div>
                                        <span className="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded">
                                            {post.channelName}
                                        </span>
                                    </div>

                                    <p className="text-gray-700 mb-4 line-clamp-2">
                                        {post.content.length > 150
                                            ? `${post.content.substring(0, 150)}...`
                                            : post.content}
                                    </p>

                                    <div className="flex items-center space-x-6 text-sm text-gray-500">
                                        <div className="flex items-center space-x-1">
                                            <ThumbsUp className="w-4 h-4" />
                                            <span>{post.likeCount}</span>
                                        </div>
                                        <div className="flex items-center space-x-1">
                                            <MessageCircle className="w-4 h-4" />
                                            <span>{post.commentCount}</span>
                                        </div>
                                    </div>
                                </div>
                            ))
                        )}
                    </div>
                )}

                {/* Pagination */}
                {!loading && totalPages > 1 && (
                    <div className="mt-8 flex justify-center">
                        <div className="flex items-center space-x-1">
                            <button
                                onClick={() =>
                                    handlePageChange(
                                        Math.max(0, (pagination.page || 0) - 1)
                                    )
                                }
                                disabled={pagination.page === 0}
                                className="px-3 py-1 rounded border border-gray-300 text-gray-600 disabled:opacity-50"
                            >
                                Previous
                            </button>

                            {Array.from(
                                { length: Math.min(5, totalPages) },
                                (_, i) => {
                                    const startPage = Math.max(
                                        0,
                                        Math.min(
                                            pagination.page - 2,
                                            totalPages - 5
                                        )
                                    );
                                    const pageToShow = startPage + i;
                                    return (
                                        <button
                                            key={pageToShow}
                                            onClick={() =>
                                                handlePageChange(pageToShow)
                                            }
                                            className={`px-3 py-1 rounded border ${
                                                pagination.page === pageToShow
                                                    ? "bg-blue-600 text-white border-blue-600"
                                                    : "border-gray-300 text-gray-600"
                                            }`}
                                        >
                                            {pageToShow + 1}
                                        </button>
                                    );
                                }
                            )}

                            <button
                                onClick={() =>
                                    handlePageChange(
                                        Math.min(
                                            totalPages - 1,
                                            (pagination.page || 0) + 1
                                        )
                                    )
                                }
                                disabled={pagination.page === totalPages - 1}
                                className="px-3 py-1 rounded border border-gray-300 text-gray-600 disabled:opacity-50"
                            >
                                Next
                            </button>
                        </div>
                    </div>
                )}
            </div>

            {/* Create Post Modal */}
            <CreatePostModal
                isOpen={isCreateModalOpen}
                onClose={() => setIsCreateModalOpen(false)}
                onSubmit={handleCreatePost}
                categories={categories}
                isLoading={submitting}
            />

            {/* Post Detail Modal */}
            {selectedPost && (
                <PostDetail
                    post={selectedPost}
                    comments={postComments}
                    isOpen={isDetailOpen}
                    onClose={() => setIsDetailOpen(false)}
                    onLikePost={handleLikePost}
                    onAddComment={handleAddComment}
                    onLikeComment={handleLikeComment}
                    isLoading={submitting || commentsLoading}
                />
            )}
        </Layout>
    );
};

export default ForumPage;
