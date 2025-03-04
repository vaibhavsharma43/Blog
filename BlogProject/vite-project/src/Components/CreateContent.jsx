import React, { useState } from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';


const CreateContent = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
      title: '',      
      imageUrl: '',
      description: ''
    });
    


  const [error, setError] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSubmit = async (e) => {

    e.preventDefault();
    let token = localStorage.getItem('token');
    // Validation: Check if all fields are filled
    if (!formData.title || !formData.imageUrl || !formData.description) {
      console.log(formData)
      setError('All fields are required.');
      return;
    }

      if (!token) {
        setError('No token found. Please log in.');
        navigate('/login-publisher');
        return;
      }
    console.log('Form Data:', formData);
    try {
       // Define your token here
      const response = await axios.post('http://localhost:8080/Publisher/createBlog', formData, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json',
        },
      });
      if (response.status === 200) {
        alert(response.data);
      }
    } catch (err) {
      setError('Blog creation failed.');
      console.error('API fetch error:', err);
    }
  };

  return (
    <Form onSubmit={handleSubmit}>
      <Form.Group className="mb-3" controlId="formBasicTitle">
        <Form.Label>Title</Form.Label>
        <Form.Control 
          type="text" 
          placeholder="Enter title" 
          name="title" 
          value={formData.title} 
          onChange={handleChange} 
        />
      </Form.Group>

      <Form.Group className="mb-3" controlId="formBasicImageUrl">
        <Form.Label>Image URL</Form.Label>
        <Form.Control 
          type="text" 
          placeholder="Enter image URL" 
          name="imageUrl" 
          value={formData.imageUrl} 
          onChange={handleChange} 
        />
      </Form.Group>

      <Form.Group className="mb-3" controlId="formBasicDescription">
        <Form.Label>Description</Form.Label>
        <Form.Control 
          as="textarea" 
          rows={3} 
          placeholder="Enter description" 
          name="description" 
          value={formData.description} 
          onChange={handleChange} 
        />
      </Form.Group>

      <Button variant="primary" type="submit">
        Submit
      </Button>
      {error && <p style={{ color: 'red' }}>{error}</p>}
    </Form>
  );
};

export default CreateContent;