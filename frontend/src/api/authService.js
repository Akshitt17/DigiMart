
import apiFetch from './apiHelper';

export const registerUser = (userData) => {
  return apiFetch('/api/v1/auth/register', {
    method: 'POST',
    body: JSON.stringify(userData),
  });
};

export const loginUser = (credentials) => {
  return apiFetch('/api/v1/auth/login', {
    method: 'POST',
    body: JSON.stringify(credentials),
  });
};