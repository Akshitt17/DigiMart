import React from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { Navbar, Nav, Container, Button } from "react-bootstrap";
import { LinkContainer } from "react-router-bootstrap";
import "./AppNavbar.css";

const AppNavbar = () => {
  const { isAuthenticated, user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <Navbar expand="lg" className="custom-navbar shadow-sm" sticky="top">
      <Container>
        <LinkContainer to="/">
          <Navbar.Brand className="brand-text">🏠 Smart Home</Navbar.Brand>
        </LinkContainer>

        <Navbar.Toggle
          aria-controls="basic-navbar-nav"
          className="custom-toggler"
        />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="ms-auto align-items-center">
            {/* Authenticated users */}
            {isAuthenticated ? (
              <>
                {user?.role === "CUSTOMER" && (
                  <>
                    <LinkContainer to="/my-orders">
                      <Nav.Link className="nav-link-custom">My Orders</Nav.Link>
                    </LinkContainer>
                    <LinkContainer to="/cart">
                      <Nav.Link className="nav-link-custom">Cart</Nav.Link>
                    </LinkContainer>
                    <LinkContainer to="/profile">
                      <Nav.Link className="nav-link-custom">
                        My Profile
                      </Nav.Link>
                    </LinkContainer>
                  </>
                )}
                {user?.role === "ADMIN" && (
                  <LinkContainer to="/admin/dashboard">
                    <Nav.Link className="nav-link-custom">
                      Admin Dashboard
                    </Nav.Link>
                  </LinkContainer>
                )}

                <Button onClick={handleLogout} className="btn-auth logout-btn">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="16"
                    height="16"
                    fill="currentColor"
                    className="bi bi-box-arrow-right"
                    viewBox="0 0 16 16"
                  >
                    <path
                      fillRule="evenodd"
                      d="M10 12a.5.5 
      0 0 1 .5.5v3a.5.5 
      0 0 1-1 0v-3a.5.5 0 0 
      1 .5-.5zM10 1a.5.5 0 0 
      1 .5.5v3a.5.5 0 0 
      1-1 0v-3A.5.5 0 0 1 10 1z"
                    />
                    <path
                      fillRule="evenodd"
                      d="M5.146 7.646a.5.5 
      0 0 1 .708 0L9 10.793V1.207l-3.146 
      3.147a.5.5 0 0 1-.708-.708l4-4a.5.5 
      0 0 1 .708 0l4 4a.5.5 0 0 1-.708.708L10 1.207v9.586l3.146-3.147a.5.5 
      0 0 1 .708.708l-4 4a.5.5 
      0 0 1-.708 0l-4-4a.5.5 
      0 0 1 0-.708z"
                    />
                  </svg>
                  Logout
                </Button>
              </>
            ) : (
              // Guest users
              <>
                <LinkContainer to="/login">
                  <Button className="btn-auth login-btn">
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="16"
                      height="16"
                      fill="currentColor"
                      className="bi bi-box-arrow-in-right"
                      viewBox="0 0 16 16"
                    >
                      <path
                        fillRule="evenodd"
                        d="M6 3a1 1 0 0 1 1-1h6.5A1.5 1.5 0 0 1 15 3.5v9a1.5 
        1.5 0 0 1-1.5 1.5H7a1 1 0 0 1-1-1v-1h1v1h6.5A.5.5 0 0 0 
        14 12.5v-9a.5.5 0 0 0-.5-.5H7v1H6V3z"
                      />
                      <path
                        fillRule="evenodd"
                        d="M.146 8.354a.5.5 0 0 1 0-.708l3-3a.5.5 
        0 1 1 .708.708L1.707 7.5H9.5a.5.5 0 0 
        1 0 1H1.707l2.147 2.146a.5.5 0 0 
        1-.708.708l-3-3z"
                      />
                    </svg>
                    Login
                  </Button>
                </LinkContainer>

                <LinkContainer to="/register">
                  <Button className="btn-auth register-btn">
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      width="16"
                      height="16"
                      fill="currentColor"
                      className="bi bi-person-plus"
                      viewBox="0 0 16 16"
                    >
                      <path
                        d="M8 7a3 3 0 1 0 0-6 3 3 0 0 
        0 0 6zM2 13s-1 0-1-1 1-4 7-4 
        7 3 7 4-1 1-1 1H2z"
                      />
                      <path
                        fillRule="evenodd"
                        d="M13.5 5a.5.5 0 0 
        1 .5.5v1.5H15a.5.5 0 0 
        1 0 1h-1v1.5a.5.5 0 0 
        1-1 0V8h-1a.5.5 0 0 
        1 0-1h1V5.5a.5.5 0 0 1 .5-.5z"
                      />
                    </svg>
                    Register
                  </Button>
                </LinkContainer>
              </>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default AppNavbar;
