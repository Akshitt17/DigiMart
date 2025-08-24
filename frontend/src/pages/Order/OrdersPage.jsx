// import React, { useState, useEffect } from "react";
// import { useAuth } from "../../context/AuthContext";

// import { Container, Accordion, Spinner, Alert } from "react-bootstrap";
// import OrderSummary from "../../components/OrderSummary";
// import { getAllOrdersByCustomerId } from "../../api/orderService";
// import { getCustomerByUserId } from "../../api/customerService";

// const OrdersPage = () => {
//   const { user } = useAuth();
//   const [orders, setOrders] = useState([]);
//   const [loading, setLoading] = useState(true);
//   const [error, setError] = useState("");

//   useEffect(() => {
//     const fetchOrders = async () => {
//       if (!user?.userId) return;
//       try {
//         console.log(user);
//         const customerResponse = await getCustomerByUserId(user.userId);
//         const data = await getAllOrdersByCustomerId(
//           customerResponse.customerId
//         );
//         setOrders(data);
//         console.log(data);
//       } catch (err) {
//         setError(err.data?.message || "Failed to load order history.");
//       } finally {
//         setLoading(false);
//       }
//     };

//     fetchOrders();
//   }, [user]);

//   if (loading) {
//     return (
//       <div className="text-center py-5">
//         <Spinner animation="border" />
//       </div>
//     );
//   }

//   if (error) {
//     return <Alert variant="danger">{error}</Alert>;
//   }

//   return (
//     <Container>
//       <h1 className="mb-4">My Order History</h1>
//       {orders.length > 0 ? (
//         <Accordion defaultActiveKey="0">
//           {orders.map((order, index) => (
//             <Accordion.Item eventKey={String(index)} key={order.orderId}>
//               <Accordion.Header>
//                 Order #{orders.length - index} -{" "}
//                 {new Date(order.orderDate).toLocaleDateString()} - ₹
//                 {order.totalAmount.toLocaleString("en-IN")}
//               </Accordion.Header>
//               <Accordion.Body>
//                 <OrderSummary order={order} />
//               </Accordion.Body>
//             </Accordion.Item>
//           ))}
//         </Accordion>
//       ) : (
//         <Alert variant="info">You have not placed any orders yet.</Alert>
//       )}
//     </Container>
//   );
// };

// export default OrdersPage;

import React, { useState, useEffect } from "react";
import { useAuth } from "../../context/AuthContext";
import { getCustomerByUserId } from "../../api/customerService"; // Added missing import
import { getCustomerOrdersReport } from "../../api/reportService";
import { updateOrderStatus } from "../../api/orderService";
import {
  Container,
  Accordion,
  Spinner,
  Alert,
  Button,
  Modal,
} from "react-bootstrap";
import OrderSummary from "../../components/OrderSummary";
import toast from "react-hot-toast";

const OrdersPage = () => {
  const { user } = useAuth();
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // State for the cancel confirmation modal
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [orderToCancel, setOrderToCancel] = useState(null);
  const [cancelLoading, setCancelLoading] = useState(false);
  const [cancelError, setCancelError] = useState("");

  const fetchOrders = async () => {
    if (!user?.userId) {
      setLoading(false);
      return;
    }
    try {
      setLoading(true);
      // Step 1: Fetch the customer profile to get the customerId
      const customerData = await getCustomerByUserId(user.userId);
      // Step 2: Use the customerId to fetch the orders
      const orderData = await getCustomerOrdersReport(customerData.customerId);
      setOrders(orderData);
    } catch (err) {
      setError(err.data?.message || "Failed to load order history.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, [user]);

  const openCancelConfirm = (order) => {
    setOrderToCancel(order);
    setShowCancelModal(true);
    setCancelError("");
  };

  const closeCancelConfirm = () => {
    setOrderToCancel(null);
    setShowCancelModal(false);
  };

  const handleCancelOrder = async () => {
    if (!orderToCancel) return;
    setCancelLoading(true);
    setCancelError("");
    try {
      await updateOrderStatus(orderToCancel.orderId, "CANCELLED");
      toast.success(`Order #${orderToCancel.orderId} has been cancelled.`);
      closeCancelConfirm();
      fetchOrders(); // Refresh the order list
    } catch (err) {
      setCancelError(err.data?.message || "Failed to cancel the order.");
    } finally {
      setCancelLoading(false);
    }
  };

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
      <h1 className="mb-4">My Order History</h1>
      {orders.length > 0 ? (
        <Accordion defaultActiveKey="0">
          {orders.map((order, index) => (
            <Accordion.Item eventKey={String(index)} key={order.orderId}>
              <Accordion.Header>
                Order #{order.orderId} -{" "}
                {new Date(order.orderDate).toLocaleDateString()} - ₹
                {order.totalAmount.toLocaleString("en-IN")}
              </Accordion.Header>
              <Accordion.Body>
                <OrderSummary order={order} />
                {/* Show cancel button only if the order is not already cancelled or delivered */}
                {order.status !== "CANCELLED" &&
                  order.status !== "DELIVERED" && (
                    <div className="text-end mt-3">
                      <Button
                        variant="outline-danger"
                        onClick={() => openCancelConfirm(order)}
                      >
                        Cancel Order
                      </Button>
                    </div>
                  )}
              </Accordion.Body>
            </Accordion.Item>
          ))}
        </Accordion>
      ) : (
        <Alert variant="info">You have not placed any orders yet.</Alert>
      )}

      {/* Cancel Confirmation Modal */}
      <Modal show={showCancelModal} onHide={closeCancelConfirm} centered>
        <Modal.Header closeButton>
          <Modal.Title>Confirm Cancellation</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {cancelError && <Alert variant="danger">{cancelError}</Alert>}
          Are you sure you want to cancel{" "}
          <strong>Order #{orderToCancel?.orderId}</strong>?
          <p className="text-muted mt-2">This action cannot be undone.</p>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={closeCancelConfirm}>
            Back
          </Button>
          <Button
            variant="danger"
            onClick={handleCancelOrder}
            disabled={cancelLoading}
          >
            {cancelLoading ? <Spinner size="sm" /> : "Yes, Cancel Order"}
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
};

export default OrdersPage;
