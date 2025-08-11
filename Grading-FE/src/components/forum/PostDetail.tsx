import React, { useState } from 'react';
import type { ForumPost, ForumComment, CreateCommentRequest } from '../../types/api';
import { ThumbsUp, MessageCircle, X, ArrowLeft, Send } from 'lucide-react';

interface PostDetailProps {
  post: ForumPost;
  comments: ForumComment[];
  isOpen: boolean;
  onClose: () => void;
  onLikePost: (postId: number) => Promise<void>;
  onAddComment: (comment: CreateCommentRequest) => Promise<void>;
  onLikeComment: (commentId: number) => Promise<void>;
  isLoading?: boolean;
}

const PostDetail: React.FC<PostDetailProps> = ({
  post,
  comments,
  isOpen,
  onClose,
  onLikePost,
  onAddComment,
  onLikeComment,
  isLoading = false
}) => {
  const [newComment, setNewComment] = useState('');
  const [replyToComment, setReplyToComment] = useState<number | null>(null);
  const [error, setError] = useState<string | null>(null);
  
  if (!isOpen) return null;
  
  const handleLikePost = async () => {
    try {
      await onLikePost(post.id);
    } catch (err) {
      console.error('Error liking post:', err);
      setError('Có lỗi xảy ra khi thích bài viết');
    }
  };
  
  const handleLikeComment = async (commentId: number) => {
    try {
      await onLikeComment(commentId);
    } catch (err) {
      console.error('Error liking comment:', err);
      setError('Có lỗi xảy ra khi thích bình luận');
    }
  };
  
  const handleSubmitComment = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!newComment.trim()) {
      return;
    }
    
    try {
      const commentRequest: CreateCommentRequest = {
        content: newComment.trim(),
        postId: post.id,
        parentId: replyToComment || undefined
      };
      
      await onAddComment(commentRequest);
      setNewComment('');
      setReplyToComment(null);
    } catch (err) {
      console.error('Error adding comment:', err);
      setError('Có lỗi xảy ra khi thêm bình luận');
    }
  };
  
  // Group comments by parent
  const parentComments = comments.filter(comment => !comment.parentId);
  const childComments = comments.filter(comment => comment.parentId);
  
  const getChildComments = (parentId: number) => {
    return childComments.filter(comment => comment.parentId === parentId);
  };
  
  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg shadow-xl w-full max-w-4xl max-h-[90vh] overflow-hidden flex flex-col">
        <div className="flex items-center justify-between border-b px-6 py-4">
          <div className="flex items-center">
            <button 
              onClick={onClose}
              className="mr-3 text-gray-500 hover:text-gray-700 focus:outline-none"
            >
              <ArrowLeft className="w-5 h-5" />
            </button>
            <h2 className="text-xl font-semibold truncate">{post.title}</h2>
          </div>
          <button 
            onClick={onClose}
            className="text-gray-500 hover:text-gray-700 focus:outline-none"
          >
            <X className="w-5 h-5" />
          </button>
        </div>
        
        <div className="flex-1 overflow-auto">
          {/* Post Content */}
          <div className="p-6 border-b">
            <div className="flex items-center mb-4">
              {/* User avatar placeholder */}
              <div className="w-10 h-10 rounded-full bg-blue-100 flex items-center justify-center mr-3 text-blue-700 font-semibold">
                {post.authorName.substring(0, 2).toUpperCase()}
              </div>
              
              <div>
                <div className="font-medium">{post.authorName}</div>
                <div className="text-sm text-gray-500">
                  {new Date(post.createdAt).toLocaleDateString('vi-VN', { 
                    day: 'numeric', month: 'long', year: 'numeric',
                    hour: '2-digit', minute: '2-digit'
                  })}
                </div>
              </div>
              
              <div className="ml-auto">
                <span className="bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded">
                  {post.channelName}
                </span>
              </div>
            </div>
            
            <div className="prose max-w-none">
              {post.content.split('\n').map((paragraph: string, idx: number) => (
                <p key={idx}>{paragraph}</p>
              ))}
            </div>
            
            {/* Post Actions */}
            <div className="mt-6 pt-4 border-t flex items-center space-x-6 text-sm">
              <button 
                onClick={handleLikePost}
                className="flex items-center text-gray-500 hover:text-blue-600 transition-colors"
                disabled={isLoading}
              >
                <ThumbsUp className="w-4 h-4 mr-1.5" />
                <span>{post.likeCount} thích</span>
              </button>
              
              <div className="flex items-center text-gray-500">
                <MessageCircle className="w-4 h-4 mr-1.5" />
                <span>{post.commentCount} bình luận</span>
              </div>
            </div>
          </div>
          
          {/* Comments Section */}
          <div className="p-6">
            <h3 className="text-lg font-semibold mb-4">Bình luận</h3>
            
            {error && (
              <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-md">
                <p className="text-sm text-red-600">{error}</p>
              </div>
            )}
            
            {/* New Comment Form */}
            <form onSubmit={handleSubmitComment} className="mb-6">
              {replyToComment && (
                <div className="mb-2 flex items-center text-sm bg-gray-50 p-2 rounded">
                  <span>
                    Đang trả lời bình luận
                  </span>
                  <button 
                    type="button" 
                    onClick={() => setReplyToComment(null)}
                    className="ml-2 text-gray-500 hover:text-gray-700"
                  >
                    <X className="w-4 h-4" />
                  </button>
                </div>
              )}
              
              <div className="flex items-start">
                {/* Current user avatar placeholder */}
                <div className="w-8 h-8 rounded-full bg-gray-200 flex items-center justify-center mr-3 flex-shrink-0">
                  <span className="text-gray-500 text-sm">Bạn</span>
                </div>
                
                <div className="relative flex-1">
                  <textarea
                    value={newComment}
                    onChange={(e) => setNewComment(e.target.value)}
                    placeholder="Viết bình luận..."
                    className="w-full border border-gray-300 rounded-md px-3 py-2 pr-10 focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"
                    rows={3}
                    disabled={isLoading}
                  ></textarea>
                  
                  <button
                    type="submit"
                    className="absolute bottom-2 right-2 text-blue-600 hover:text-blue-800 focus:outline-none"
                    disabled={!newComment.trim() || isLoading}
                  >
                    <Send className="w-5 h-5" />
                  </button>
                </div>
              </div>
            </form>
            
            {/* Comments List */}
            {parentComments.length === 0 ? (
              <div className="text-center py-6 text-gray-500">
                Chưa có bình luận nào. Hãy là người đầu tiên bình luận!
              </div>
            ) : (
              <div className="space-y-4">
                {parentComments.map((comment) => (
                  <div key={comment.id} className="border-b pb-4">
                    {/* Parent Comment */}
                    <div className="flex items-start">
                      {/* User avatar placeholder */}
                      <div className="w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center mr-3 text-blue-700 font-semibold text-sm flex-shrink-0">
                        {comment.authorName.substring(0, 2).toUpperCase()}
                      </div>
                      
                      <div className="flex-1">
                        <div className="flex items-center">
                          <span className="font-medium text-sm">{comment.authorName}</span>
                          <span className="ml-2 text-xs text-gray-500">
                            {new Date(comment.createdAt).toLocaleDateString('vi-VN', { 
                              day: 'numeric', month: 'short', hour: '2-digit', minute: '2-digit'
                            })}
                          </span>
                        </div>
                        
                        <div className="mt-1 text-sm text-gray-700">
                          {comment.content}
                        </div>
                        
                        <div className="mt-2 flex items-center space-x-4 text-xs">
                          <button 
                            onClick={() => handleLikeComment(comment.id)}
                            className="flex items-center text-gray-500 hover:text-blue-600"
                            disabled={isLoading}
                          >
                            <ThumbsUp className="w-3 h-3 mr-1" />
                            <span>{comment.likeCount}</span>
                          </button>
                          
                          <button 
                            onClick={() => setReplyToComment(comment.id)}
                            className="text-gray-500 hover:text-blue-600"
                            disabled={isLoading}
                          >
                            Trả lời
                          </button>
                        </div>
                      </div>
                    </div>
                    
                    {/* Child Comments */}
                    <div className="ml-11 mt-2 space-y-2">
                      {getChildComments(comment.id).map((childComment) => (
                        <div key={childComment.id} className="flex items-start pt-2">
                          {/* User avatar placeholder */}
                          <div className="w-6 h-6 rounded-full bg-blue-100 flex items-center justify-center mr-2 text-blue-700 font-semibold text-xs flex-shrink-0">
                            {childComment.authorName.substring(0, 2).toUpperCase()}
                          </div>
                          
                          <div className="flex-1">
                            <div className="flex items-center">
                              <span className="font-medium text-sm">{childComment.authorName}</span>
                              <span className="ml-2 text-xs text-gray-500">
                                {new Date(childComment.createdAt).toLocaleDateString('vi-VN', { 
                                  day: 'numeric', month: 'short', hour: '2-digit', minute: '2-digit'
                                })}
                              </span>
                            </div>
                            
                            <div className="mt-1 text-sm text-gray-700">
                              {childComment.content}
                            </div>
                            
                            <div className="mt-1 flex items-center space-x-4 text-xs">
                              <button 
                                onClick={() => handleLikeComment(childComment.id)}
                                className="flex items-center text-gray-500 hover:text-blue-600"
                                disabled={isLoading}
                              >
                                <ThumbsUp className="w-3 h-3 mr-1" />
                                <span>{childComment.likeCount}</span>
                              </button>
                            </div>
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default PostDetail; 