import React from "react";
import { Table, Button, Image } from "react-bootstrap";

const DeviceList = ({ devices, onEdit, onDelete }) => {
  return (
    <Table striped bordered hover responsive>
      <thead>
        <tr>
          <th>Image</th>
          <th>ID</th>
          <th>Name</th>
          <th>Type</th>
          <th>Price</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {devices.map((device) => (
          <tr key={device.deviceId}>
            <td>
              <Image
                src={
                  device.imageUrl ||
                  "https://placehold.co/60x60/EEE/31343C?text=No+Image"
                }
                thumbnail
                style={{ width: "60px" }}
              />
            </td>
            <td>{device.deviceId}</td>
            <td>{device.deviceName}</td>
            <td>{device.deviceType}</td>
            <td>₹{device.price.toLocaleString("en-IN")}</td>
            <td>
              <Button
                variant="outline-primary"
                size="sm"
                className="me-2"
                onClick={() => onEdit(device)}
              >
                Edit
              </Button>
              <Button
                variant="outline-danger"
                size="sm"
                onClick={() => onDelete(device)}
              >
                Delete
              </Button>
            </td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
};

export default DeviceList;
