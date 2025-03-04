import { useState } from 'react';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Button from 'react-bootstrap/Button';
import Container from 'react-bootstrap/Container';
import Card from 'react-bootstrap/Card';
import { useNavigate } from 'react-router-dom';

function CreateUser() {
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
    console.log(formData)

    if (formData.password.length <= 8) {
      setPasswordError('Password must be greater than 8 characters');
    } else {
      setPasswordError('');
      setFormError('');
      try {
        const response = await fetch('http://localhost:8080/Public/createUser', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(formData),
        });

        const responseBody = await response.text();
        if (response.status === 200) {
          setApiResponse('User created successfully!');
          alert('User created successfully!');
          navigate('/LoginUser');
        } else if (response.status === 400 && responseBody.includes('User already exists')) {
          setApiResponse('User already exists!');
        } else {
          setApiResponse('An error occurred. Please try again.');
        }
      } catch (error) {
        setApiResponse('Failed to connect to the server.');
      }
    }
  };

  return (
    <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: '100vh' }}>
      <Card className="shadow-lg p-4" style={{ width: '100%', maxWidth: '600px', borderRadius: '20px' }}>
        <Card.Title className="text-center mb-4">Create User</Card.Title>
        <Form onSubmit={handleSubmit}>
          {formError && <p className="text-danger">{formError}</p>}
          {apiResponse && <p className={`text-${apiResponse.includes('exists') ? 'danger' : 'success'}`}>{apiResponse}</p>}

          <Form.Group className="mb-3" controlId="formUsername">
            <Form.Label>Username</Form.Label>
            <Form.Control
              type="text"
              name="username"
              placeholder="Enter username"
              value={formData.username}
              onChange={handleChange}
            />
          </Form.Group>

          <Form.Group className="mb-3" controlId="formEmail">
            <Form.Label>Email</Form.Label>
            <Form.Control
              type="email"
              name="email"
              placeholder="Enter email"
              value={formData.email}
              onChange={handleChange}
            />
          </Form.Group>

          <Form.Group className="mb-3" controlId="formPassword">
            <Form.Label>Password</Form.Label>
            <Form.Control
              type="password"
              name="password"
              placeholder="Password (min 8 characters)"
              value={formData.password}
              onChange={handleChange}
            />
            {passwordError && <p className="text-danger mt-2">{passwordError}</p>}
          </Form.Group>

          <Button variant="primary" type="submit" className="w-100">
            Submit
          </Button>
        </Form>
      </Card>
    </Container>
  );
}

export default CreateUser;
