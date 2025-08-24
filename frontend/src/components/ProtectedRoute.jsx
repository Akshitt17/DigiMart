import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const ProtectedRoute = ({ children, allowedRoles }) => {
  const { isAuthenticated, user } = useAuth();
  const location = useLocation();

  if (!isAuthenticated) {
    // Redirect to login page, saving the current location they were trying to go to
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  // If roles are specified, check if the user has one of the allowed roles
  if (allowedRoles && !allowedRoles.includes(user?.role)) {
    // User is logged in but doesn't have permission
    // You can redirect to an "Unauthorized" page or back to home
    return <Navigate to="/" replace />;
  }

  return children;
};

export default ProtectedRoute;
