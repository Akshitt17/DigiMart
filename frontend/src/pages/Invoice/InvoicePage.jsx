import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { getOrderById } from "../../api/orderService";
import {
  getCustomerByCustomerId,
  getCustomerAddresses,
} from "../../api/customerService";
import { getDeviceById } from "../../api/deviceService";
import { getInvoiceByOrderId } from "../../api/reportService";
import {
  Container,
  Card,
  Table,
  Spinner,
  Alert,
  Button,
  Row,
  Col,
} from "react-bootstrap";

const InvoicePage = () => {
  const { orderId } = useParams();
  const [order, setOrder] = useState(null);
  const [customer, setCustomer] = useState(null);
  const [deliveryAddress, setDeliveryAddress] = useState(null);
  const [deviceNames, setDeviceNames] = useState({});
  const [invoice, setInvoice] = useState(null); // State for invoice details
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchInvoiceData = async () => {
      if (!orderId) {
        setError("No Order ID provided.");
        setLoading(false);
        return;
      }
      try {
        // Fetch all data in parallel for better performance
        const [orderData, invoiceData] = await Promise.all([
          getOrderById(orderId),
          getInvoiceByOrderId(orderId), // Fetch invoice from the backend
        ]);

        setOrder(orderData);
        setInvoice(invoiceData);

        const [customerData, allAddresses] = await Promise.all([
          getCustomerByCustomerId(orderData.customerId),
          getCustomerAddresses(orderData.customerId),
        ]);

        setCustomer(customerData);
        const matchedAddress = allAddresses.find(
          (addr) => addr.addressId === orderData.deliveryAddressId
        );
        setDeliveryAddress(matchedAddress);

        if (orderData && orderData.orderItems) {
          const devicePromises = orderData.orderItems.map((item) =>
            getDeviceById(item.deviceId)
          );
          const deviceDetails = await Promise.all(devicePromises);
          const namesMap = deviceDetails.reduce((acc, device) => {
            acc[device.deviceId] = device.deviceName;
            return acc;
          }, {});
          setDeviceNames(namesMap);
        }
      } catch (err) {
        setError(err.data?.message || "Failed to load invoice details.");
      } finally {
        setLoading(false);
      }
    };
    fetchInvoiceData();
  }, [orderId]);

  const handlePrint = () => {
    window.print();
  };

  if (loading)
    return (
      <div className="text-center py-5">
        <Spinner animation="border" />
      </div>
    );
  if (error) return <Alert variant="danger">{error}</Alert>;
  if (!order || !customer || !deliveryAddress || !invoice)
    return (
      <Alert variant="warning">Could not retrieve all invoice details.</Alert>
    );

  return (
    <Container className="my-4">
      <Card className="p-4 p-md-5">
        <div className="d-flex justify-content-between align-items-start mb-4">
          <div>
            <h1 className="mb-0">Invoice</h1>
            <p className="text-muted">Order #{order.orderId}</p>
          </div>
          <div className="text-end">
            <h4 className="mb-0">Smart Home V2</h4>
            <p className="mb-0 text-muted">Cognizant, Chennai, India</p>
          </div>
        </div>

        <Row className="mb-4">
          <Col md={6}>
            <h5>Billed To:</h5>
            <p className="mb-0">{customer.fullName}</p>
            <p className="mb-0">{customer.email}</p>
            <p className="mb-0">{customer.phone}</p>
          </Col>
          <Col md={6} className="text-md-end">
            <h5>Shipped To:</h5>
            <p className="mb-0">{deliveryAddress.streetAddress}</p>
            <p className="mb-0">
              {deliveryAddress.city}, {deliveryAddress.state}{" "}
              {deliveryAddress.postalCode}
            </p>
          </Col>
        </Row>

        <div className="d-flex justify-content-between text-muted mb-4">
          <span>
            <strong>Date:</strong>{" "}
            {new Date(invoice.createdAt).toLocaleDateString()}
          </span>
          <span>
            <strong>Invoice No:</strong> {invoice.invoiceNumber}
          </span>
        </div>

        <Table striped bordered hover responsive>
          <thead className="bg-light">
            <tr>
              <th>#</th>
              <th>Item</th>
              <th className="text-center">Quantity</th>
              <th className="text-end">Unit Price</th>
              <th className="text-end">Total</th>
            </tr>
          </thead>
          <tbody>
            {order.orderItems.map((item, index) => (
              <tr key={item.orderItemId}>
                <td>{index + 1}</td>
                <td>{item.deviceName}</td>
                <td className="text-center">{item.quantity}</td>
                <td className="text-end">
                  ₹{item.price.toLocaleString("en-IN")}
                </td>
                <td className="text-end">
                  ₹{(item.quantity * item.price).toLocaleString("en-IN")}
                </td>
              </tr>
            ))}
          </tbody>
          <tfoot>
            <tr>
              <td colSpan="4" className="text-end border-0">
                <strong>Subtotal</strong>
              </td>
              <td className="text-end border-0">
                ₹{order.totalAmount.toLocaleString("en-IN")}
              </td>
            </tr>
            <tr>
              <td colSpan="4" className="text-end border-0">
                <strong>Shipping</strong>
              </td>
              <td className="text-end border-0">FREE</td>
            </tr>
            <tr className="fw-bold bg-light">
              <td colSpan="4" className="text-end">
                <h5>Total Amount</h5>
              </td>
              <td className="text-end">
                <h5>₹{order.totalAmount.toLocaleString("en-IN")}</h5>
              </td>
            </tr>
          </tfoot>
        </Table>

        <div className="text-center mt-4">
          <p className="text-muted">Thank you for your purchase!</p>
          <Button
            variant="primary"
            onClick={handlePrint}
            className="d-print-none"
          >
            Print Invoice
          </Button>
        </div>
      </Card>
    </Container>
  );
};

export default InvoicePage;
