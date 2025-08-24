import React, { useState, useEffect } from "react";
import { getAllDevices, searchDevices } from "../../api/deviceService";

import DeviceCard from "../../components/DeviceCard";
import { useCart } from "../../context/CartContext";
import "./HomePage.css";
import { LinkContainer } from "react-router-bootstrap";


import {
  Row,
  Spinner,
  Alert,
  Form,
  InputGroup,
  Container,
  Badge,
  Col,
} from "react-bootstrap";

const HomePage = () => {
  const [devices, setDevices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [searchQuery, setSearchQuery] = useState("");
  const { items, addItem } = useCart();

  useEffect(() => {
    // This effect handles fetching devices based on the search query.
    const fetchDevices = async () => {
      setLoading(true);
      try {
        let data;
        if (searchQuery.trim()) {
          // If there's a search query, call the search endpoint.
          data = await searchDevices(searchQuery);
        } else {
          // Otherwise, fetch all devices.
          data = await getAllDevices();
        }
        setDevices(data);
      } catch (err) {
        setError("Failed to load devices. The server might be down.");
      } finally {
        setLoading(false);
      }
    };

    // Debounce the API call to avoid searching on every keystroke.
    const searchTimeout = setTimeout(() => {
      fetchDevices();
    }, 300); // Wait 300ms after the user stops typing.

    // Cleanup function to cancel the timeout if the component unmounts or the query changes.
    return () => clearTimeout(searchTimeout);
  }, [searchQuery]); // Re-run this effect whenever the searchQuery changes.

  const handleAddToCart = (deviceToAdd) => {
    addItem(deviceToAdd);
    // You could show a toast notification here
    console.log(`${deviceToAdd.deviceName} added to cart!`);
  };

  return (
    <div>
      <div
        className="text-center text-white py-5"
        style={{
          background: "linear-gradient(90deg, #4b6cb7 0%, #182848 100%)",
        }}
      >
        <Container>
          <h1 className="fw-bold">Smart Home Devices</h1>
          <p className="lead">
            Upgrade your living with cutting-edge smart home products.
          </p>
          <LinkContainer to="/cart" style={{ cursor: "pointer" }}>
            <Badge bg="warning" text="dark" className="p-2 fs-6">
              🛒 Cart Items: {items.length}
            </Badge>
          </LinkContainer>
        </Container>
      </div>

      <Container className="py-5">
        {/* Search Bar */}
        <Row className="justify-content-center mb-4">
          <Col md={8} lg={6}>
            <InputGroup>
              <InputGroup.Text>🔍</InputGroup.Text>
              <Form.Control
                type="text"
                placeholder="Search for devices by name..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
              />
            </InputGroup>
          </Col>
        </Row>

        {/* Device Grid */}
        {loading ? (
          <div className="text-center">
            <Spinner animation="border" />
          </div>
        ) : error ? (
          <Alert variant="danger">{error}</Alert>
        ) : devices.length > 0 ? (
          <Row xs={1} md={2} lg={3} className="g-4">
            {devices.map((device) => (
              <DeviceCard
                key={device.deviceId}
                device={device}
                onAddToCart={handleAddToCart}
              />
            ))}
          </Row>
        ) : (
          <Alert variant="info" className="text-center">
            No devices found.
          </Alert>
        )}
      </Container>
    </div>
  );
};

export default HomePage;
