import React, { useState, useEffect, useMemo } from "react";
import {
  getAllDevices,
  createDevice,
  deleteDevice,
  updateDevice,
} from "../../../api/deviceService";
import {
  Container,
  Row,
  Col,
  Card,
  Spinner,
  Alert,
  InputGroup,
  Form,
  Modal,
  Button,
} from "react-bootstrap";
import toast from "react-hot-toast";
import DeviceList from "../../../components/DeviceList";
import CreateDeviceForm from "../../../components/CreateDeviceForm";
import EditDeviceModal from "../../../components/EditDeviceModal";

const DeviceManagement = () => {
  const CLOUDINARY_CLOUD_NAME = import.meta.env.VITE_CLOUDINARY_CLOUD_NAME;
  const CLOUDINARY_UPLOAD_PRESET = import.meta.env
    .VITE_CLOUDINARY_UPLOAD_PRESET;

  const [devices, setDevices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [searchQuery, setSearchQuery] = useState("");

  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [deviceToDelete, setDeviceToDelete] = useState(null);
  const [deleteLoading, setDeleteLoading] = useState(false);
  const [deleteError, setDeleteError] = useState("");

  const [showEditModal, setShowEditModal] = useState(false);
  const [deviceToEdit, setDeviceToEdit] = useState(null);

  const fetchDevices = async () => {
    try {
      setLoading(true);
      const data = await getAllDevices();
      setDevices(data);
    } catch (err) {
      setError(err.data?.message || "Failed to load devices.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDevices();
  }, []);

  const filteredDevices = useMemo(() => {
    if (!searchQuery) return devices;
    return devices.filter((device) =>
      device.deviceName.toLowerCase().includes(searchQuery.toLowerCase())
    );
  }, [devices, searchQuery]);

  const handleCreateDevice = async (newDevice, newDeviceImage) => {
    let imageUrl = null;
    if (newDeviceImage) {
      const formData = new FormData();
      formData.append("file", newDeviceImage);
      formData.append("upload_preset", CLOUDINARY_UPLOAD_PRESET);
      const response = await fetch(
        `https://api.cloudinary.com/v1_1/${CLOUDINARY_CLOUD_NAME}/image/upload`,
        {
          method: "POST",
          body: formData,
        }
      );
      const data = await response.json();
      if (!data.secure_url) throw new Error("Image upload failed.");
      imageUrl = data.secure_url;
    }

    const deviceData = {
      ...newDevice,
      price: parseFloat(newDevice.price),
      imageUrl,
    };
    await createDevice(deviceData);
    toast.success(`${newDevice.deviceName} created successfully!`);
    fetchDevices();
  };

  const handleUpdateDevice = async (editedDevice, editDeviceImage) => {
    let updatedImageUrl = editedDevice.imageUrl;
    if (editDeviceImage) {
      const formData = new FormData();
      formData.append("file", editDeviceImage);
      formData.append("upload_preset", CLOUDINARY_UPLOAD_PRESET);
      const response = await fetch(
        `https://api.cloudinary.com/v1_1/${CLOUDINARY_CLOUD_NAME}/image/upload`,
        {
          method: "POST",
          body: formData,
        }
      );
      const data = await response.json();
      if (!data.secure_url) throw new Error("Image upload failed.");
      updatedImageUrl = data.secure_url;
    }

    const deviceDataForApi = {
      ...editedDevice,
      price: parseFloat(editedDevice.price),
      imageUrl: updatedImageUrl,
    };
    await updateDevice(editedDevice.deviceId, deviceDataForApi);
    toast.success(`${editedDevice.deviceName} updated successfully!`);
    fetchDevices();
  };

  const handleDeleteDevice = async () => {
    if (!deviceToDelete) return;
    setDeleteLoading(true);
    setDeleteError("");
    try {
      await deleteDevice(deviceToDelete.deviceId);
      setShowDeleteModal(false);
      setDeviceToDelete(null);
      fetchDevices();
      toast.success(`${deviceToDelete.deviceName} deleted successfully!`);
    } catch (err) {
      setDeleteError(err.data?.message || "Failed to delete device.");
    } finally {
      setDeleteLoading(false);
    }
  };

  return (
    <Container>
      <h1 className="mb-4">Device Management</h1>
      <Row>
        <Col md={8}>
          <Card>
            <Card.Header as="h5">All Devices</Card.Header>
            <Card.Body>
              <InputGroup className="mb-3">
                <InputGroup.Text>🔍</InputGroup.Text>
                <Form.Control
                  placeholder="Search by device name..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                />
              </InputGroup>
              {loading && (
                <div className="text-center">
                  <Spinner animation="border" />
                </div>
              )}
              {error && <Alert variant="danger">{error}</Alert>}
              {!loading && !error && (
                <DeviceList
                  devices={filteredDevices}
                  onEdit={(device) => {
                    setDeviceToEdit(device);
                    setShowEditModal(true);
                  }}
                  onDelete={(device) => {
                    setDeviceToDelete(device);
                    setShowDeleteModal(true);
                  }}
                />
              )}
            </Card.Body>
          </Card>
        </Col>
        <Col md={4}>
          <Card>
            <Card.Header as="h5">Create New Device</Card.Header>
            <Card.Body>
              <CreateDeviceForm onSubmit={handleCreateDevice} />
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {deviceToEdit && (
        <EditDeviceModal
          show={showEditModal}
          onHide={() => setShowEditModal(false)}
          device={deviceToEdit}
          onUpdate={handleUpdateDevice}
        />
      )}

      {deviceToDelete && (
        <Modal
          show={showDeleteModal}
          onHide={() => setShowDeleteModal(false)}
          centered
        >
          <Modal.Header closeButton>
            <Modal.Title>Confirm Deletion</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            {deleteError && <Alert variant="danger">{deleteError}</Alert>}
            Are you sure you want to permanently delete{" "}
            <strong>{deviceToDelete?.deviceName}</strong>?
          </Modal.Body>
          <Modal.Footer>
            <Button
              variant="secondary"
              onClick={() => setShowDeleteModal(false)}
            >
              Cancel
            </Button>
            <Button
              variant="danger"
              onClick={handleDeleteDevice}
              disabled={deleteLoading}
            >
              {deleteLoading ? <Spinner size="sm" /> : "Delete"}
            </Button>
          </Modal.Footer>
        </Modal>
      )}
    </Container>
  );
};

export default DeviceManagement;
