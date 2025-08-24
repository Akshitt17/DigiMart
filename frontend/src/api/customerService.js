
import apiFetch from "./apiHelper";

// After a user has a JWT token, they register their customer profile
export const registerCustomerProfile = (customerData) => {
  return apiFetch("/api/v1/customers/register", {
    method: "POST",
    body: JSON.stringify(customerData),
  });
};

// Add a new address for a specific customer
export const addCustomerAddress = (customerId, addressData) => {
  return apiFetch(`/api/v1/customers/${customerId}/addresses`, {
    method: "POST",
    body: JSON.stringify(addressData),
  });
};

// Get all addresses for a specific customer
export const getCustomerAddresses = (customerId) => {
  return apiFetch(`/api/v1/customers/${customerId}/addresses`);
};

export const getCustomerByUserId = (userId) => {
  return apiFetch(`/api/v1/customers/by-user/${userId}`);
};

export const getCustomerByCustomerId = (customerId) => {
  return apiFetch(`/api/v1/customers/${customerId}`);
};


export const updateCustomer = (customerId, customerData) => {
  return apiFetch(`/api/v1/customers/${customerId}`, {
    method: "PUT",
    body: JSON.stringify(customerData),
  });
};
