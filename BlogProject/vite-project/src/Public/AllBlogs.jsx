import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import axios from 'axios';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const AllBlogs = () => {
    const [blogs, setBlogs] = useState([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [showComments, setShowComments] = useState({});
    const [commentText, setCommentText] = useState({});
    const blogsPerPage = 8;
    const navigate = useNavigate();
    const token = localStorage.getItem('token');

    const fetchBlogs = () => {
        axios.get('http://localhost:8080/Public/blogs')
            .then(response => setBlogs(response.data))
            .catch(error => console.error('Error fetching blogs:', error));
    };

    useEffect(() => {
        fetchBlogs();
    }, []);

    const toggleComments = (blogId) => {
        setShowComments((prev) => ({ ...prev, [blogId]: !prev[blogId] }));
    };

    const handleCommentChange = (blogId, value) => {
        setCommentText((prev) => ({ ...prev, [blogId]: value }));
    };

    const handleLike = async (blogId) => {
        if (!token || localStorage.getItem('userType') !== 'USER') {
            return navigate('/LoginUser');
        }
        
        try {
            const response = await axios.put(`http://localhost:8080/user/toggleLike/${blogId}`, {}, {
                headers: { Authorization: `Bearer ${token}` }
            });
            toast.success(response.data);
            fetchBlogs();
        } catch (error) {
            toast.error('Error toggling like');
            console.error('Error toggling like:', error);
        }
    };

    const handleAddComment = async (blogId, comment) => {
        if (!token) return navigate('/LoginUser');
        try {
            await axios.post('http://localhost:8080/user/addComment', null, {
                params: { blogId, commentText: comment },
                headers: { Authorization: `Bearer ${token}` }
            });
            toast.success('Comment added successfully!');
            setCommentText((prev) => ({ ...prev, [blogId]: '' }));
            fetchBlogs();
        } catch (error) {
            toast.error('Error adding comment');
            console.error('Error adding comment:', error);
        }
    };

    const indexOfLastBlog = currentPage * blogsPerPage;
    const indexOfFirstBlog = indexOfLastBlog - blogsPerPage;
    const currentBlogs = blogs.slice(indexOfFirstBlog, indexOfLastBlog);

    const paginate = (pageNumber) => setCurrentPage(pageNumber);

    return (
        <div className="container mt-4">
            <ToastContainer />
            <div className="d-flex flex-wrap gap-2 justify-content-start">
                {currentBlogs.map((blog) => (
                    <Card key={blog.id} style={{ width: 'calc(25% - 0.5rem)', minWidth: '250px', height: '100%' }} className="d-flex flex-column">
                        <Card.Img 
                            variant="top" 
                            src={blog.imageUrl || 'https://via.placeholder.com/300'} 
                            alt="Blog image" 
                            style={{ objectFit: 'cover', height: '200px' }}
                        />
                        <Card.Body className="d-flex flex-column">
                            <Card.Title>{blog.title || 'No Title'}</Card.Title>
                            <Card.Text style={{ flexGrow: 1 }}>
                                {blog.description || 'No description available.'}
                            </Card.Text>
                            <div className="d-flex justify-content-between">
                                <Button onClick={() => handleLike(blog.id)}>
                                    Like ({blog.likeCount || 0})
                                </Button>
                                <Button onClick={() => toggleComments(blog.id)} variant="secondary">
                                    {showComments[blog.id] ? 'Hide Comments' : `View Comments (${blog.commentCount || 0})`}
                                </Button>
                            </div>
                            {showComments[blog.id] && (
                                <div className="mt-3">
                                    <h6>Comments</h6>
                                    {blog.comments.length > 0 ? (
                                        blog.comments.map((comment, index) => (
                                            <p key={index} className="border p-2 rounded">
                                                {comment}
                                            </p>
                                        ))
                                    ) : (
                                        <p>No comments yet</p>
                                    )}
                                </div>
                            )}
                            <div className="mt-3">
                                <input
                                    type="text"
                                    placeholder="Add a comment..."
                                    value={commentText[blog.id] || ''}
                                    onChange={(e) => handleCommentChange(blog.id, e.target.value)}
                                    onKeyDown={(e) => {
                                        if (e.key === 'Enter' && commentText[blog.id]?.trim()) {
                                            handleAddComment(blog.id, commentText[blog.id]);
                                        }
                                    }}
                                />
                            </div>
                        </Card.Body>
                    </Card>
                ))}
            </div>
            <div className="pagination mt-4">
                {Array.from({ length: Math.ceil(blogs.length / blogsPerPage) }, (_, index) => (
                    <Button key={index + 1} onClick={() => paginate(index + 1)} className="me-2">
                        {index + 1}
                    </Button>
                ))}
            </div>
        </div>
    );
}

export default AllBlogs;

