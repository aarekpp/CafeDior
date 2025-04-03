import React from "react";
import logo from "../../icons/logo.png";
import styles from "./Logo.module.scss";

const Logo = () => {
  return <img src={logo} alt="Logo" className={styles.logoImage} />;
};

export default Logo;
