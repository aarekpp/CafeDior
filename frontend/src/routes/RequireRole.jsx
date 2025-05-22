import React from "react";
import { useSelector } from "react-redux";
import { Navigate, Outlet } from "react-router";

export default function RequireRole({ allowedRoles }) {
  const { isLoggedIn, role } = useSelector((state) => state.auth);
  if (!isLoggedIn) return <Navigate to="/signin" replace />;
  return allowedRoles.includes(role) ? <Outlet /> : <Navigate to="/" replace />;
}
