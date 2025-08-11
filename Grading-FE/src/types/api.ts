// API types based on backend DTOs
export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
  email: string;
  fullName: string;
}

export interface UserResponse {
  id: number;
  username: string;
  email: string;
  fullName: string;
  role: string;
  avatarUrl?: string;
  isActive: boolean;
}

export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number;
  user: UserResponse;
}

export interface RefreshTokenRequest {
  refreshToken: string;
}

export interface ForgotPasswordRequest {
  email: string;
}

export interface ResetPasswordRequest {
  token: string;
  newPassword: string;
}

export interface ApiResponse<T> {
  data?: T;
  message?: string;
  success: boolean;
}

export interface ApiError {
  message: string;
  statusCode: number;
  timestamp: string;
  path: string;
}

// Backend error response types
export interface ErrorResponse {
  apiPath: string;
  errorCode: string;
  errorMessage: string;
  errorTime: string;
}

export interface ValidationErrorResponse {
  apiPath: string;
  errorCode: string;
  errorMessage: string;
  validationErrors: Record<string, string>;
  errorTime: string;
}

// Forum types
export interface ForumChannel {
  id: number;
  name: string;
  description: string;
  color: string;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface ForumPost {
  id: number;
  title: string;
  content: string;
  authorId: number;
  authorName: string;
  channelId: number;
  channelName: string;
  likeCount: number;
  commentCount: number;
  createdAt: string;
  updatedAt: string;
  isLiked?: boolean;
}

export interface ForumComment {
  id: number;
  content: string;
  authorId: number;
  authorName: string;
  postId: number;
  parentId?: number;
  likeCount: number;
  createdAt: string;
  updatedAt: string;
  isLiked?: boolean;
  replies?: ForumComment[];
}

export interface CreatePostRequest {
  title: string;
  content: string;
  channelId: number;
}

export interface CreateCommentRequest {
  content: string;
  postId: number;
  parentId?: number;
}

export interface PaginationParams {
  page: number;
  size: number;
  sort?: string;
  direction?: 'asc' | 'desc';
  search?: string;
  channelId?: number;
}
