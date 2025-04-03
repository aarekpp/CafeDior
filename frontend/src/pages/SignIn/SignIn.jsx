import React, { useState } from "react";
import {
  Box,
  Container,
  TextField,
  Button,
  Typography,
  Link,
} from "@mui/material";
import { styled } from "@mui/system";
import styles from "./SignIn.module.scss";
import logo from "../../icons/logo.png";
import { useNavigate } from "react-router-dom";
import Loader from "../../components/Loader/Loader";

const StyledContainer = styled(Container)({
  display: "flex",
  flexDirection: "column",
  justifyContent: "center",
  alignItems: "center",
  padding: "2rem 0",
  borderRadius: "1rem",
  boxShadow: "0px 0px 15px 0px rgba(66, 68, 90, 1)",
});

const StyledForm = styled("form")({
  width: "100%",
});

const SignIn = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [emailError, setEmailError] = useState("");
  const [passwordError, setPasswordError] = useState("");
  const [loading, setLoading] = useState(false);

  const validateData = () => {
    const emailTest = email.length > 0 ? 1 : 0;
    const passwordTest = password.length >= 10 ? 1 : 0;
    setEmailError(!emailTest);
    setPasswordError(!passwordTest);
    return emailTest && passwordTest;
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);
    if (!validateData()) {
      setLoading(false);
      return;
    }
  };

  return (
    <div className={styles.signInPage}>
      <div className={styles.container}>
        <StyledContainer>
          <Box textAlign="center">
            <img
              src={logo}
              alt="CafeDior logo"
              className={styles.logo}
              onClick={() => navigate("/")}
            />
            <Typography variant="h5" color="grey">
              Panel logowania
            </Typography>
          </Box>

          <StyledForm onSubmit={handleSubmit}>
            <TextField
              className={`formInput ${emailError ? "error" : ""}`}
              placeholder="name@name.com"
              fullWidth
              label="E-mail"
              variant="outlined"
              margin="normal"
              required
              autoFocus
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />

            <TextField
              className={`formInput ${passwordError ? "error" : ""}`}
              placeholder="********"
              fullWidth
              label="Password"
              type="password"
              variant="outlined"
              margin="normal"
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />

            <Box textAlign="center" sx={{ my: 2 }}>
              <Link
                href="#"
                variant="body2"
                className={styles.link}
                sx={{ textDecorationLine: "none" }}
              >
                Przypomnij hasło
              </Link>
            </Box>

            <Button
              fullWidth
              variant="contained"
              color="primary"
              size="large"
              type="submit"
            >
              {loading ? <Loader /> : "ZALOGUJ SIĘ"}
            </Button>

            <Box textAlign="center" sx={{ mt: 3 }}>
              <Typography
                variant="h7"
                color="grey"
                sx={{ display: "flex", justifyContent: "center", gap: "1rem" }}
              >
                Nie masz konta?
                <Link
                  href="/signup"
                  className={styles.link}
                  sx={{ textDecorationLine: "none" }}
                >
                  Zarejestruj się
                </Link>
              </Typography>
            </Box>
          </StyledForm>
        </StyledContainer>
      </div>
    </div>
  );
};

export default SignIn;
