import React, { useState, useEffect } from 'react';
import { forumAPI } from '../../services/api';
import type { ForumPost, ForumChannel, PaginationParams, CreatePostRequest, CreateCommentRequest, ForumComment } from '../../types/api';
import { Search, MessageSquare, ThumbsUp, MessageCircle, Plus, ChevronDown } from 'lucide-react';
import Layout from '../Layout';
import CreatePostModal from '../forum/CreatePostModal';
import PostDetail from '../forum/PostDetail';
import { toast } from 'react-hot-toast';

interface ForumPageProps {
  onLogout: () => Promise<void>;
}

const ForumPage: React.FC<ForumPageProps> = ({ onLogout }) => {
  const [posts, setPosts] = useState<ForumPost[]>([]);
  const [categories, setCategories] = useState<ForumChannel[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<number | null>(null);
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
    sort: 'createdAt',
    direction: 'desc'
  });
  
  const [totalPages, setTotalPages] = useState(0);
  const [totalItems, setTotalItems] = useState(0);
  
  // Fetch posts with current pagination and search params
  const fetchPosts = async () => {
    try {
      setLoading(true);
      const params: PaginationParams = {
        ...pagination,
        search: searchTerm
      };
      
      if (selectedCategory) {
        params.channelId = selectedCategory;
      }
      
      const response = await forumAPI.getPosts(params);
      if (response.data) {
        setPosts(response.data.content || []);
        setTotalPages(response.data.totalPages || 0);
        setTotalItems(response.data.totalElements || 0);
      }
    } catch (err) {
      setError('Failed to load forum posts');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };
  
  // Fetch categories
  const fetchCategories = async () => {
    try {
      const response = await forumAPI.getChannels();
      if (response.data) {
        setCategories(response.data.content || []);
      }
    } catch (err) {
      setError('Failed to load categories');
      console.error(err);
    }
  };
  
  // Fetch comments for a specific post
  const fetchComments = async (postId: number) => {
    try {
      setCommentsLoading(true);
      // In a real implementation, you'd filter by postId
      const response = await forumAPI.getComments({ 
        page: 0,
        size: 50,
        sort: 'createdAt',
        direction: 'asc'
      });
      
      if (response.data) {
        const commentsForPost = response.data.content || [];
        setPostComments(commentsForPost);
      }
    } catch (err) {
      console.error('Failed to load comments:', err);
    } finally {
      setCommentsLoading(false);
    }
  };
  
  useEffect(() => {
    const loadData = async () => {
      await fetchPosts();
      await fetchCategories();
    };
    
    loadData();
  }, [pagination.page, pagination.size, pagination.sort, pagination.direction, selectedCategory]);
  
  // Handler for search
  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();
    await fetchPosts();
  };
  
  // Navigate to different page
  const handlePageChange = (newPage: number) => {
    setPagination({
      ...pagination,
      page: newPage
    });
  };
  
  const [isCategoryDropdownOpen, setIsCategoryDropdownOpen] = useState(false);
  
  // Update dropdown state
  const toggleCategoryDropdown = () => {
    setIsCategoryDropdownOpen(!isCategoryDropdownOpen);
  };
  
  // Filter by category
  const handleCategoryChange = async (categoryId: number | null) => {
    setSelectedCategory(categoryId);
    setIsCategoryDropdownOpen(false);
    setPagination({
      ...pagination,
      page: 0 // Reset to first page when changing category
    });
  };
  
  // Create new post
  const handleCreatePost = async (postData: CreatePostRequest) => {
    try {
      setSubmitting(true);
      const response = await forumAPI.createPost(postData);
      
      if (response.data) {
        // Add new post to the list if we're on the first page
        if (pagination.page === 0) {
          setPosts(prevPosts => {
            if (response.data) {
              return [response.data as ForumPost, ...prevPosts];
            }
            return prevPosts;
          });
        } else {
          // If not on first page, go to first page to show the new post
          setPagination({
            ...pagination,
            page: 0
          });
        }
        
        toast.success('Bài viết đã được tạo thành công');
      }
    } catch (err) {
      console.error('Error creating post:', err);
      toast.error('Có lỗi xảy ra khi tạo bài viết');
      throw err;
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
      
      // Update the likes count in the UI
      setPosts(prevPosts => 
        prevPosts.map(post => 
          post.id === postId 
            ? { ...post, likesCount: post.likesCount + 1 } 
            : post
        )
      );
      
      // If we're viewing this post in detail, update it there too
      if (selectedPost?.id === postId) {
        setSelectedPost(prevPost => 
          prevPost ? { ...prevPost, likesCount: prevPost.likesCount + 1 } : null
        );
      }
    } catch (err) {
      console.error('Error liking post:', err);
      toast.error('Có lỗi xảy ra khi thích bài viết');
      throw err;
    }
  };
  
  // Add a comment
  const handleAddComment = async (comment: CreateCommentRequest) => {
    try {
      setSubmitting(true);
      const response = await forumAPI.createComment(comment);
      
      if (response.data && selectedPost) {
        // Add the new comment to the list
        setPostComments(prevComments => {
          if (response.data) {
            return [...prevComments, response.data as ForumComment];
          }
          return prevComments;
        });
        
        // Update the comments count in the selected post
        setSelectedPost(prevPost => 
          prevPost ? { ...prevPost, commentsCount: prevPost.commentsCount + 1 } : null
        );
        
        // Also update the post in the list
        setPosts(prevPosts => 
          prevPosts.map(post => 
            post.id === selectedPost.id 
              ? { ...post, commentsCount: post.commentsCount + 1 } 
              : post
          )
        );
      }
    } catch (err) {
      console.error('Error adding comment:', err);
      toast.error('Có lỗi xảy ra khi thêm bình luận');
      throw err;
    } finally {
      setSubmitting(false);
    }
  };
  
  // Like a comment
  const handleLikeComment = async (commentId: number) => {
    try {
      await forumAPI.likeComment(commentId);
      
      // Update the likes count in the UI
      setPostComments(prevComments => 
        prevComments.map(comment => 
          comment.id === commentId 
            ? { ...comment, likesCount: comment.likesCount + 1 } 
            : comment
        )
      );
    } catch (err) {
      console.error('Error liking comment:', err);
      toast.error('Có lỗi xảy ra khi thích bình luận');
      throw err;
    }
  };
  
  return (
    <Layout onLogout={onLogout}>
      <div className="container mx-auto p-4 max-w-7xl">
        {/* Header Section */}
        <div className="bg-white shadow-sm rounded-lg mb-6 p-6">
          <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-6">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">Diễn đàn</h1>
              <p className="text-gray-500 mt-1">Trao đổi, thảo luận về các vấn đề học tập</p>
            </div>
            
            <button 
              onClick={() => setIsCreateModalOpen(true)}
              className="mt-4 md:mt-0 flex items-center px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
            >
              <Plus className="w-4 h-4 mr-2" />
              Bài viết mới
            </button>
          </div>
          
          {/* Search and Filter Bar */}
          <div className="flex flex-col md:flex-row space-y-4 md:space-y-0 md:space-x-4">
            <form onSubmit={handleSearch} className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
              <input
                type="text"
                placeholder="Tìm kiếm bài viết..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10 pr-4 py-2 w-full border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              <button type="submit" className="sr-only">Search</button>
            </form>
            
            {/* Categories Filter */}
            <div className="relative">
              <button 
                onClick={toggleCategoryDropdown}
                className="flex items-center justify-between w-full md:w-56 px-4 py-2 bg-white border border-gray-300 rounded-md focus:outline-none"
              >
                <span>{selectedCategory ? categories.find(c => c.id === selectedCategory)?.name : 'Tất cả danh mục'}</span>
                <ChevronDown className="w-4 h-4 text-gray-500" />
              </button>
              
              {/* Dropdown */}
              {isCategoryDropdownOpen && (
                <div className="absolute z-10 mt-1 w-full bg-white border border-gray-300 rounded-md shadow-lg">
                  <ul className="py-1 max-h-48 overflow-auto">
                    <li>
                      <button
                        onClick={() => handleCategoryChange(null)}
                        className="block w-full text-left px-4 py-2 text-gray-700 hover:bg-gray-100"
                      >
                        Tất cả danh mục
                      </button>
                    </li>
                    {categories.map((category) => (
                      <li key={category.id}>
                        <button
                          onClick={() => handleCategoryChange(category.id)}
                          className="block w-full text-left px-4 py-2 text-gray-700 hover:bg-gray-100"
                        >
                          {category.name}
                        </button>
                      </li>
                    ))}
                  </ul>
                </div>
              )}
            </div>
          </div>
        </div>
        
        {/* Error Message */}
        {error && (
          <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded mb-6">
            {error}
          </div>
        )}
        
        {/* Loading State */}
        {loading && (
          <div className="text-center py-12">
            <div className="inline-block h-8 w-8 animate-spin rounded-full border-4 border-solid border-blue-600 border-r-transparent"></div>
            <p className="mt-3 text-gray-600">Đang tải dữ liệu...</p>
          </div>
        )}
        
        {/* Posts List */}
        {!loading && posts.length === 0 ? (
          <div className="text-center py-16">
            <MessageSquare className="mx-auto h-12 w-12 text-gray-400" />
            <h3 className="mt-4 text-lg font-medium text-gray-900">Không có bài viết</h3>
            <p className="mt-1 text-gray-500">Hãy tạo bài viết đầu tiên để bắt đầu cuộc thảo luận.</p>
          </div>
        ) : (
          <div className="bg-white shadow-sm rounded-lg overflow-hidden">
            <ul className="divide-y divide-gray-200">
              {posts.map((post) => (
                <li 
                  key={post.id} 
                  className="p-6 hover:bg-gray-50 transition-colors cursor-pointer"
                  onClick={() => handleOpenPostDetail(post)}
                >
                  <div className="flex items-start">
                    {/* User avatar placeholder */}
                    <div className="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center mr-4 text-blue-700 font-semibold">
                      {post.authorName.substring(0, 2).toUpperCase()}
                    </div>
                    
                    <div className="flex-1 min-w-0">
                      <h3 className="text-lg font-semibold text-gray-900 truncate">{post.title}</h3>
                      
                      <div className="flex items-center mt-1 text-sm text-gray-500">
                        <span>{post.authorName}</span>
                        <span className="mx-2">&middot;</span>
                        <span>{new Date(post.createdAt).toLocaleDateString('vi-VN')}</span>
                        <span className="mx-2">&middot;</span>
                        <span className="bg-blue-100 text-blue-800 px-2 py-0.5 rounded text-xs">
                          {post.channelName}
                        </span>
                      </div>
                      
                      <p className="mt-3 text-gray-700 line-clamp-2">
                        {post.content}
                      </p>
                      
                      <div className="mt-4 flex items-center space-x-4 text-sm">
                        <button 
                          className="flex items-center text-gray-500 hover:text-blue-600"
                          onClick={(e) => {
                            e.stopPropagation();
                            handleLikePost(post.id);
                          }}
                        >
                          <ThumbsUp className="w-4 h-4 mr-1" />
                          <span>{post.likesCount}</span>
                        </button>
                        <div className="flex items-center text-gray-500">
                          <MessageCircle className="w-4 h-4 mr-1" />
                          <span>{post.commentsCount}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </li>
              ))}
            </ul>
          </div>
        )}
        
        {/* Pagination */}
        {totalPages > 1 && (
          <div className="flex justify-between items-center mt-6 pb-6">
            <div className="text-sm text-gray-600">
              Hiển thị {posts.length} trên tổng số {totalItems} bài viết
            </div>
            
            <div className="flex space-x-1">
              <button
                onClick={() => handlePageChange(Math.max(0, pagination.page! - 1))}
                disabled={pagination.page === 0}
                className="px-3 py-1 rounded border border-gray-300 text-gray-600 disabled:opacity-50"
              >
                Previous
              </button>
              
              {[...Array(Math.min(5, totalPages))].map((_, idx) => {
                // Logic to show current page and adjacent pages
                const pageToShow = pagination.page! < 2
                  ? idx
                  : pagination.page! - 2 + idx;
                
                if (pageToShow >= totalPages) return null;
                
                return (
                  <button
                    key={pageToShow}
                    onClick={() => handlePageChange(pageToShow)}
                    className={`px-3 py-1 rounded border ${
                      pagination.page === pageToShow
                        ? 'bg-blue-600 text-white border-blue-600'
                        : 'border-gray-300 text-gray-600'
                    }`}
                  >
                    {pageToShow + 1}
                  </button>
                );
              })}
              
              <button
                onClick={() => handlePageChange(Math.min(totalPages - 1, (pagination.page || 0) + 1))}
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