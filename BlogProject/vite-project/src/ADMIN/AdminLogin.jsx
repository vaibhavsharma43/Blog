import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Card from 'react-bootstrap/Card';
import axios from 'axios';

const AdminLogin = () => {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    username: '',
    password: ''
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      console.log(formData);
      const response = await axios.post("http://localhost:8080/Test/adminlogin", formData, {
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (response.data) {
        const token = response.data;
        window.localStorage.setItem("token", token);
        window.localStorage.setItem("userType", "ADMIN");

        console.log("Token:", token);
        alert("Admin Login successful!");

        navigate('/admin');
      }
    } catch (e) {
      console.error("Login failed:", e);
      alert("Login failed. Please check your credentials.");
    }
  };

  return (
    <Container fluid className="vh-100 d-flex justify-content-center align-items-center" style={{ background: 'linear-gradient(135deg, #74EBD5 0%, #9FACE6 100%)' }}>
      <Row className="w-100">
        <Col md={{ span: 6, offset: 3 }} lg={{ span: 4, offset: 4 }}>
          <Card className="p-4 shadow-lg" style={{ borderRadius: '20px', background: '#ffffff' }}>
            <Card.Body>
              <h2 className="text-center mb-4" style={{ color: '#4CAF50' }}>Admin Login</h2>
              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3" controlId="formBasicEmail">
                  <Form.Label>Username</Form.Label>
                  <Form.Control 
                    type="text" 
                    placeholder="Enter the username" 
                    name="username" 
                    value={formData.username} 
                    onChange={handleChange} 
                  />
                </Form.Group>

                <Form.Group className="mb-3" controlId="formBasicPassword">
                  <Form.Label>Password</Form.Label>
                  <Form.Control 
                    type="password" 
                    placeholder="Password" 
                    name="password" 
                    value={formData.password} 
                    onChange={handleChange} 
                  />
                </Form.Group>

                <Button 
                  variant="success" 
                  type="submit" 
                  className="w-100"
                  style={{ boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)', borderRadius: '10px' }}
                >
                  Login
                </Button>
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
}

export default AdminLogin;
