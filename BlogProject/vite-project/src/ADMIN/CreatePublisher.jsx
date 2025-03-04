import { useState } from 'react';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Button from 'react-bootstrap/Button';
import { useNavigate } from 'react-router-dom';

function CreatePublisher() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
  });

  const [passwordError, setPasswordError] = useState('');
  const [formError, setFormError] = useState('');
  const [apiResponse, setApiResponse] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.username || !formData.email || !formData.password) {
      setFormError('All fields are required');
      return;
    }
    
    if (formData.password.length <= 8) {
      setPasswordError('Password must be greater than 8 characters');
    } else {
      setPasswordError('');
      setFormError('');
      try {
        const token = localStorage.getItem('token');
        if (!token || localStorage.getItem('userType') !== 'ADMIN') {
            setError('No token found. Please log in.');
            navigate('/login-Admin');
            return;
          }
        const response = await fetch('http://localhost:8080/admin/createPublisher', {
          method: 'POST',
          headers: {
            Authorization: `Bearer ${token}`,
          

            'Content-Type': 'application/json'
            
          },
          body: JSON.stringify(formData),
        });
        
        const responseBody = await response.text();
        if (response.status === 200) {
          setApiResponse(responseBody);
          alert('Publisher created successfully!');
        } else if (response.status === 400 && responseBody.includes('Publisher already exists')) {
          setApiResponse('Publisher already exists!');
        } else {
          setApiResponse('An error occurred. Please try again.');
        }
      } catch (error) {
        setApiResponse('Failed to connect to the server.');
      }
    }
  };

  return (
    <Form onSubmit={handleSubmit}>
      {formError && <p style={{ color: 'red' }}>{formError}</p>}
      {apiResponse && <p style={{ color: apiResponse.includes('exists') ? 'red' : 'green' }}>{apiResponse}</p>}
      
      <Form.Group as={Row} className="mb-3" controlId="formPlaintextUsername">
        <Form.Label column sm="2">
          Username
        </Form.Label>
        <Col sm="10">
          <Form.Control
            type="text"
            name="username"
            placeholder="Enter username (required)"
            value={formData.username}
            onChange={handleChange}
          />
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3" controlId="formPlaintextEmail">
        <Form.Label column sm="2">
          Email
        </Form.Label>
        <Col sm="10">
          <Form.Control
            type="email"
            name="email"
            placeholder="Enter email (required)"
            value={formData.email}
            onChange={handleChange}
          />
        </Col>
      </Form.Group>

      <Form.Group as={Row} className="mb-3" controlId="formPlaintextPassword">
        <Form.Label column sm="2">
          Password
        </Form.Label>
        <Col sm="10">
          <Form.Control
            type="password"
            name="password"
            placeholder="Password (min 8 characters, required)"
            value={formData.password}
            onChange={handleChange}
          />
          {passwordError && <p style={{ color: 'red' }}>{passwordError}</p>}
        </Col>
      </Form.Group>

      <Button variant="primary" type="submit">
        Submit
      </Button>
    </Form>
  );
}

export default CreatePublisher;
