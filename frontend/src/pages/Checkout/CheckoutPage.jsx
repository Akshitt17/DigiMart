import React, { useState, useEffect } from "react";

import {
  getCustomerByUserId,
  getCustomerAddresses,
} from "../../api/customerService";
import { createOrder } from "../../api/orderService";
import {
  Container,
  Row,
  Col,
  Card,
  ListGroup,
  Button,
  Spinner,
  Alert,
  Form,
} from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { useCart } from "../../context/CartContext";
import toast from "react-hot-toast";
import "./CheckoutPage.css";

const CheckoutPage = () => {
  const { items, cartTotal, clearCart } = useCart();
  const { user } = useAuth();
  const navigate = useNavigate();

  const [addresses, setAddresses] = useState([]);
  const [selectedAddressId, setSelectedAddressId] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [orderPlacing, setOrderPlacing] = useState(false);
  const [customer, setCustomer] = useState(null); // State to hold the customer profile

  useEffect(() => {
    const loadCustomerData = async () => {
      if (!user?.userId) {
        setLoading(false);
        return;
      }
      try {
        setLoading(true);
        // Step 1: Fetch the full customer profile using the userId to get the customerId.
        const customerData = await getCustomerByUserId(user.userId);
        setCustomer(customerData);

        // Step 2: Use the customerId from the profile to fetch the addresses.
        const addressData = await getCustomerAddresses(customerData.customerId);
        setAddresses(addressData);

        // Pre-select the default address if one exists.
        const defaultAddress = addressData.find((addr) => addr.isDefault);
        if (defaultAddress) {
          setSelectedAddressId(defaultAddress.addressId);
        }
      } catch (err) {
        if (err.status === 404) {
          setError(
            <span>
              Your customer profile is not set up. Please{" "}
              <Link to="/profile">create your profile</Link> before checking
              out.
            </span>
          );
        } else {
          setError(err.data?.message || "Failed to load your data.");
        }
      } finally {
        setLoading(false);
      }
    };

    loadCustomerData();
  }, [user]);

  const handlePlaceOrder = async () => {
    if (!selectedAddressId) {
      setError("Please select a delivery address.");
      return;
    }
    setError("");
    setOrderPlacing(true);

    const orderData = {
      deliveryAddressId: parseInt(selectedAddressId, 10),
      orderItems: items.map((item) => ({
        deviceId: item.deviceId,
        quantity: item.quantity,
        price: item.price,
      })),
    };

    try {
      const createdOrder = await createOrder(orderData);
      clearCart();
      // Redirect to the new invoice page with the order ID
      toast.success("Order placed successfully ", 4000);
      navigate(`/invoice/${createdOrder.orderId}`);
    } catch (err) {
      setError(err.data?.message || "Failed to place order. Please try again.");
    } finally {
      setOrderPlacing(false);
    }
  };

  if (items.length === 0) {
    return (
      <Container className="text-center mt-5">
        <Alert variant="info">Your cart is empty.</Alert>
        <Link to="/">Go Shopping</Link>
      </Container>
    );
  }

  return (
    // <Container>
    //   <h1 className="mb-4">Checkout</h1>
    //   {error && (
    //     <Alert variant="danger" onClose={() => setError("")} dismissible>
    //       {error}
    //     </Alert>
    //   )}
    //   <Row>
    //     <Col md={8}>
    //       <h4>Delivery Address</h4>
    //       {loading ? (
    //         <Spinner animation="border" />
    //       ) : (
    //         <Form.Group className="mb-4">
    //           <Form.Label>Select an address</Form.Label>
    //           {addresses.length > 0 ? (
    //             <Form.Select
    //               value={selectedAddressId}
    //               onChange={(e) => setSelectedAddressId(e.target.value)}
    //             >
    //               <option value="">-- Please select an address --</option>
    //               {addresses.map((addr) => (
    //                 <option key={addr.addressId} value={addr.addressId}>
    //                   {addr.streetAddress}, {addr.city}, {addr.state}
    //                 </option>
    //               ))}
    //             </Form.Select>
    //           ) : (
    //             <Alert variant="warning">
    //               No addresses found. Please{" "}
    //               <Link to="/profile">add an address</Link> to your profile.
    //             </Alert>
    //           )}
    //         </Form.Group>
    //       )}

    //       <h4>Order Items</h4>
    //       <ListGroup variant="flush">
    //         {items.map((item) => (
    //           <ListGroup.Item key={item.deviceId}>
    //             <Row className="align-items-center">
    //               <Col md={6}>{item.deviceName}</Col>
    //               <Col md={3}>Qty: {item.quantity}</Col>
    //               <Col md={3} className="text-end">
    //                 ₹{(item.price * item.quantity).toLocaleString("en-IN")}
    //               </Col>
    //             </Row>
    //           </ListGroup.Item>
    //         ))}
    //       </ListGroup>
    //     </Col>

    //     <Col md={4}>
    //       <Card>
    //         <Card.Body>
    //           <Card.Title as="h4">Order Summary</Card.Title>
    //           <ListGroup variant="flush">
    //             <ListGroup.Item className="d-flex justify-content-between">
    //               <span>Subtotal</span>
    //               <span>₹{cartTotal.toLocaleString("en-IN")}</span>
    //             </ListGroup.Item>
    //             <ListGroup.Item className="d-flex justify-content-between">
    //               <span>Shipping</span>
    //               <span>FREE</span>
    //             </ListGroup.Item>
    //             <ListGroup.Item className="d-flex justify-content-between fw-bold">
    //               <span>Total</span>
    //               <span>₹{cartTotal.toLocaleString("en-IN")}</span>
    //             </ListGroup.Item>
    //           </ListGroup>
    //           <div className="d-grid mt-3">
    //             <Button
    //               variant="primary"
    //               onClick={handlePlaceOrder}
    //               disabled={
    //                 loading ||
    //                 orderPlacing ||
    //                 addresses.length === 0 ||
    //                 !selectedAddressId
    //               }
    //             >
    //               {orderPlacing ? <Spinner size="sm" /> : "Place Order"}
    //             </Button>
    //           </div>
    //         </Card.Body>
    //       </Card>
    //     </Col>
    //   </Row>
    // </Container>

    <Container className="checkout-container">
      <h1 className="checkout-title">Checkout</h1>
      {error && (
        <Alert variant="danger" onClose={() => setError("")} dismissible>
          {error}
        </Alert>
      )}
      <Row>
        <Col md={8}>
          <h4 className="section-title">Delivery Address</h4>
          {loading ? (
            <Spinner animation="border" />
          ) : (
            <Form.Group className="mb-4">
              <Form.Label>Select an address</Form.Label>
              {addresses.length > 0 ? (
                <Form.Select
                  className="address-select"
                  value={selectedAddressId}
                  onChange={(e) => setSelectedAddressId(e.target.value)}
                >
                  <option value="">-- Please select an address --</option>
                  {addresses.map((addr) => (
                    <option key={addr.addressId} value={addr.addressId}>
                      {addr.streetAddress}, {addr.city}, {addr.state}
                    </option>
                  ))}
                </Form.Select>
              ) : (
                <Alert variant="warning">
                  No addresses found. Please{" "}
                  <Link to="/profile">add an address</Link> to your profile.
                </Alert>
              )}
            </Form.Group>
          )}
          <h4 className="section-title">Order Items</h4>
          <ListGroup variant="flush" className="order-items-list">
            {items.map((item) => (
              <ListGroup.Item key={item.deviceId} className="order-item">
                <Row className="align-items-center">
                  <Col md={6}>{item.deviceName}</Col>
                  <Col md={3}>Qty: {item.quantity}</Col>
                  <Col md={3} className="text-end">
                    ₹{(item.price * item.quantity).toLocaleString("en-IN")}
                  </Col>
                </Row>
              </ListGroup.Item>
            ))}
          </ListGroup>
        </Col>

        <Col md={4}>
          <Card className="order-summary-card">
            <Card.Body>
              <Card.Title as="h4">Order Summary</Card.Title>
              <ListGroup variant="flush">
                <ListGroup.Item className="d-flex justify-content-between">
                  <span>Subtotal</span>
                  <span>₹{cartTotal.toLocaleString("en-IN")}</span>
                </ListGroup.Item>
                <ListGroup.Item className="d-flex justify-content-between">
                  <span>Shipping</span>
                  <span>FREE</span>
                </ListGroup.Item>
                <ListGroup.Item className="d-flex justify-content-between fw-bold total-row">
                  <span>Total</span>
                  <span>₹{cartTotal.toLocaleString("en-IN")}</span>
                </ListGroup.Item>
              </ListGroup>
              <div className="d-grid mt-3">
                <Button
                  variant="primary"
                  onClick={handlePlaceOrder}
                  disabled={
                    loading ||
                    orderPlacing ||
                    addresses.length === 0 ||
                    !selectedAddressId
                  }
                  className="place-order-btn"
                >
                  {orderPlacing ? <Spinner size="sm" /> : "Place Order"}
                </Button>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default CheckoutPage;
