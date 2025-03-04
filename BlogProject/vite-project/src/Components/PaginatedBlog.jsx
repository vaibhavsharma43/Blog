import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Container, Row, Col, Card, Pagination, Alert } from 'react-bootstrap';
import "./page.css";
import { useNavigate } from 'react-router-dom';

const PaginatedBlog = () => {
  const navigate = useNavigate();
  const [blogData, setBlogData] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [error, setError] = useState(null);
  const itemsPerPage = 6;

  // Fetch blog data from the API
  useEffect(() => {
    const fetchBlogs = async () => {
      const token = localStorage.getItem('token');
      
      if (!token || localStorage.getItem('userType') !== 'PUBLISHER') {
        setError('No token found. Please log in.');
        navigate('/login-publisher');
        return;
      }

      try {
        const response = await axios.get('http://localhost:8080/Publisher/myBlogs', {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json',
          },
        });

        console.log(response);
        
        if (response.data && response.data.length > 0) {
          setBlogData(response.data);
        } else {
          setBlogData([]);
          setError('No content available.');
        }
      } catch (err) {
        setError('Failed to fetch blog data. Please check your API or token.');
        console.error('API fetch error:', err);
      }
    };

    fetchBlogs();
  }, [navigate]);

  // Pagination logic
  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentItems = blogData.slice(indexOfFirstItem, indexOfLastItem);
  const totalPages = Math.ceil(blogData.length / itemsPerPage);

  const handlePageChange = (pageNumber) => setCurrentPage(pageNumber);

  return (
    <Container>
      {error && <Alert variant="danger">{error}</Alert>}
      {!error && blogData.length === 0 && (
        <Alert variant="info">No content available.</Alert>
      )}
      <Row>
        {currentItems.map((item) => {
          console.log(item);
          return (
            <Col md={4} key={item.id} className="mb-4">
              <Card>
                <Card.Img 
                  variant="top" 
                  src={item.imageUrl || "http://helpx.adobe.com/content/dam/help/en/photoshop/using/convert-color-image-black-white/jcr_content/main-pars/before_and_after/image-before/Landscape-Color.jpg"} 
                />
                <Card.Body>
                  <Card.Title>{item.admin?.username || 'Unknown Publisher'}</Card.Title>
                  <Card.Text>{item.description || 'No description available'}</Card.Text>
                </Card.Body>
              </Card>
            </Col>
          );
        })}
      </Row>

      {blogData.length > 0 && (
        <Pagination className="justify-content-center">
          <Pagination.Prev
            onClick={() => handlePageChange(currentPage - 1)}
            disabled={currentPage === 1}
          />
          {Array.from({ length: totalPages }).map((_, index) => (
            <Pagination.Item
              key={index + 1}
              active={index + 1 === currentPage}
              onClick={() => handlePageChange(index + 1)}
            >
              {index + 1}
            </Pagination.Item>
          ))}
          <Pagination.Next
            onClick={() => handlePageChange(currentPage + 1)}
            disabled={currentPage === totalPages}
          />
        </Pagination>
      )}
    </Container>
  );
};

export default PaginatedBlog;
