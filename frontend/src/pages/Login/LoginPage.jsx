import React, { useState } from "react";
import { useAuth } from "../../context/AuthContext";
import { useNavigate, useLocation } from "react-router-dom";
import { Alert, Button, Card, Form } from "react-bootstrap";
import "./LoginPage.css"; // custom styles

const LoginPage = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const from = location.state?.from?.pathname || "/";

  // const handleSubmit = async (e) => {
  //   e.preventDefault();
  //   setError("");
  //   setLoading(true);
  //   try {
  //     await login(username, password);
  //     navigate(from, { replace: true });
  //   } catch (err) {
  //     setError(
  //       err.message || "Failed to log in. Please check your credentials."
  //     );
  //   } finally {
  //     setLoading(false);
  //   }
  // };
  // In src/pages/LoginPage.jsx

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      // The login function returns the user data upon success
      const userData = await login(username, password);

      // Check the user's role and navigate accordingly
      if (userData.role === "ADMIN") {
        navigate("/admin/dashboard", { replace: true });
      } else {
        // For customers, redirect to the page they were trying to access or the homepage
        const from = location.state?.from?.pathname || "/";
        navigate(from, { replace: true });
      }
    } catch (err) {
      setError(
        err.message || "Failed to log in. Please check your credentials."
      );
    } finally {
      setLoading(false);
    }
  };
  return (
    <div className="login-page-container">
      <Card className="login-card shadow-lg">
        <Card.Body>
          <Card.Title className="text-center mb-4 fw-bold fs-3">
            🔐 Login
          </Card.Title>
          {error && <Alert variant="danger">{error}</Alert>}
          <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3" controlId="username">
              <Form.Label>Username</Form.Label>
              <Form.Control
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                placeholder="Enter username"
                required
              />
            </Form.Group>

            <Form.Group className="mb-4" controlId="password">
              <Form.Label>Password</Form.Label>
              <Form.Control
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Enter password"
                required
              />
            </Form.Group>

            <Button
              variant="primary"
              type="submit"
              className="w-100 login-btn"
              disabled={loading}
            >
              {loading ? "Logging in..." : "Login"}
            </Button>
          </Form>
        </Card.Body>
      </Card>
    </div>
  );
};

export default LoginPage;
