import React, { useState, useEffect, useMemo } from "react";
import { getAllOrders, updateOrderStatus } from "../../../api/orderService";
import {
  Container,
  Card,
  Accordion,
  Spinner,
  Alert,
  Form,
  Row,
  Col,
  Badge,
  InputGroup,
} from "react-bootstrap";
import OrderSummary from "../../../components/OrderSummary";

const OrderManagement = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // State for filtering by customer ID
  const [filterCustomerId, setFilterCustomerId] = useState("");

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
  const fetchOrders = async () => {
    try {
      setLoading(true);
      const data = await getAllOrders();
      setOrders(data);
    } catch (err) {
      setError(err.data?.message || "Failed to load orders.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  const handleStatusChange = async (orderId, newStatus) => {
    try {
      await updateOrderStatus(orderId, newStatus);
      fetchOrders(); // Refresh list to show updated status
    } catch (err) {
      console.error("Failed to update status:", err);
      // In a real app, you'd use a toast notification here
      alert(`Failed to update status for order ${orderId}.`);
    }
  };

  // --- FILTERING LOGIC ---
  const filteredOrders = useMemo(() => {
    if (!filterCustomerId) {
      return orders;
    }
    return orders.filter((order) =>
      String(order.customerId).includes(filterCustomerId)
    );
  }, [orders, filterCustomerId]);
  // ---------------------------

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
      <h1 className="mb-4">Order Management</h1>

      <Card className="mb-4">
        <Card.Body>
          <InputGroup>
            <InputGroup.Text>Filter by Customer ID</InputGroup.Text>
            <Form.Control
              type="text"
              placeholder="Enter Customer ID..."
              value={filterCustomerId}
              onChange={(e) => setFilterCustomerId(e.target.value)}
            />
          </InputGroup>
        </Card.Body>
      </Card>

      {filteredOrders.length > 0 ? (
        <Accordion>
          {filteredOrders.map((order, index) => (
            <Accordion.Item eventKey={String(index)} key={order.orderId}>
              <Accordion.Header>
                <div className="d-flex justify-content-between w-100 align-items-center pe-3">
                  <span>
                    <strong>Order #{order.orderId}</strong> | Customer:{" "}
                    {order.customerId}
                  </span>
                  <Badge bg={getStatusBadge(order.status)}>
                    {order.status}
                  </Badge>
                </div>
              </Accordion.Header>
              <Accordion.Body>
                <Row>
                  <Col md={9}>
                    <OrderSummary order={order} />
                  </Col>
                  <Col md={3}>
                    <h5>Actions</h5>
                    <Form.Label>Update Status</Form.Label>
                    <Form.Select
                      defaultValue={order.status}
                      onChange={(e) =>
                        handleStatusChange(order.orderId, e.target.value)
                      }
                    >
                      <option value="CONFIRMED">CONFIRMED</option>
                      <option value="SHIPPED">SHIPPED</option>
                      <option value="DELIVERED">DELIVERED</option>
                      <option value="CANCELLED">CANCELLED</option>
                    </Form.Select>
                  </Col>
                </Row>
              </Accordion.Body>
            </Accordion.Item>
          ))}
        </Accordion>
      ) : (
        <Alert variant="info">No orders found for the selected filter.</Alert>
      )}
    </Container>
  );
};

export default OrderManagement;
