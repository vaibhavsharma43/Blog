import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import NavBar from './NavBar';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import { Link, Outlet } from 'react-router-dom';

const DashBoard = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');
    const userType = localStorage.getItem('userType');

    if (!token || userType !== 'PUBLISHER') {
      navigate('/login-publisher');
    }
  }, [navigate]);

  return (
    <div className="d-flex flex-column vh-100">
      {/* <NavBar /> */}
      <Container fluid className="d-flex flex-grow-1 p-0">
        <Row className="g-0 w-100 flex-grow-1">
          <Col xs={12} md={3} className="bg-dark border-end p-3 vh-100">
            <div>
              <div className="mb-4">
                <h4 className="fw-bold">Publisher Dash-Board</h4>
              </div>
              <ul className="nav flex-column">
                <li className="nav-item">
                  <Link className="nav-link active text-light" to="/">
                    <i className="bi bi-speedometer me-2"></i> Dashboard
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link text-light" to="/all-content">
                    <i className="bi bi-bell me-2"></i> ALL Content
                    <span className="badge bg-primary ms-2">New</span>
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link text-light" to="/create-content">
                    <i className="bi bi-puzzle me-2"></i> Create Content
                  </Link>
                </li>
              </ul>
            </div>
          </Col>
          <Col xs={12} md={9} className="p-4 vh-100 overflow-auto">
            <Outlet /> {/* Dynamic Content Renders Here */}
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default DashBoard;
