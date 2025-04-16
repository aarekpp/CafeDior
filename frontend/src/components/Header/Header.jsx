import React, { useState } from "react";
import Logo from "../../icons/logo.png";
import { useNavigate } from "react-router";
import styles from "./Header.module.scss";
import { Box, Button, Typography, Menu, MenuItem } from "@mui/material";

const Header = () => {
  const navigate = useNavigate();
  const [isSignIn, setIsSignIn] = useState(true);
  const [anchorEl, setAnchorEl] = useState(null);

  const handleMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = () => {
    setIsSignIn(false);
    handleMenuClose();
    navigate("/");
  };

  return (
    <div className={styles.container}>
      <Box textAlign="center">
        <img src={Logo} alt="CafeDior logo" className={styles.logo} />
      </Box>
      <div className={styles.bookmarks}>
        <Typography onClick={() => navigate("/")}>O nas</Typography>
        <Typography onClick={() => navigate("/")}>
          Nasze specjalności
        </Typography>
        <Typography onClick={() => navigate("/")}>Menu</Typography>
        <Typography onClick={() => navigate("/")}>Kontakt</Typography>
      </div>

      {isSignIn ? (
        <div className={styles.loggedInSection}>
          <Button
            variant="contained"
            className={styles.reservationButton}
            onClick={() => navigate("/reservation")}
          >
            Złóż rezerwację
          </Button>

          <Button
            aria-controls="profile-menu"
            aria-haspopup="true"
            onClick={handleMenuOpen}
            className={styles.profileButton}
          >
            Mój profil
          </Button>

          <Menu
            id="profile-menu"
            anchorEl={anchorEl}
            keepMounted
            open={Boolean(anchorEl)}
            onClose={handleMenuClose}
          >
            <MenuItem
              onClick={() => {
                navigate("/profile");
                handleMenuClose();
              }}
              sx={{ color: "#9f7438" }}
            >
              Moje dane
            </MenuItem>
            <MenuItem
              onClick={() => {
                navigate("/history");
                handleMenuClose();
              }}
              sx={{ color: "#9f7438" }}
            >
              Historia rezerwacji
            </MenuItem>
            <MenuItem onClick={handleLogout} sx={{ color: "#9f7438" }}>
              Wyloguj
            </MenuItem>
          </Menu>
        </div>
      ) : (
        <div className={styles.buttons}>
          <Button
            className={styles.loginButton}
            fullWidth
            type="button"
            color="white"
            onClick={() => navigate("/signin")}
          >
            Zaloguj się
          </Button>

          <Button
            className={styles.registerButton}
            fullWidth
            type="button"
            onClick={() => navigate("/signup")}
          >
            Zarejestruj się
          </Button>
        </div>
      )}
    </div>
  );
};
export default Header;
