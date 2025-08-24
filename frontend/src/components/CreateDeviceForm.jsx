import React, { useState } from "react";
import { Form, Button, Spinner, Alert } from "react-bootstrap";

const CreateDeviceForm = ({ onSubmit }) => {
  const [newDevice, setNewDevice] = useState({
    deviceName: "",
    deviceType: "",
    description: "",
    price: "",
  });
  const [newDeviceImage, setNewDeviceImage] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setNewDevice((prev) => ({ ...prev, [name]: value }));
  };

  const handleFileChange = (e) => {
    setNewDeviceImage(e.target.files[0]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    try {
      await onSubmit(newDevice, newDeviceImage);
      // Clear form on successful submission
      setNewDevice({
        deviceName: "",
        deviceType: "",
        description: "",
        price: "",
      });
      setNewDeviceImage(null);
      e.target.reset(); // Resets the file input
    } catch (err) {
      setError(err.message || "Failed to create device.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Form onSubmit={handleSubmit}>
      {error && <Alert variant="danger">{error}</Alert>}
      <Form.Group className="mb-3">
        <Form.Label>Device Name</Form.Label>
        <Form.Control
          type="text"
          name="deviceName"
          value={newDevice.deviceName}
          onChange={handleChange}
          required
        />
      </Form.Group>
      <Form.Group className="mb-3">
        <Form.Label>Device Image (Optional)</Form.Label>
        <Form.Control
          type="file"
          name="image"
          id="new-device-image-input"
          onChange={handleFileChange}
        />
      </Form.Group>
      <Form.Group className="mb-3">
        <Form.Label>Device Type (Optional)</Form.Label>
        <Form.Control
          type="text"
          name="deviceType"
          value={newDevice.deviceType}
          onChange={handleChange}
        />
      </Form.Group>
      <Form.Group className="mb-3">
        <Form.Label>Description (Optional)</Form.Label>
        <Form.Control
          as="textarea"
          rows={3}
          name="description"
          value={newDevice.description}
          onChange={handleChange}
        />
      </Form.Group>
      <Form.Group className="mb-3">
        <Form.Label>Price</Form.Label>
        <Form.Control
          type="number"
          name="price"
          value={newDevice.price}
          onChange={handleChange}
          required
          step="0.01"
        />
      </Form.Group>
      <Button
        type="submit"
        variant="primary"
        className="w-100"
        disabled={loading}
      >
        {loading ? <Spinner size="sm" /> : "Create Device"}
      </Button>
    </Form>
  );
};

export default CreateDeviceForm;
