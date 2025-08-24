import React, { useEffect, useState } from "react";
import { Card, Badge, Table, Button } from "react-bootstrap";
import { getDeviceById } from "../api/deviceService";
import { Link } from "react-router-dom";

const OrderSummary = ({ order }) => {
  const [deviceNames, setDeviceNames] = useState({});

  useEffect(() => {
    const fetchDeviceNames = async () => {
      if (order && order.orderItems) {
        const devicePromises = order.orderItems.map((item) =>
          getDeviceById(item.deviceId)
        );
        try {
          const deviceDetails = await Promise.all(devicePromises);
          const namesMap = deviceDetails.reduce((acc, device) => {
            acc[device.deviceId] = device.deviceName;
            return acc;
          }, {});
          setDeviceNames(namesMap);
        } catch (error) {
          console.error(
            "Failed to fetch device names for order summary",
            error
          );
        }
      }
    };
    fetchDeviceNames();
  }, [order]);

  const getStatusBadge = (status) => {
    switch (status) {
      case "CONFIRMED":
        return "primary";
      case "SHIPPED":
        return "info";
      case "DELIVERED":
        return "success";
      case "CANCELLED":
        return "danger";
      default:
        return "secondary";
    }
  };

  return (
    <Card>
      <Card.Header
        as="h5"
        className="d-flex justify-content-between align-items-center"
      >
        <span>Order Details</span>
        <Badge bg={getStatusBadge(order.status)}>{order.status}</Badge>
      </Card.Header>
      <Card.Body>
        <Card.Text>
          <strong>Order Date:</strong>{" "}
          {new Date(order.orderDate).toLocaleString("en-IN")}
        </Card.Text>

        <h6 className="mt-4">Order Items</h6>
        <Table striped bordered hover responsive="sm">
          <thead>
            <tr>
              <th>Device Name</th>
              <th>Quantity</th>
              <th>Unit Price</th>
              <th>Subtotal</th>
            </tr>
          </thead>
          <tbody>
            {order.orderItems.map((item) => (
              <tr key={item.orderItemId}>
                <td>
                  {item.deviceName}
                </td>
                <td>{item.quantity}</td>
                <td>₹{item.price.toLocaleString("en-IN")}</td>
                <td>₹{(item.quantity * item.price).toLocaleString("en-IN")}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      </Card.Body>
      <Card.Footer className="d-flex justify-content-between align-items-center">
        <span className="fs-5 fw-bold">
          Total Amount: ₹{order.totalAmount.toLocaleString("en-IN")}
        </span>
        <Link to={`/invoice/${order.orderId}`}>
          <Button variant="secondary">View Invoice</Button>
        </Link>
      </Card.Footer>
    </Card>
  );
};

export default OrderSummary;
