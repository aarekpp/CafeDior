import React, { useState, useEffect } from "react";
import {
  Box,
  Container,
  TextField,
  Button,
  Typography,
  Link,
} from "@mui/material";
import { styled } from "@mui/system";
import styles from "./Profile.module.scss";
import logo from "../../icons/logo.png";
import { useNavigate } from "react-router-dom";
import Loader from "../../components/Loader/Loader";
import { setUser } from "src/redux/AuthSlice";
import { useSelector, useDispatch } from "react-redux";
import axios from "axios";

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

const Profile = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { user } = useSelector((state) => state.auth);
  const [isEditEnable, setIsEditEnable] = useState(false);

  const [userData, setUserData] = useState({
    firstName: "",
    lastName: "",
    phoneNumber: "",
  });

  const [firstNameError, setFirstNameError] = useState("");
  const [lastNameError, setLastNameError] = useState("");
  const [phoneNumberError, setPhoneNumberError] = useState("");
  const [loading, setLoading] = useState(false);

  const validateData = () => {
    const regexpFirstName = /^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]{1,100}$/;
    const regexpLastName = /^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ -]{1,100}$/;
    const regexpPhone = /^\d{9}$/;

    const firstNameTest = regexpFirstName.test(userData.firstName);
    const lastNameTest = regexpLastName.test(userData.lastName);
    const phoneNumberTest = regexpPhone.test(userData.phoneNumber);

    setFirstNameError(!firstNameTest);
    setLastNameError(!lastNameTest);
    setPhoneNumberError(!phoneNumberTest);

    return firstNameTest && lastNameTest && phoneNumberTest;
  };

  const handleDataChange = (field, value) => {
    setUserData((prevState) => ({
      ...prevState,
      [field]: value,
    }));
  };

  useEffect(() => {
    if (user) {
      setUserData({
        firstName: user.firstName,
        lastName: user.lastName,
        phoneNumber: user.phoneNumber,
      });
    }
  }, [user, isEditEnable]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);
    if (!validateData()) {
      setLoading(false);
      return;
    }

    try {
      const response = await axios.put(
        `${import.meta.env.VITE_REACT_APP_API}/users/user`,
        userData
      );
      if (response.status === 200) {
        setIsEditEnable(false);
        dispatch(
          setUser({
            user: response.data,
          })
        );
      }
    } catch (error) {
      console.log(error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.profilePage}>
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
              Moje dane
            </Typography>
          </Box>

          <StyledForm onSubmit={handleSubmit}>
            <TextField
              className={`formInput ${firstNameError ? "error" : ""}`}
              fullWidth
              label="Imię"
              variant="outlined"
              margin="normal"
              disabled={!isEditEnable}
              value={userData.firstName}
              onChange={(e) => handleDataChange("firstName", e.target.value)}
            />

            <TextField
              className={`formInput ${lastNameError ? "error" : ""}`}
              fullWidth
              label="Nazwisko"
              variant="outlined"
              margin="normal"
              disabled={!isEditEnable}
              value={userData.lastName}
              onChange={(e) => handleDataChange("lastName", e.target.value)}
            />

            <TextField
              className="formInput"
              fullWidth
              label="E-mail"
              variant="outlined"
              margin="normal"
              disabled={true}
              value={user && user.email}
            />

            <TextField
              className={`formInput ${phoneNumberError ? "error" : ""}`}
              fullWidth
              label="Numer telefonu"
              variant="outlined"
              margin="normal"
              disabled={!isEditEnable}
              value={userData.phoneNumber}
              onChange={(e) => handleDataChange("phoneNumber", e.target.value)}
            />

            <div className={styles.buttonBox}>
              {!isEditEnable ? (
                <Button
                  variant="contained"
                  color="primary"
                  size="large"
                  type="button"
                  onClick={() => setIsEditEnable(true)}
                  sx={{ mt: 3 }}
                >
                  Edytuj
                </Button>
              ) : (
                <>
                  <Button
                    variant="contained"
                    color="primary"
                    size="large"
                    type="button"
                    onClick={() => setIsEditEnable(true)}
                    sx={{ mt: 3 }}
                  >
                    Anuluj
                  </Button>

                  <Button
                    variant="contained"
                    color="primary"
                    size="large"
                    type="submit"
                    sx={{ mt: 3 }}
                  >
                    {loading ? <Loader /> : "Zapisz zmiany"}
                  </Button>
                </>
              )}
            </div>
          </StyledForm>
        </StyledContainer>
      </div>
    </div>
  );
};

export default Profile;
