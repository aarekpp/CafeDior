import React from "react";
import styles from "./Home.module.scss";
import Header from "src/components/Header/Header";

export default function Home() {
  return (
    <div className={styles.container}>
      <Header />
    </div>
  );
}
