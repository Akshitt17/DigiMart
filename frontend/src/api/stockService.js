import apiFetch from "./apiHelper";

// For Admins
export const getAllStock = () => {
  return apiFetch("/api/v1/stock");
};

// For Admins
export const updateStock = (deviceId, quantityData) => {
  return apiFetch(`/api/v1/stock/device/${deviceId}`, {
    method: "PUT",
    body: JSON.stringify(quantityData),
  });
};

// Public or Customer
export const checkStockForDevice = (deviceId) => {
  return apiFetch(`/api/v1/stock/device/${deviceId}`);
};
