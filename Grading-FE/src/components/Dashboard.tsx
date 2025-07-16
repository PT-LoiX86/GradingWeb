import React from 'react';
import { 
  BarChart3, 
  Users, 
  BookOpen, 
  Award, 
  TrendingUp, 
  CheckCircle, 
  Clock, 
  AlertCircle,
  Calendar,
  FileText,
  PieChart
} from 'lucide-react';
import PageContainer from './PageContainer';

const Dashboard: React.FC = () => {
  return (
    <PageContainer>
      {/* Hero Section */}
      <div className="bg-gradient-to-r from-blue-600 to-blue-800 text-white rounded-lg mb-8">
        <div className="px-8 py-12">
          <div className="text-center">
            <h1 className="text-4xl font-bold mb-4">Welcome to Grading</h1>
            <p className="text-xl text-blue-100 mb-8">Your comprehensive grading and assessment platform</p>
            <div className="flex justify-center space-x-4">
              <button className="bg-white text-blue-600 px-6 py-3 rounded-lg font-semibold hover:bg-gray-100 transition-colors">
                Get Started
              </button>
              <button className="border border-white text-white px-6 py-3 rounded-lg font-semibold hover:bg-white hover:text-blue-600 transition-colors">
                Learn More
              </button>
            </div>
          </div>
        </div>
      </div>

      {/* Stats Overview */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
            <div className="flex items-center">
              <div className="p-3 bg-blue-100 rounded-lg">
                <Users className="w-6 h-6 text-blue-600" />
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Total Students</p>
                <p className="text-2xl font-semibold text-gray-900">1,234</p>
              </div>
            </div>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
            <div className="flex items-center">
              <div className="p-3 bg-green-100 rounded-lg">
                <BookOpen className="w-6 h-6 text-green-600" />
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Active Courses</p>
                <p className="text-2xl font-semibold text-gray-900">24</p>
              </div>
            </div>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
            <div className="flex items-center">
              <div className="p-3 bg-yellow-100 rounded-lg">
                <Award className="w-6 h-6 text-yellow-600" />
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Assignments</p>
                <p className="text-2xl font-semibold text-gray-900">156</p>
              </div>
            </div>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
            <div className="flex items-center">
              <div className="p-3 bg-purple-100 rounded-lg">
                <TrendingUp className="w-6 h-6 text-purple-600" />
              </div>
              <div className="ml-4">
                <p className="text-sm font-medium text-gray-600">Avg. Grade</p>
                <p className="text-2xl font-semibold text-gray-900">85.2%</p>
              </div>
            </div>
          </div>
        </div>

        {/* Main Content Grid */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Recent Activities */}
          <div className="lg:col-span-2 bg-white rounded-lg shadow-sm border border-gray-200">
            <div className="p-6 border-b border-gray-200">
              <h2 className="text-lg font-semibold text-gray-900">Recent Activities</h2>
            </div>
            <div className="p-6">
              <div className="space-y-4">
                <div className="flex items-center space-x-4">
                  <div className="p-2 bg-green-100 rounded-full">
                    <CheckCircle className="w-4 h-4 text-green-600" />
                  </div>
                  <div className="flex-1">
                    <p className="text-sm text-gray-900">Assignment "React Components" graded</p>
                    <p className="text-xs text-gray-500">2 hours ago</p>
                  </div>
                </div>
                
                <div className="flex items-center space-x-4">
                  <div className="p-2 bg-blue-100 rounded-full">
                    <FileText className="w-4 h-4 text-blue-600" />
                  </div>
                  <div className="flex-1">
                    <p className="text-sm text-gray-900">New submission for "Database Design"</p>
                    <p className="text-xs text-gray-500">4 hours ago</p>
                  </div>
                </div>
                
                <div className="flex items-center space-x-4">
                  <div className="p-2 bg-yellow-100 rounded-full">
                    <Clock className="w-4 h-4 text-yellow-600" />
                  </div>
                  <div className="flex-1">
                    <p className="text-sm text-gray-900">Deadline reminder: "Final Project"</p>
                    <p className="text-xs text-gray-500">6 hours ago</p>
                  </div>
                </div>
                
                <div className="flex items-center space-x-4">
                  <div className="p-2 bg-red-100 rounded-full">
                    <AlertCircle className="w-4 h-4 text-red-600" />
                  </div>
                  <div className="flex-1">
                    <p className="text-sm text-gray-900">Late submission: "JavaScript Basics"</p>
                    <p className="text-xs text-gray-500">8 hours ago</p>
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* Quick Actions */}
          <div className="space-y-6">
            <div className="bg-white rounded-lg shadow-sm border border-gray-200">
              <div className="p-6 border-b border-gray-200">
                <h2 className="text-lg font-semibold text-gray-900">Quick Actions</h2>
              </div>
              <div className="p-6">
                <div className="space-y-3">
                  <button className="w-full text-left p-3 rounded-lg hover:bg-gray-50 transition-colors flex items-center space-x-3">
                    <FileText className="w-5 h-5 text-blue-600" />
                    <span className="text-sm text-gray-700">Create Assignment</span>
                  </button>
                  <button className="w-full text-left p-3 rounded-lg hover:bg-gray-50 transition-colors flex items-center space-x-3">
                    <BarChart3 className="w-5 h-5 text-green-600" />
                    <span className="text-sm text-gray-700">View Analytics</span>
                  </button>
                  <button className="w-full text-left p-3 rounded-lg hover:bg-gray-50 transition-colors flex items-center space-x-3">
                    <Users className="w-5 h-5 text-purple-600" />
                    <span className="text-sm text-gray-700">Manage Students</span>
                  </button>
                  <button className="w-full text-left p-3 rounded-lg hover:bg-gray-50 transition-colors flex items-center space-x-3">
                    <Calendar className="w-5 h-5 text-yellow-600" />
                    <span className="text-sm text-gray-700">Schedule Exam</span>
                  </button>
                </div>
              </div>
            </div>

            {/* Upcoming Deadlines */}
            <div className="bg-white rounded-lg shadow-sm border border-gray-200">
              <div className="p-6 border-b border-gray-200">
                <h2 className="text-lg font-semibold text-gray-900">Upcoming Deadlines</h2>
              </div>
              <div className="p-6">
                <div className="space-y-4">
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="text-sm font-medium text-gray-900">Final Project</p>
                      <p className="text-xs text-gray-500">Computer Science</p>
                    </div>
                    <div className="text-right">
                      <p className="text-sm text-red-600">2 days</p>
                    </div>
                  </div>
                  
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="text-sm font-medium text-gray-900">Midterm Exam</p>
                      <p className="text-xs text-gray-500">Mathematics</p>
                    </div>
                    <div className="text-right">
                      <p className="text-sm text-yellow-600">5 days</p>
                    </div>
                  </div>
                  
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="text-sm font-medium text-gray-900">Lab Report</p>
                      <p className="text-xs text-gray-500">Physics</p>
                    </div>
                    <div className="text-right">
                      <p className="text-sm text-green-600">1 week</p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Charts Section */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 mt-8">
          <div className="bg-white rounded-lg shadow-sm border border-gray-200">
            <div className="p-6 border-b border-gray-200">
              <h2 className="text-lg font-semibold text-gray-900">Grade Distribution</h2>
            </div>
            <div className="p-6">
              <div className="flex items-center justify-center h-64">
                <div className="text-center">
                  <PieChart className="w-16 h-16 text-gray-400 mx-auto mb-4" />
                  <p className="text-gray-500">Chart visualization would go here</p>
                </div>
              </div>
            </div>
          </div>

          <div className="bg-white rounded-lg shadow-sm border border-gray-200">
            <div className="p-6 border-b border-gray-200">
              <h2 className="text-lg font-semibold text-gray-900">Performance Trends</h2>
            </div>
            <div className="p-6">
              <div className="flex items-center justify-center h-64">
                <div className="text-center">
                  <BarChart3 className="w-16 h-16 text-gray-400 mx-auto mb-4" />
                  <p className="text-gray-500">Chart visualization would go here</p>
                </div>
              </div>
            </div>
          </div>
        </div>
    </PageContainer>
  );
};

export default Dashboard;
