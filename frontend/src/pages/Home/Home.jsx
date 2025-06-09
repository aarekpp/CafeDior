import styles from "./Home.module.scss";
import Header from "src/components/Header/Header";
import { Typography } from "@mui/material";
import About from "../../components/About/About";
import Specialities from "../../components/Specialities/Specialities";
import Menu from "../../components/Menu/Menu";
import Contact from "../../components/Contact/Contact";
import Footer from "../../components/Footer/Footer";
import { useEffect, useState } from "react";
import HomeService from "src/services/HomeService";

export default function Home() {
  const [menuImages, setMenuImages] = useState([]);

  useEffect(() => {
    const fetchHomeData = async () => {
      const response = await HomeService.fetchData();
      if (response?.status === 200 && response.data) {
        const images = response.data.images;
        const menu = images.filter((img) => img.type.startsWith("MENU"));
        setMenuImages(menu);
      }
    };

    fetchHomeData();
  }, []);

  return (
    <div className={styles.container}>
      <Header />
      <div className={styles.main}>
        <Typography className={styles.text}>
          <h1 className={styles.title}>CafeDior</h1>
          <h2 className={styles.subtitle}>Bistro & Cafe</h2>
          <p className={styles.description}>
            Wejdź do świata, gdzie czas płynie wolniej, a każdy łyk kawy
            opowiada nową historię.
            <br />
            Zanurz się w atmosferze inspirowanej urokliwymi zakątkami Francji
            <br /> pełnej ciepła, elegancji i spokoju. To miejsce stworzyliśmy z
            myślą o Tobie.
          </p>
        </Typography>
      </div>
      <div className={styles.about}>
        <About></About>
      </div>
      <div className={styles.specialities}>
        <Specialities></Specialities>
      </div>
      <div className={styles.menu}>
        <Menu images={menuImages}></Menu>
      </div>
      <div className={styles.contact}>
        <Contact></Contact>
      </div>
      <div className={styles.footer}>
        <Footer></Footer>
      </div>
    </div>
  );
}
