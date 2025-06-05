import { LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import Loader from "../../components/Loader/Loader";
import React, { useEffect, useState } from "react";
import {
  Card,
  CardContent,
  CardHeader,
  Typography,
  Box,
  Grid,
} from "@mui/material";
import styles from "./History.module.scss";
import ReservationService from "src/services/ReservationService";
import { format } from "date-fns";
import dayjs from "dayjs";
import customParseFormat from "dayjs/plugin/customParseFormat";

dayjs.extend(customParseFormat);
dayjs.locale("pl", {
  weekdays: [
    "Niedziela",
    "Poniedziałek",
    "Wtorek",
    "Środa",
    "Czwartek",
    "Piątek",
    "Sobota",
  ],
});

export default function History() {
  const [reservations, setReservations] = useState([]);
  const [isDataLoaded, setIsDataLoaded] = useState(false);

  useEffect(() => {
    const getAllReservations = async () => {
      const response = await ReservationService.fetchAll();
      console.log(response);
      if (response?.status === 200) {
        setReservations(response.data);
      }
      setIsDataLoaded(true);
    };

    getAllReservations();
  }, []);

  if (!isDataLoaded) return <Loader />;

  const mapReservationStatus = (status) => {
    let s;
    switch (status) {
      case "ACTIVE":
        s = "Aktywna";
        break;
      case "CANCELLED":
        s = "Anulowana";
        break;
      case "FINISHED":
        s = "Zakończona";
        break;
      default:
        s = "Brak danych";
    }
    return s;
  };

  return (
    <LocalizationProvider dateAdapter={AdapterDayjs}>
      <Box className={styles.formHeader}>
        <Typography
          variant="h4"
          component="h1"
          className={styles.formHeaderText}
        >
          Historia rezerwacji
        </Typography>
      </Box>
      <Box className={styles.content}>
        {reservations.length === 0 ? (
          <Typography variant="body1" className={styles.noReservations}>
            Brak historii rezerwacji
          </Typography>
        ) : (
          <Grid container spacing={3}>
            {reservations.map((reservation, index) => {
              const start = dayjs(reservation.reservationTime, "HH:mm");
              const end = start.add(1, "hour");

              return (
                <Grid item xs={12} sm={6} md={4} key={index}>
                  <Card className={styles.tile}>
                    <CardHeader
                      title={reservation.restaurant}
                      className={styles.tileHeader}
                      titleTypographyProps={{ variant: "h6" }}
                    />
                    <CardContent className={styles.tileContent}>
                      <Typography variant="body2" gutterBottom>
                        Data:{" "}
                        <span style={{ textTransform: "capitalize" }}>
                          {dayjs(reservation.reservationDate).format("dddd")},{" "}
                          {format(reservation.reservationDate, "dd/MM/yyyy")}
                        </span>
                      </Typography>
                      <Typography variant="body2" gutterBottom>
                        Godzina od-do: {start.format("HH:mm")} –{" "}
                        {end.format("HH:mm")}
                      </Typography>
                      <Typography variant="body2">
                        Liczba osób: {reservation.people}
                      </Typography>
                      <Typography variant="body2">
                        Status: {mapReservationStatus(reservation.status)}
                      </Typography>
                    </CardContent>
                  </Card>
                </Grid>
              );
            })}
          </Grid>
        )}
      </Box>
    </LocalizationProvider>
  );
}
