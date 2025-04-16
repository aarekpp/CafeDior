import React from "react";
import styles from "./Menu.module.scss";
import menu from "../../icons/menu.png";

export default function Menu() {
  return (
    <div className={styles.menu}>
      <div className={styles.menuLeft}>
        <img className={styles.menuPhoto} src={menu} alt="Menu" />
      </div>
      <div className={styles.menuRight}>
        <img className={styles.menuPhoto} src={menu} alt="Menu" />
      </div>
      <div className={styles.menuLeft}>
        <img className={styles.menuPhoto} src={menu} alt="Menu" />
      </div>
      <div className={styles.menuRight}>
        <img className={styles.menuPhoto} src={menu} alt="Menu" />
      </div>
    </div>
  );
}
