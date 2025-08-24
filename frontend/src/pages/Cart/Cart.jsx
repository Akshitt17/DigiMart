
import React from "react";
import { useNavigate } from "react-router-dom";
import { Button, Table, Alert, Card } from "react-bootstrap";

import "./Cart.css"; 
import { useCart } from "../../context/CartContext";

const Cart = () => {
  const { items = [], removeItem, clearCart, updateQuantity } = useCart();
  const navigate = useNavigate();

  const handleCheckout = () => {
    navigate("/checkout");
  };

  const totalPrice = items.reduce(
    (total, item) => total + (item.price || 0) * (item.quantity || 0),
    0
  );

  if (items.length === 0) {
    return (
      <div className="cart-page-container">
        <Card className="cart-empty-card shadow-lg">
          <Card.Body className="text-center">
            <h3 className="mb-3">🛒 Your cart is empty</h3>
            <p>Add some devices to your cart to see them here.</p>
            <Button variant="primary" onClick={() => navigate("/")}>
              Browse Products
            </Button>
          </Card.Body>
        </Card>
      </div>
    );
  }

  return (
    <div className="cart-page-container">
      <Card className="cart-card shadow-lg p-3">
        <h2 className="mb-4">🛒 Your Cart</h2>
        <Table striped hover responsive className="mb-4">
          <thead>
            <tr>
              <th>Device</th>
              <th>Price</th>
              <th>Qty</th>
              <th>Total</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {items.map((item, idx) => (
              <tr key={idx}>
                <td>{item.deviceName}</td>
                <td>₹{(item.price || 0).toFixed(2)}</td>
                <td>
                  <input
                    type="number"
                    min="1"
                    value={item.quantity || 1}
                    onChange={(e) =>
                      updateQuantity(
                        item.deviceId,
                        parseInt(e.target.value, 10)
                      )
                    }
                    className="cart-qty-input"
                  />
                </td>
                <td>
                  ₹{((item.price || 0) * (item.quantity || 0)).toFixed(2)}
                </td>
                <td>
                  <Button
                    variant="danger"
                    size="sm"
                    onClick={() => removeItem(item.deviceId)}
                  >
                    Remove
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>

        <div className="d-flex justify-content-between align-items-center">
          <h4>Total: ₹{totalPrice.toFixed(2)}</h4>
          <div>
            <Button variant="secondary" className="me-2" onClick={clearCart}>
              Clear Cart
            </Button>
            <Button variant="success" onClick={handleCheckout}>
              Proceed to Checkout
            </Button>
          </div>
        </div>
      </Card>
    </div>
  );
};

export default Cart;
