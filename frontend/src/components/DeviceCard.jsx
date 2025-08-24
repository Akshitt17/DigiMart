import React, { useEffect, useState } from "react";
import { Card, Button, Col, Badge } from "react-bootstrap";
import { Link, useParams } from "react-router-dom";
import { useCart } from "../context/CartContext";
import "./DeviceCard.css"; // custom styles
import { getDeviceById } from "../api/deviceService";
import { checkStockForDevice } from "../api/stockService";
import { useAuth } from "../context/AuthContext";

const DeviceCard = ({ device }) => {
  const { user } = useAuth;
  const { addToCart } = useCart();
  const deviceId = device.deviceId; 

  const [stock, setStock] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchDeviceDetails = async () => {
      if (!device.deviceId) return;
      setLoading(true);
      try {
        // Fetch device details and stock in parallel
        const devicePromise = getDeviceById(deviceId);
        const stockPromise = checkStockForDevice(device.deviceId);

        const [deviceData, stockData] = await Promise.all([
          devicePromise,
          stockPromise,
        ]);

        setStock(stockData);
      } catch (err) {
        setError(err.data?.message || "Failed to load product details.");
      } finally {
        setLoading(false);
      }
    };

    fetchDeviceDetails();
  }, [deviceId]);

  return (
    <Col>
      <Card className="device-card h-100 shadow-sm">
        {/* Show device image if available, else a placeholder */}
        {device.imageUrl && (
          <Card.Img
            variant="top"
            src={device.imageUrl}
            alt={device.deviceName}
            className="device-img"
          />
        )}
        <Card.Body className="d-flex flex-column">
          <Card.Title className="device-title">{device.deviceName}</Card.Title>
          <Card.Subtitle className="mb-2 text-muted">
            {device.deviceType}
          </Card.Subtitle>
          <Card.Text className="flex-grow-1 text-secondary">
            {device.description}
          </Card.Text>
          <Card.Text className="fw-bold fs-5 text-primary">
            ₹{device.price.toLocaleString("en-IN")}
          </Card.Text>
          <div className="mt-auto">
            <Link to={`/devices/${device.deviceId}`}>
              <Button variant="outline-dark" className="me-2">
                View Details
              </Button>
            </Link>
           
            {stock && stock.quantity > 0 ? (
              <Button variant="success" onClick={() => addToCart(device)}>
                <i className="bi bi-cart-plus me-1"></i> Add to Cart
              </Button>
            ) : (
              <Badge bg="danger">Out of Stock</Badge>
            )}
          </div>
        </Card.Body>
      </Card>
    </Col>
  );
};

export default DeviceCard;
