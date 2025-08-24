import React, { useState, useEffect } from "react";

import {
  Container,
  Card,
  Table,
  Button,
  Spinner,
  Alert,
  Modal,
  Form,
} from "react-bootstrap";
import "./StockManagement.css"; 
import { getAllStock, updateStock } from "../../../api/stockService";


const StockManagement = () => {
  const [stockItems, setStockItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");


  // Modal State
  const [showModal, setShowModal] = useState(false);
  const [selectedStock, setSelectedStock] = useState(null);
  const [newQuantity, setNewQuantity] = useState("");
  const [modalLoading, setModalLoading] = useState(false);
  const [modalError, setModalError] = useState("");

  const fetchStock = async () => {
    try {
      setLoading(true);
      const data = await getAllStock();

      setStockItems(data);
    } catch (err) {
      setError(err.data?.message || "Failed to load stock data.");
    } finally {
      setLoading(false);
    }
  };


  useEffect(() => {
    fetchStock();
  }, []);

  const handleOpenModal = (stockItem) => {
    setSelectedStock(stockItem);
    setNewQuantity(stockItem.quantity);
    setShowModal(true);
    setModalError("");
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setSelectedStock(null);
  };

  const handleUpdateStock = async (e) => {
    console.log(stockItems);
    e.preventDefault();
    setModalLoading(true);
    setModalError("");
    try {
      console.log(selectedStock);
      await updateStock(selectedStock.deviceId, {
        deviceId: selectedStock.deviceId,
        quantity: parseInt(newQuantity, 10),
      });
      handleCloseModal();
      fetchStock();
    } catch (err) {
      setModalError(err.data?.message || "Failed to update stock.");
    } finally {
      setModalLoading(false);
    }
  };

  return (
    <Container className="stock-management-container">
      <h1 className="page-title">📦 Stock Management</h1>
      <p className="page-subtitle">
        Monitor and update device inventory levels with ease.
      </p>

      <Card className="custom-card">
        <Card.Header as="h5" className="card-header-custom">
          All Stock Items
        </Card.Header>
        <Card.Body>
          {loading && (
            <div className="text-center">
              <Spinner animation="border" />
            </div>
          )}
          {error && <Alert variant="danger">{error}</Alert>}
          {!loading && !error && (
            <Table striped bordered hover responsive className="stock-table">
              <thead>
                <tr>
                  <th>Stock ID</th>
                  <th>Device Name</th>
                  <th>Device ID</th>
                  <th>Quantity</th>
                  <th>Last Updated</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {stockItems.map((item) => (
                  <tr key={item.stockId}>
                    <td>{item.stockId}</td>
                    <td>{item.deviceName}</td>
                    <td>{item.deviceId}</td>
                    <td>{item.quantity}</td>
                    <td>{new Date(item.updatedAt).toLocaleString("en-IN")}</td>
                    <td>
                      <Button
                        variant="outline-primary"
                        size="sm"
                        className="update-btn"
                        onClick={() => handleOpenModal(item)}
                      >
                        Update
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          )}
        </Card.Body>
      </Card>

      {/* Update Stock Modal */}
      <Modal show={showModal} onHide={handleCloseModal} centered>
        <Modal.Header closeButton>
          <Modal.Title>
            Update Stock for {selectedStock?.deviceName}
          </Modal.Title>
        </Modal.Header>
        <Form onSubmit={handleUpdateStock}>
          <Modal.Body>
            {modalError && <Alert variant="danger">{modalError}</Alert>}
            <Form.Group>
              <Form.Label>New Quantity</Form.Label>
              <Form.Control
                type="number"
                value={newQuantity}
                onChange={(e) => setNewQuantity(e.target.value)}
                required
              />
            </Form.Group>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleCloseModal}>
              Cancel
            </Button>
            <Button
              variant="primary"
              type="submit"
              disabled={modalLoading}
              className="save-btn"
            >
              {modalLoading ? <Spinner size="sm" /> : "Save Changes"}
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>
    </Container>
  );
};

export default StockManagement;
