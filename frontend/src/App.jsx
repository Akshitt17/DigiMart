import { Routes, Route } from "react-router-dom";

import AppNavbar from "./components/AppNavbar";
import ProtectedRoute from "./components/ProtectedRoute";
import ProductDetailsPage from "./pages/Device/ProductDetailsPage";

import CustomerProfilePage from "./pages/Customer/CustomerProfilePage";
import OrdersPage from "./pages/Order/OrdersPage";
import CheckoutPage from "./pages/Checkout/CheckoutPage";

import InvoicePage from "./pages/Invoice/InvoicePage";
import HomePage from "./pages/HomePage/HomePage";
import LoginPage from "./pages/Login/LoginPage";
import RegisterPage from "./pages/Register/RegisterPage";

import AdminDashboard from "./pages/admin/AdminDashboard";
import DeviceManagement from "./pages/admin/Device/DeviceManagement";
import StockManagement from "./pages/admin/Stock/StockManagement";
import OrderManagement from "./pages/admin/Order/OrderManagement";

import Cart from "./pages/Cart/Cart";
import { Toaster } from "react-hot-toast";

import { Navigate } from "react-router-dom";
import { useAuth } from "./context/AuthContext";

const HomeRouteWrapper = () => {
  const { user } = useAuth();
  // If the user is an admin, redirect them away from the homepage.
  if (user && user.role === "ADMIN") {
    return <Navigate to="/admin/dashboard" replace />;
  }
  // Otherwise, show the homepage.
  return <HomePage />;
};
function App() {
  return (
    <>
      <Toaster
        position="top-left"
        toastOptions={{
          duration: 3000,
          style: {
            background: "#4CAF50",
            color: "#fff",
            fontWeight: "500",
            borderRadius: "8px",
            padding: "15px 20px",
          },
          success: {
            iconTheme: {
              primary: "#fff",
              secondary: "#4CAF50",
            },
          },
          error: {
            style: {
              background: "#f44336",
            },
            iconTheme: {
              primary: "#fff",
              secondary: "#f44336",
            },
          },
        }}
      />
      <AppNavbar />
      <main className="container-fluid px-0">
        <Routes>
          {/* =================================
              PUBLIC ROUTES
          ================================== */}
          <Route path="/" element={<HomeRouteWrapper />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/devices/:deviceId" element={<ProductDetailsPage />} />
          <Route path="/cart" element={<Cart />} />

          {/* =================================
              CUSTOMER PROTECTED ROUTES
          ================================== */}
          <Route
            path="/my-orders"
            element={
              <ProtectedRoute allowedRoles={["CUSTOMER"]}>
                <OrdersPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/profile"
            element={
              <ProtectedRoute allowedRoles={["CUSTOMER"]}>
                <CustomerProfilePage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/checkout"
            element={
              <ProtectedRoute allowedRoles={["CUSTOMER"]}>
                <CheckoutPage />
              </ProtectedRoute>
            }
          />

          <Route
            path="/invoice/:orderId"
            element={
              <ProtectedRoute allowedRoles={["CUSTOMER", "ADMIN"]}>
                <InvoicePage />
              </ProtectedRoute>
            }
          />

          {/* =================================
              ADMIN PROTECTED ROUTES
          ================================== */}
          <Route
            path="/admin/dashboard"
            element={
              <ProtectedRoute allowedRoles={["ADMIN"]}>
                <AdminDashboard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/devices"
            element={
              <ProtectedRoute allowedRoles={["ADMIN"]}>
                <DeviceManagement />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/stock"
            element={
              <ProtectedRoute allowedRoles={["ADMIN"]}>
                <StockManagement />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/orders"
            element={
              <ProtectedRoute allowedRoles={["ADMIN"]}>
                <OrderManagement />
              </ProtectedRoute>
            }
          />

          {/* Fallback Route for 404 Not Found */}
          <Route path="*" element={<h1>404: Page Not Found</h1>} />
        </Routes>
      </main>
    </>
  );
}

export default App;
