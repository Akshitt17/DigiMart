import React, { useState, useEffect } from "react";
import { useAuth } from "../../context/AuthContext";
import {
  getCustomerByUserId,
  registerCustomerProfile,
  updateCustomer, // Import the new update function
} from "../../api/customerService";
import {
  Container,
  Card,
  Button,
  Spinner,
  Alert,
  Form,
  Row,
  Col,
} from "react-bootstrap";
import AddressManagement from "../../components/AddressManagement";
import "./CustomerProfilePage.css";

const CustomerDetails = ({ customer, onProfileUpdate }) => {
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    fullName: customer.fullName,
    email: customer.email,
    phone: customer.phone,
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleFormChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleEditSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    try {
      await updateCustomer(customer.customerId, formData);
      setIsEditing(false);
      onProfileUpdate(); // Notify parent to refresh data
    } catch (err) {
      setError(err.data?.message || "Failed to update profile.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card className="mb-4">
      <Card.Header as="h5">My Details</Card.Header>
      <Card.Body>
        {error && <Alert variant="danger">{error}</Alert>}
        {isEditing ? (
          <Form onSubmit={handleEditSubmit}>
            <Form.Group className="mb-3">
              <Form.Label>Full Name</Form.Label>
              <Form.Control
                type="text"
                name="fullName"
                value={formData.fullName}
                onChange={handleFormChange}
                required
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Email Address</Form.Label>
              <Form.Control
                type="email"
                name="email"
                value={formData.email}
                onChange={handleFormChange}
                required
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Phone Number</Form.Label>
              <Form.Control
                type="tel"
                name="phone"
                value={formData.phone}
                onChange={handleFormChange}
                required
              />
            </Form.Group>
            <div className="d-flex justify-content-end">
              <Button
                variant="secondary"
                onClick={() => setIsEditing(false)}
                className="me-2"
              >
                Cancel
              </Button>
              <Button variant="primary" type="submit" disabled={loading}>
                {loading ? <Spinner size="sm" /> : "Save Changes"}
              </Button>
            </div>
          </Form>
        ) : (
          <>
            <p>
              <strong>Full Name:</strong> {customer.fullName}
            </p>
            <p>
              <strong>Email:</strong> {customer.email}
            </p>
            <p>
              <strong>Phone:</strong> {customer.phone}
            </p>
            <Button
              variant="outline-primary"
              onClick={() => setIsEditing(true)}
            >
              Edit Details
            </Button>
          </>
        )}
      </Card.Body>
    </Card>
  );
};

// A component to handle the customer profile creation form
const CreateProfileForm = ({ onProfileCreate }) => {
  const [formData, setFormData] = useState({
    fullName: "",
    email: "",
    phone: "",
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    try {
      const newProfile = await registerCustomerProfile(formData);
      onProfileCreate(newProfile); // Notify parent component of success
    } catch (err) {
      setError(err.data?.message || "Failed to create profile.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card>
      <Card.Header as="h5">Create Your Customer Profile</Card.Header>
      <Card.Body>
        <p className="text-muted">
          Complete your profile to start managing your addresses and orders.
        </p>
        {error && <Alert variant="danger">{error}</Alert>}
        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3">
            <Form.Label>Full Name</Form.Label>
            <Form.Control
              type="text"
              name="fullName"
              onChange={handleChange}
              required
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Email Address</Form.Label>
            <Form.Control
              type="email"
              name="email"
              onChange={handleChange}
              required
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Phone Number</Form.Label>
            <Form.Control
              type="tel"
              name="phone"
              onChange={handleChange}
              required
            />
          </Form.Group>
          <Button
            variant="primary"
            type="submit"
            className="w-100"
            disabled={loading}
          >
            {loading ? <Spinner size="sm" /> : "Save Profile"}
          </Button>
        </Form>
      </Card.Body>
    </Card>
  );
};

// Main page component that decides whether to show the profile or the creation form
const CustomerProfilePage = () => {
  const { user } = useAuth();
  const [customer, setCustomer] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [profileExists, setProfileExists] = useState(false);

  const checkAndFetchProfile = async () => {
    if (!user?.userId) {
      setLoading(false);
      return;
    }
    setLoading(true);
    try {
      const data = await getCustomerByUserId(user.userId);
      setCustomer(data);
      setProfileExists(true);
    } catch (err) {
      if (err.status === 404) {
        setProfileExists(false);
      } else {
        setError(
          "An error occurred while checking your profile. Please try again later."
        );
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    checkAndFetchProfile();
  }, [user]);

  if (loading) {
    return (
      <div className="text-center py-5">
        <Spinner animation="border" />
      </div>
    );
  }

  if (error) {
    return <Alert variant="danger">{error}</Alert>;
  }

  return (
    <Container>
      <h1 className="mb-4">{user.username} Profile</h1>
      {profileExists ? (
        <Row>
          <Col lg={4}>
            <CustomerDetails
              customer={customer}
              onProfileUpdate={checkAndFetchProfile}
            />
          </Col>
          <Col lg={8}>
            <AddressManagement customer={customer} />
          </Col>
        </Row>
      ) : (
        <CreateProfileForm onProfileCreate={checkAndFetchProfile} />
      )}
    </Container>
  );
};

export default CustomerProfilePage;
