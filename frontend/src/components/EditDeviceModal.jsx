import React, { useState, useEffect } from "react";
import { Modal, Form, Button, Spinner, Alert } from "react-bootstrap";

const EditDeviceModal = ({ show, onHide, device, onUpdate }) => {
  const [editedDevice, setEditedDevice] = useState(device);
  const [editDeviceImage, setEditDeviceImage] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    // Update the form data when a new device is passed in
    setEditedDevice(device);
  }, [device]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setEditedDevice((prev) => ({ ...prev, [name]: value }));
  };

  const handleFileChange = (e) => {
    setEditDeviceImage(e.target.files[0]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    try {
      await onUpdate(editedDevice, editDeviceImage);
      onHide(); // Close modal on success
    } catch (err) {
      setError(err.message || "Failed to update device.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal show={show} onHide={onHide} centered>
      <Modal.Header closeButton>
        <Modal.Title>Edit Device</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          {error && <Alert variant="danger">{error}</Alert>}
          <Form.Group className="mb-3">
            <Form.Label>Device Name</Form.Label>
            <Form.Control
              type="text"
              name="deviceName"
              value={editedDevice?.deviceName || ""}
              onChange={handleChange}
              required
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>New Device Image (Optional)</Form.Label>
            <Form.Control
              type="file"
              name="image"
              onChange={handleFileChange}
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Device Type</Form.Label>
            <Form.Control
              type="text"
              name="deviceType"
              value={editedDevice?.deviceType || ""}
              onChange={handleChange}
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Description</Form.Label>
            <Form.Control
              as="textarea"
              rows={3}
              name="description"
              value={editedDevice?.description || ""}
              onChange={handleChange}
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Price</Form.Label>
            <Form.Control
              type="number"
              name="price"
              value={editedDevice?.price || ""}
              onChange={handleChange}
              required
              step="0.01"
            />
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onHide}>
            Cancel
          </Button>
          <Button variant="primary" type="submit" disabled={loading}>
            {loading ? <Spinner size="sm" /> : "Save Changes"}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
};

export default EditDeviceModal;
