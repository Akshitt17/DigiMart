
import apiFetch from './apiHelper';

// For Customers
export const getPaymentDetailsForOrder = (orderId) => {
  return apiFetch(`/api/v1/payments/order/${orderId}`);
};