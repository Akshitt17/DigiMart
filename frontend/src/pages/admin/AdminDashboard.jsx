import React from "react";
import { Container, Row, Col, Card } from "react-bootstrap";
import { Link } from "react-router-dom";
import "./AdminDashboard.css";

const AdminDashboard = () => {
  const cardItems = [
    {
      title: "Device Management",
      text: "Add, view, or update devices in the store.",
      link: "/admin/devices",
      btn: "Manage Devices",
      colorClass: "blue-theme",
    },
    {
      title: "Stock Management",
      text: "View and update stock levels for all devices.",
      link: "/admin/stock",
      btn: "Manage Stock",
      colorClass: "green-theme",
    },
    {
      title: "Order Management",
      text: "View all customer orders and update their status.",
      link: "/admin/orders",
      btn: "Manage Orders",
      colorClass: "orange-theme",
    },
  ];

  return (
    <Container className="dashboard-container">
      <h1 className="dashboard-title">⚙️ Admin Control Center</h1>
      <p className="dashboard-subtitle">
        Manage all aspects of your store efficiently from here.
      </p>
      <Row>
        {cardItems.map((item, index) => (
          <Col key={index} md={4} className="mb-4">
            <Card className="dashboard-card h-100">
              <div className={`card-icon ${item.colorClass}`}>
                <i className="bi bi-gear-fill"></i>
              </div>
              <Card.Body className="d-flex flex-column">
                <Card.Title className="card-title">{item.title}</Card.Title>
                <Card.Text className="card-text flex-grow-1">
                  {item.text}
                </Card.Text>
                <Link
                  to={item.link}
                  className={`btn dashboard-btn ${item.colorClass} mt-auto`}
                >
                  {item.btn}
                </Link>
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>
    </Container>
  );
};

export default AdminDashboard;
