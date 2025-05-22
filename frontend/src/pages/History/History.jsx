import { LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";
import Loader from "../../components/Loader/Loader";
import dayjs from "dayjs";
import React, { useEffect, useState } from "react";
import { Card, CardContent, CardHeader, Typography, Box } from "@mui/material";
import styles from "./History.module.scss";

export default function History() {
  const [reservations, setReservations] = useState([]);
  const [isDataLoaded, setIsDataLoaded] = useState(false);

  useEffect(() => {
    setReservations([]);
    setIsDataLoaded(true);
  }, []);

  if (!isDataLoaded) return <Loader />;

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
            {reservations.map((reservation, index) => (
              <Grid item xs={12} sm={6} md={4} key={index}>
                <Card className={styles.tile}>
                  <CardHeader
                    title={reservation.restaurant}
                    className={styles.tileHeader}
                    titleTypographyProps={{ variant: "h6" }}
                  />
                  <CardContent className={styles.tileContent}>
                    <Typography variant="body2" gutterBottom>
                      Adres: {reservation.address}
                    </Typography>
                    <Typography variant="body2" gutterBottom>
                      Data:{" "}
                      <span style={{ textTransform: "capitalize" }}>
                        {dayjs(reservation.date).format("dddd, DD-MM-YYYY")}
                      </span>
                    </Typography>
                    <Typography variant="body2" gutterBottom>
                      Od/do: {reservation.startTime} / {reservation.endTime}
                    </Typography>
                    <Typography variant="body2">
                      Liczba osób: {reservation.numberOfPeople}
                    </Typography>
                  </CardContent>
                </Card>
              </Grid>
            ))}
          </Grid>
        )}
      </Box>
    </LocalizationProvider>
  );
}
