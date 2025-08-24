import apiFetch from "./apiHelper";

export const getInvoiceByOrderId = (orderId) => {
  return apiFetch(`/api/v1/reports/invoice/order/${orderId}`);
};

export const getCustomerOrdersReport = (customerId) => {
  return apiFetch(`/api/v1/reports/customer/${customerId}/orders`);
};
