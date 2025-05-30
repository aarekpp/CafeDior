import React from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { logout } from "src/redux/AuthSlice";
import AuthService from "src/services/AuthService";
import styles from "./AdminPanel.module.scss";

export default function AdminPanel() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleLogout = async () => {
    try {
      const response = await AuthService.logout();
      if (response.status === 200) {
        dispatch(logout());
        navigate("/");
      }
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div className={styles.header}>
      <h1>Panel administratora</h1>
      <button className={styles.logoutButton} onClick={handleLogout}>
        Wyloguj
      </button>
    </div>
  );
}
