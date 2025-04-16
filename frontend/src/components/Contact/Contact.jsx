import React from "react";
import styles from "./Contact.module.scss";
import openingHours from "../../icons/openingHours.png";
import address from "../../icons/address.png";

export default function Contact() {
  return (
    <div className={styles.contact}>
      <div className={styles.openingHours}>
        <img
          className={styles.image}
          src={openingHours}
          alt="Godziny otwarcia"
        />
      </div>
      <div className={styles.address}>
        <img className={styles.image} src={address} alt="Adres" />
      </div>
    </div>
  );
}
