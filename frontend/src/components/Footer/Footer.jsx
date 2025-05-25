import React from "react";
import styles from "./Footer.module.scss";

export default function Footer() {
  return (
    <div className={styles.footer}>
      <p className={styles.text}>Kacper Czernecki</p>
      <p className={styles.text}>Arkadiusz Przywara</p>
      <p className={styles.text}>Amelia Walaś</p>
    </div>
  );
}
