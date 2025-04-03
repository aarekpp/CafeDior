import React from "react";
import Logo from "../../icons/logo.png";

import styles from "./Header.module.scss";
import {
  Box,
  Container,
  TextField,
  Button,
  Typography,
  Link,
} from "@mui/material";

const Header = () => {
  return (
    <div className={styles.container}>
      <Box textAlign="center">
        <img src={Logo} alt="CafeDior logo" className={styles.logo} />
      </Box>

      <div className={styles.buttons}>
        <Button
          fullWidth
          variant="outlined"
          type="submit"
          color="white"
          className={styles.loginButton}
          sx={{
            border: "2px solid",
            borderColor: "primary.main",
            borderRadius: ".5rem",
          }}
        >
          <Link
            href="/signin"
            className={styles.link}
            sx={{
              textDecorationLine: "none",
              color: "primary",
              fontSize: "12px",
              fontWeight: "bold",
            }}
          >
            Zaloguj się
          </Link>
        </Button>

        <Button
          fullWidth
          variant="contained"
          type="submit"
          className={styles.registerButton}
        >
          <Link
            href="/signup"
            className={styles.link}
            sx={{
              textDecorationLine: "none",
              color: "white",
              fontSize: "12px",
              fontWeight: "bold",
            }}
          >
            Zarejestruj się
          </Link>
        </Button>
      </div>
    </div>
  );
};
export default Header;
