
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { registerCustomerProfile } from '../../api/customerService';
import { Form, Button, Card, Alert, Spinner } from 'react-bootstrap';

const CustomerProfileRegistrationPage = () => {
  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    phone: ''
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { user } = useAuth(); 

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const response = await registerCustomerProfile(formData);
      // On success, maybe redirect them to add an address or to the homepage
      console.log('Customer profile created:', response);
      
      navigate('/'); // Redirect to homepage for now
    } catch (err) {
      setError(err.message || 'Failed to create profile. Please try again.');
    } finally {
      setLoading(false);
    }
  };
  
 
  if (!user) {
    return <Alert variant="warning">You must be logged in to create a profile.</Alert>
  }

 return (
    <div className="d-flex justify-content-center">
      <Card style={{ width: '30rem' }}>
        <Card.Body>
          <Card.Title className="text-center mb-4">Create Your Customer Profile</Card.Title>
          <Card.Text className="text-muted text-center mb-4">
            Welcome, {user.username}! Just a few more details to get you started.
          </Card.Text>
          {error && <Alert variant="danger">{error}</Alert>}
          <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3">
              <Form.Label>Full Name</Form.Label>
              <Form.Control type="text" name="fullName" onChange={handleChange} required />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Email Address</Form.Label>
              <Form.Control type="email" name="email" onChange={handleChange} required />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Phone Number</Form.Label>
              <Form.Control type="tel" name="phone" onChange={handleChange} required />
            </Form.Group> 
            <Button variant="primary" type="submit" className="w-100" disabled={loading}>
              {loading ? <Spinner as="span" animation="border" size="sm" /> : 'Save Profile'}
            </Button>
          </Form>
        </Card.Body>
      </Card>
    </div>
  );
};

export default CustomerProfileRegistrationPage;