import React from "react";
import styles from "./About.module.scss";
import { Typography } from "@mui/material";
import about from "../../icons/about.png";

export default function About() {
  return (
    <div className={styles.about}>
      <div className={styles.textBox}>
        <Typography>
          <p className={styles.text}>
            Nasze menu to prawdziwa uczta dla miłośników
            <br />
            świeżego pieczywa, wypiekanego na miejscu.
            <br />
            Proponujemy śniadania inspirowane kuchnią francuską,
            <br />
            zarówno w wersji słodkiej, jak i wytrawnej.
            <br />
            Oferujemy świeże pieczywo, w tym maślane bułki, bagietki
            <br />
            oraz pyszne ciasta, takie jak muffiny czy tarty.
            <br />
            Do każdego wypieku serwujemy aromatyczną kawę,
            <br />
            która doskonale dopełnia smak naszych specjałów.
          </p>
        </Typography>
      </div>
      <div className={styles.photo}>
        <img className={styles.image} src={about} alt="About" />
      </div>
    </div>
  );
}
