// src/components/AddressForm.jsx
import React, { useState, useEffect } from "react";
import { Form, Button, Spinner, Row, Col } from "react-bootstrap";

const AddressForm = ({ onSubmit, isLoading }) => {
  const [address, setAddress] = useState({
    streetAddress: "",
    city: "",
    state: "",
    postalCode: "",
    addressType: "HOME",
    isDefault: false,
  });

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setAddress((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(address);
  };

  return (
    <Form onSubmit={handleSubmit}>
      <Form.Group className="mb-3">
        <Form.Label>Street Address</Form.Label>
        <Form.Control
          type="text"
          name="streetAddress"
          value={address.streetAddress}
          onChange={handleChange}
          required
        />
      </Form.Group>
      <Row>
        <Col md={6}>
          <Form.Group className="mb-3">
            <Form.Label>City</Form.Label>
            <Form.Control
              type="text"
              name="city"
              value={address.city}
              onChange={handleChange}
              required
            />
          </Form.Group>
        </Col>
        <Col md={6}>
          <Form.Group className="mb-3">
            <Form.Label>State</Form.Label>
            <Form.Control
              type="text"
              name="state"
              value={address.state}
              onChange={handleChange}
              required
            />
          </Form.Group>
        </Col>
      </Row>
      <Row>
        <Col md={6}>
          <Form.Group className="mb-3">
            <Form.Label>Postal Code</Form.Label>
            <Form.Control
              type="text"
              name="postalCode"
              value={address.postalCode}
              onChange={handleChange}
              required
            />
          </Form.Group>
        </Col>
        <Col md={6}>
          <Form.Group className="mb-3">
            <Form.Label>Address Type</Form.Label>
            <Form.Select
              name="addressType"
              value={address.addressType}
              onChange={handleChange}
            >
              <option value="HOME">HOME</option>
              <option value="OFFICE">WORK</option>
              <option value="OTHER">OTHER</option>
            </Form.Select>
          </Form.Group>
        </Col>
      </Row>
      <Form.Group className="mb-3">
        <Form.Check
          type="checkbox"
          name="isDefault"
          label="Make this my default address"
          checked={address.isDefault}
          onChange={handleChange}
        />
      </Form.Group>
      <Button type="submit" variant="primary" disabled={isLoading}>
        {isLoading ? (
          <Spinner as="span" animation="border" size="sm" />
        ) : (
          "Save Address"
        )}
      </Button>
    </Form>
  );
};

export default AddressForm;
