import apiFetch from "./apiHelper";

export const getAllDevices = () => {
  return apiFetch("/api/v1/devices/getAll");
};

export const getDeviceById = (deviceId) => {
  return apiFetch(`/api/v1/devices/getBy/${deviceId}`);
};

export const createDevice = (deviceData) => {
  return apiFetch("/api/v1/devices", {
    method: "POST",
    body: JSON.stringify(deviceData),
  });
};

export const deleteDevice = (deviceId) => {
  return apiFetch(`/api/v1/devices/${deviceId}`, { method: "DELETE" });
};

export const searchDevices = (query) => {
  return apiFetch(`/api/v1/devices/search?name=${query}`);
};



export const updateDevice = (deviceId, deviceData) => {
  return apiFetch(`/api/v1/devices/${deviceId}`, {
    method: "PUT",
    body: JSON.stringify(deviceData),
  });
};
