import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Table from 'react-bootstrap/Table';
import { Button, Alert } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

const GetAllPublisher = () => {
   const navigate = useNavigate();
  const [publishers, setPublishers] = useState([]);
  const [message, setMessage] = useState('');

  useEffect(() => {
    fetchPublishers();
  }, []);

  const fetchPublishers = async () => {
    try {
      const token = localStorage.getItem('token');
      if (!token || localStorage.getItem('userType') !== 'ADMIN') {
        console.error('No token found. Please log in.');
        navigate('/login-Admin');
        return;
      }

      const response = await axios.get('http://localhost:8080/admin/getPublisher', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setPublishers(response.data);
    } catch (error) {
      console.error('Failed to fetch publishers:', error);
    }
  };

  const handleDelete = async (id) => {
    try {
      const token = localStorage.getItem('token');
      if (!token) {
        console.error('No token found. Please log in.');
        return;
      }
       console.log("token",token,"id",id)
      const response = await axios.delete(`http://localhost:8080/admin/deleteById/${id}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setMessage(response.data);
      fetchPublishers();
    } catch (error) {
      setMessage(`Failed to delete user with ID: ${id}`);
      console.error('Delete error:', error);
    }
  };

  return (
    <div className="container mt-4">
      <h2 className="mb-4">All Publishers</h2>
      {message && <Alert variant="info">{message}</Alert>}
      <Table striped bordered hover responsive>
        <thead className="table-primary">
          <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Email</th>
            <th>Role</th>
            <th>Created At</th>
            <th>Updated At</th>
            <th>Banned</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {publishers.length > 0 ? (
            publishers.map((publisher) => (
              <tr key={publisher.id}>
                <td>{publisher.id}</td>
                <td>{publisher.username}</td>
                <td>{publisher.email || 'N/A'}</td>
                <td>{publisher.role}</td>
                <td>{new Date(publisher.createdAt).toLocaleString()}</td>
                <td>{new Date(publisher.updatedAt).toLocaleString()}</td>
                <td>{publisher.banned ? 'Yes' : 'No'}</td>
                <td>
                  <Button 
                    variant="danger" 
                    onClick={() => handleDelete(publisher.id)}
                  >
                    Delete
                  </Button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="8" className="text-center">
                No publishers found.
              </td>
            </tr>
          )}
        </tbody>
      </Table>
    </div>
  );
};

export default GetAllPublisher;
