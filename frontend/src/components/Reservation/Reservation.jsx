import React, { useState } from "react";
import {
  Box,
  Container,
  Button,
  Typography,
  Select,
  MenuItem,
  InputLabel,
  FormControl,
} from "@mui/material";
import { styled } from "@mui/system";
import { LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import { DatePicker, TimePicker } from "@mui/x-date-pickers";
import dayjs from "dayjs";
import { useNavigate } from "react-router-dom";
import Loader from "../../components/Loader/Loader";
import styles from "./Reservation.module.scss";
import logo from "../../icons/logo.png";
import ReservationService from "src/services/ReservationService";
import "dayjs/locale/pl";
import customParseFormat from "dayjs/plugin/customParseFormat";
import localizedFormat from "dayjs/plugin/localizedFormat";

dayjs.extend(customParseFormat);
dayjs.extend(localizedFormat);
dayjs.locale("pl");

const StyledContainer = styled(Container)({
  display: "flex",
  flexDirection: "column",
  justifyContent: "center",
  alignItems: "center",
  padding: "2rem 0",
  borderRadius: "1rem",
  boxShadow: "0px 0px 15px 0px rgba(66, 68, 90, 0.2)",
});

const StyledForm = styled("form")({
  width: "100%",
  padding: "0 2rem",
});

export default function Reservation() {
  const navigate = useNavigate();
  const [selectedDate, setSelectedDate] = useState(null);
  const [selectedTime, setSelectedTime] = useState(null);
  const [people, setPeople] = useState("");
  const [loading, setLoading] = useState(false);
  const [dateError, setDateError] = useState(false);
  const [timeError, setTimeError] = useState(false);
  const [peopleError, setPeopleError] = useState(false);

  const isWeekend = (date) => [0, 6].includes(date.day());

  const validateData = () => {
    const dateValid = !!selectedDate;
    const timeValid = !!selectedTime;
    const peopleValid = !!people;

    setDateError(!dateValid);
    setTimeError(!timeValid);
    setPeopleError(!peopleValid);

    return dateValid && timeValid && peopleValid;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    if (!validateData()) {
      setLoading(false);
      return;
    }

    const dataToSend = {
      reservationDate: selectedDate ? selectedDate.format("DD/MM/YYYY") : "",
      reservationTime: selectedTime ? selectedTime.format("HH:mm") : "",
      people: Number(people),
    };

    const response = await ReservationService.createReservation(dataToSend);

    if (response?.status === 201) {
      navigate("/", { replace: true });
    }
  };

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <div className={styles.reservationPage}>
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
                Rezerwacja stolika
              </Typography>
            </Box>

            <StyledForm onSubmit={handleSubmit}>
              <Box sx={{ my: 2 }}>
                <DatePicker
                  label="Data rezerwacji"
                  value={selectedDate}
                  onChange={(newValue) => setSelectedDate(newValue)}
                  minDate={dayjs()}
                  format="DD/MM/YYYY"
                  slotProps={{
                    popper: {
                      disablePortal: false,
                    },
                    textField: {
                      variant: "outlined",
                      fullWidth: true,
                      error: dateError,
                      helperText: dateError ? "Wybierz datę" : "",
                      autoFocus: false,
                    },
                  }}
                />
              </Box>

              <Box sx={{ my: 2 }}>
                <TimePicker
                  label="Godzina rozpoczęcia"
                  value={selectedTime}
                  onChange={(newValue) => setSelectedTime(newValue)}
                  ampm={false}
                  minutesStep={15}
                  shouldDisableTime={(value, view) => {
                    if (!selectedDate) return true;
                    const minHour = isWeekend(selectedDate) ? 9 : 8;
                    const maxHour = isWeekend(selectedDate) ? 20 : 19;

                    if (view === "hours") {
                      return value.hour() < minHour || value.hour() >= maxHour;
                    }
                    return false;
                  }}
                  slotProps={{
                    textField: {
                      variant: "outlined",
                      fullWidth: true,
                      error: timeError,
                      helperText: timeError ? "Wybierz godzinę" : "",
                    },
                  }}
                />
              </Box>

              <Box sx={{ my: 2 }}>
                <FormControl fullWidth>
                  <InputLabel id="demo-simple-select-label">
                    Liczba osób
                  </InputLabel>
                  <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    value={people}
                    onChange={(e) => setPeople(e.target.value)}
                    fullWidth
                    error={peopleError}
                    label="Liczba osób"
                  >
                    {[...Array(10)].map((_, i) => (
                      <MenuItem key={i + 1} value={i + 1}>
                        {i + 1}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
                {peopleError && (
                  <Typography variant="caption" color="error">
                    Wybierz liczbę osób
                  </Typography>
                )}
              </Box>
              <div className={styles.button}>
                <Box textAlign="center" sx={{ mt: 3 }}>
                  <Button
                    fullWidth
                    variant="contained"
                    size="large"
                    type="submit"
                    disabled={loading}
                  >
                    {loading ? <Loader /> : "ZŁÓŻ REZERWACJĘ"}
                  </Button>
                </Box>
              </div>
            </StyledForm>
          </StyledContainer>
        </div>
      </div>
    </LocalizationProvider>
  );
}
