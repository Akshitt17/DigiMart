import React, { createContext, useState, useContext, useEffect } from "react";
import { loginUser as apiLogin } from "../api/authService";

const AuthContext = createContext(null);

// Helper function to safely parse JSON from localStorage
const getInitialState = (key) => {
  try {
    const item = localStorage.getItem(key);
    // Return null if item is null, undefined, or the string "undefined"
    if (item === null || item === "undefined") {
      return null;
    }
    return JSON.parse(item);
  } catch (error) {
    console.error(`Error parsing JSON from localStorage key "${key}":`, error);
    return null;
  }
};

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(localStorage.getItem("jwt_token"));
  const [user, setUser] = useState(getInitialState("user_data")); // Use the safe function

  useEffect(() => {
    if (token) {
      localStorage.setItem("jwt_token", token);
      // Only stringify user if it's not null
      if (user) {
        localStorage.setItem("user_data", JSON.stringify(user));
      }
    } else {
      localStorage.removeItem("jwt_token");
      localStorage.removeItem("user_data");
    }
  }, [token, user]);

  const login = async (username, password) => {
    try {
      const data = await apiLogin({ username, password });
      if (data.token) {
        setToken(data.token);
        setUser({
          userId: data.userId,
          username: data.username,
          role: data.role,
        });
        return data;
      }
    } catch (error) {
      console.error("Login failed:", error);
      throw error;
    }
  };

  const logout = () => {
    setToken(null);
    setUser(null);
  };

  const value = {
    token,
    user,
    isAuthenticated: !!token,
    login,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};


export const useAuth = () => {
  return useContext(AuthContext);
};
