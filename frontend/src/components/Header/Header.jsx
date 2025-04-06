import React from "react";
import Logo from "../../icons/logo.png";
import { useNavigate } from "react-router";
import styles from "./Header.module.scss";
import { Box, Button, Typography } from "@mui/material";

const Header = () => {
  const navigate = useNavigate();
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
    </div>
  );
};
export default Header;
