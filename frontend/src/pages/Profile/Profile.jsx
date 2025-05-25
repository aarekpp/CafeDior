import React, { useState, useEffect } from "react";
import { Box, Container, TextField, Button, Typography } from "@mui/material";
import { styled } from "@mui/system";
import styles from "./Profile.module.scss";
import logo from "../../icons/logo.png";
import { useNavigate } from "react-router-dom";
import Loader from "../../components/Loader/Loader";
import { useSelector } from "react-redux";
import UserService from "src/services/UserService";

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
  const navigate = useNavigate();
  const userId = useSelector((state) => state.auth.user);
  const [isEditEnable, setIsEditEnable] = useState(false);
  const [originalData, setOriginalData] = useState({
    firstName: "",
    lastName: "",
    phoneNumber: "",
    email: "",
  });
  const [userData, setUserData] = useState(originalData);

  const [firstNameError, setFirstNameError] = useState("");
  const [lastNameError, setLastNameError] = useState("");
  const [phoneNumberError, setPhoneNumberError] = useState("");
  const [isDataLoaded, setIsDataLoaded] = useState(false);
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

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);
    if (!validateData()) {
      setLoading(false);
      return;
    }

    const dataToSend = {
      firstName: userData.firstName,
      lastName: userData.lastName,
      phoneHumber: userData.phoneNumber,
    };

    const response = await UserService.updateUserData(userId, dataToSend);
    if (response?.status === 200) {
      setUserData((prev) => ({
        ...prev,
        firstName: response.data.firstName,
        lastName: response.data.lastName,
        phoneNumber: response.data.phoneHumber,
      }));
      setIsEditEnable(false);
    } else {
      setUserData(originalData);
    }
    setLoading(false);
  };

  useEffect(() => {
    const getUserData = async () => {
      const response = await UserService.getUserData(userId);
      if (response?.status === 200) {
        const data = {
          firstName: response.data.firstName,
          lastName: response.data.lastName,
          email: response.data.email,
          phoneNumber: response.data.phoneNumber,
        };
        setOriginalData(data);
        setUserData(data);
      }
      setIsDataLoaded(true);
    };

    if (userId !== null) {
      getUserData();
    } else {
      navigate("/");
    }
  }, [userId, navigate]);

  if (!isDataLoaded) return <Loader />;

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
              value={userData.email}
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
                    onClick={() => setIsEditEnable(false)}
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
