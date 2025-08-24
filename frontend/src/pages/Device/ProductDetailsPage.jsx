import React, { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";
import { getDeviceById } from "../../api/deviceService";
import { checkStockForDevice } from "../../api/stockService";
import {
  Container,
  Row,
  Col,
  Card,
  Button,
  Spinner,
  Alert,
  Badge,
} from "react-bootstrap";
import { useCart } from "../../context/CartContext";
import "./ProductDetailsPage.css"; // ✅ Import styles

const ProductDetailsPage = () => {
  const { deviceId } = useParams();
  const [device, setDevice] = useState(null);
  const [stock, setStock] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const { addToCart } = useCart();

  useEffect(() => {
    const fetchDeviceDetails = async () => {
      if (!deviceId) return;
      setLoading(true);
      try {
        const devicePromise = getDeviceById(deviceId);
        const stockPromise = checkStockForDevice(deviceId);
        const [deviceData, stockData] = await Promise.all([
          devicePromise,
          stockPromise,
        ]);
        setDevice(deviceData);
        setStock(stockData);
      } catch (err) {
        setError(err.data?.message || "Failed to load product details.");
      } finally {
        setLoading(false);
      }
    };
    fetchDeviceDetails();
  }, [deviceId]);

  if (loading) {
    return (
      <div className="text-center py-5">
        <Spinner animation="border" />
      </div>
    );
  }
  if (error) return <Alert variant="danger">{error}</Alert>;
  if (!device) return <Alert variant="warning">Product not found.</Alert>;

  return (
    <Container className="product-details-container">
      <Link to="/" className="back-btn">
        ← Go Back
      </Link>
      <Row className="align-items-center">
        <Col md={6} className="image-col">
          <Card.Img
            className="product-image"
            variant="top"
            src={
              device.imageUrl ||
              `https://placehold.co/600x400/EEE/31343C?text=${device.deviceName}`
            }
            alt={device.deviceName}
          />
        </Col>
        <Col md={6}>
          <Card className="product-card">
            <Card.Body>
              <Card.Title as="h2" className="product-title">
                {device.deviceName}
              </Card.Title>
              <Card.Subtitle className="mb-2 text-muted">
                {device.deviceType}
              </Card.Subtitle>
              <hr />
              <Card.Text>
                <strong>Description:</strong> {device.description}
              </Card.Text>
              <Card.Text as="h3" className="price-text my-3">
                ₹{device.price.toLocaleString("en-IN")}
              </Card.Text>
              <hr />
              <p>
                <strong>Status:</strong>{" "}
                {stock && stock.quantity > 0 ? (
                  <Badge bg="success" className="status-badge">
                    In Stock ({stock.quantity} available)
                  </Badge>
                ) : (
                  <Badge bg="danger" className="status-badge">
                    Out of Stock
                  </Badge>
                )}
              </p>
              <div className="d-grid">
                <Button
                  onClick={() => addToCart(device)}
                  variant="primary"
                  disabled={!stock || stock.quantity === 0}
                  className="add-to-cart-btn"
                >
                  Add to Cart
                </Button>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default ProductDetailsPage;
