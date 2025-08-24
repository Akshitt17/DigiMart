import apiFetch from "./apiHelper";

export const createOrder = (orderData) => {
  return apiFetch("/api/v1/orders/create", {
    method: "POST",
    body: JSON.stringify(orderData),
  });
};

export const getAllOrders = () => {
  return apiFetch("/api/v1/orders");
};

export const getAllOrdersByCustomerId = (customerId) => {
  return apiFetch(`/api/v1/orders/customer/${customerId}`);
};

export const getOrderById = (orderId) => {
  return apiFetch(`/api/v1/orders/${orderId}`);
};

export const updateOrderStatus = (orderId, status) => {
  return apiFetch(`/api/v1/orders/${orderId}/status?status=${status}`, {
    method: "PUT",
  });
};
