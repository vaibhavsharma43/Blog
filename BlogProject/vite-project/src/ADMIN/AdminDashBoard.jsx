import React, { useEffect } from 'react';
import { useNavigate, Link, Outlet } from 'react-router-dom';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

const AdminDashBoard = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');
    const userType = localStorage.getItem('userType');
    if (!token || userType !== 'ADMIN') {
      navigate('/login-Admin');
    }
  }, [navigate]);

  return (
    <div className="bg-light text-dark min-vh-100 d-flex">
      <Container fluid className="p-0">
        <Row className="g-0">
          {/* Sidebar */}
          <Col xs={3} className="bg-white border-end shadow-sm min-vh-100">
            <div className="p-3">
              <div className="mb-4">
                <h4 className="fw-bold text-primary">ADMIN PANEL</h4>
              </div>
              <ul className="nav flex-column">
                <li className="nav-item">
                  <Link className="nav-link text-dark fw-semibold" to="/admin">
                    📊 Dashboard
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link text-dark fw-semibold" to="/admin/GetAllPublisher">
                    📚 All Publishers
                  </Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link text-dark fw-semibold" to="/admin/CreatePublisher">
                    ✍️ Create Publisher
                  </Link>
                </li>
              </ul>
            </div>
          </Col>

          {/* Main Content */}
          <Col xs={9} className="p-4 d-flex flex-column">
            <div className="bg-white p-4 rounded shadow-sm flex-grow-1">
              <Outlet />
            </div>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default AdminDashBoard;
