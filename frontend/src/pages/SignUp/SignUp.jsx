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
import styles from "./SignUp.module.scss";
import logo from "../../icons/logo.png";
import { useNavigate } from "react-router-dom";
import Loader from "../../components/Loader/Loader";
import AuthService from "src/services/AuthService";

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

const SignUp = () => {
  const navigate = useNavigate();
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const [firstNameError, setFirstNameError] = useState("");
  const [lastNameError, setLastNameError] = useState("");
  const [emailError, setEmailError] = useState("");
  const [phoneNumberError, setPhoneNumberError] = useState("");
  const [passwordError, setPasswordError] = useState("");
  const [confirmPasswordError, setConfirmPasswordError] = useState("");
  const [loading, setLoading] = useState(false);

  const validateData = () => {
    const regexpFirstName = /^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]{1,100}$/;
    const regexpLastName = /^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ -]{1,100}$/;
    const regexpEmail = /^[^@]+@[^@]+\.[^@]+$/;
    const regexpPhone = /^\d{9}$/;
    const regexpPassword =
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*]).{8,}$/;

    const firstNameTest = regexpFirstName.test(firstName);
    const lastNameTest = regexpLastName.test(lastName);
    const emailTest = regexpEmail.test(email);
    const phoneNumberTest = regexpPhone.test(phoneNumber);
    const passwordTest = regexpPassword.test(password);
    const confirmPasswordTest =
      regexpPassword.test(confirmPassword) && password === confirmPassword
        ? 1
        : 0;

    setFirstNameError(!firstName);
    setLastNameError(!lastNameError);
    setEmailError(!emailTest);
    setPhoneNumberError(!phoneNumberError);
    setPasswordError(!passwordError);
    setConfirmPasswordError(!confirmPasswordError);
    setConfirmPasswordError(!confirmPasswordError);

    return (
      firstNameTest &&
      lastNameTest &&
      emailTest &&
      phoneNumberTest &&
      passwordTest &&
      confirmPasswordTest
    );
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);
    if (!validateData()) {
      setLoading(false);
      return;
    }

    const dataToSend = {
      firstName: firstName,
      lastName: lastName,
      email: email,
      phoneNumber: phoneNumber,
      password: password,
      confirmPassword: confirmPassword,
    };
    try {
      const response = await AuthService.signUp(dataToSend);
      if (response.status === 200) {
        navigate("/signin");
      }
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div className={styles.signUpPage}>
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
              Panel rejestracji
            </Typography>
          </Box>

          <StyledForm onSubmit={handleSubmit}>
            <TextField
              className={`formInput ${firstNameError ? "error" : ""}`}
              placeholder="Jan"
              fullWidth
              label="Imię"
              variant="outlined"
              margin="normal"
              required
              autoFocus
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
            />

            <TextField
              className={`formInput ${lastNameError ? "error" : ""}`}
              placeholder="Nowak"
              fullWidth
              label="Nazwisko"
              variant="outlined"
              margin="normal"
              required
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
            />

            <TextField
              className={`formInput ${emailError ? "error" : ""}`}
              placeholder="name@name.com"
              fullWidth
              label="E-mail"
              variant="outlined"
              margin="normal"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />

            <TextField
              className={`formInput ${phoneNumberError ? "error" : ""}`}
              placeholder="501502503"
              fullWidth
              label="Numer telefonu"
              variant="outlined"
              margin="normal"
              required
              value={phoneNumber}
              onChange={(e) => setPhoneNumber(e.target.value)}
            />

            <TextField
              className={`formInput ${passwordError ? "error" : ""}`}
              placeholder="********"
              fullWidth
              label="Hasło"
              type="password"
              variant="outlined"
              margin="normal"
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />

            <TextField
              className={`formInput ${confirmPasswordError ? "error" : ""}`}
              placeholder="********"
              fullWidth
              label="Powtórz hasło"
              type="password"
              variant="outlined"
              margin="normal"
              required
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
            />

            <Button
              fullWidth
              variant="contained"
              color="primary"
              size="large"
              type="submit"
              sx={{ mt: 3 }}
            >
              {loading ? <Loader /> : "ZAREJESTRUJ SIĘ"}
            </Button>

            <Box textAlign="center" sx={{ mt: 3 }}>
              <Typography
                variant="h7"
                color="grey"
                sx={{ display: "flex", justifyContent: "center", gap: "1rem" }}
              >
                Masz już konto?
                <Link
                  href="/signin"
                  className={styles.link}
                  sx={{ textDecorationLine: "none" }}
                >
                  Zaloguj się
                </Link>
              </Typography>
            </Box>
          </StyledForm>
        </StyledContainer>
      </div>
    </div>
  );
};

export default SignUp;
