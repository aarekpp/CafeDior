import React, { useEffect } from "react";
import { useSelector } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";

export default function RoutesRedirect() {
  const navigate = useNavigate();
  const location = useLocation();
  const { isLoggedIn, role } = useSelector((state) => state.auth);

  useEffect(() => {
    const shoudRedirect =
      isLoggedIn && role === "ADMIN" && !location.pathname.startsWith("/admin");
    if (shoudRedirect) {
      navigate("/admin", { replace: true });
    }
  });
}
