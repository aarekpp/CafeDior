import React from "react";
import styles from "./Specialities.module.scss";
import { Typography } from "@mui/material";
import croissants from "../../icons/croissants.jpg";
import bread from "../../icons/bread.jpg";
import breakfast from "../../icons/breakfast.jpg";
import coffee from "../../icons/coffee.jpg";
import sweets from "../../icons/sweets.jpg";

export default function Specialities() {
  return (
    <div className={styles.container}>
      <div className={styles.section}>
        <div className={styles.imageContainer}>
          <img src={croissants} alt="Croissants" className={styles.image} />
        </div>
        <div className={styles.content}>
          <Typography variant="h2" className={styles.title}>
            CROISSANTS
          </Typography>
          <Typography variant="h3" className={styles.subtitle}>
            francuskie dzieła maślane
          </Typography>
          <Typography className={styles.description}>
            Delikatnie warstwowe, złociste półksiężyce o chrupiącej skórce i
            miękkim wnętrzu. Idealne solo, z konfiturą, lub jako baza dla
            wytwornych kompozycji smakowych. Codziennie rano wypiekane z masła
            AOP Charentes-Poitou.
          </Typography>
        </div>
      </div>

      <div className={`${styles.section} ${styles.reverse}`}>
        <div className={styles.imageContainer}>
          <img src={bread} alt="Bread" className={styles.image} />
        </div>
        <div className={styles.content}>
          <Typography variant="h2" className={styles.title}>
            PIECZYWO
          </Typography>
          <Typography variant="h3" className={styles.subtitle}>
            wypieki z tradycją w tle
          </Typography>
          <Typography className={styles.description}>
            Rytuał porannego pieczenia wg sekretnej receptury. W naszej piekarni
            znajdziesz chleby z 48-godzinnym zakwasem, chrupiące bagietki
            parzone oraz puszyste brioszki. Wszystkie wypieki tworzymy tylko z
            czterech podstawowych składników.
          </Typography>
        </div>
      </div>

      <div className={styles.section}>
        <div className={styles.imageContainer}>
          <img src={breakfast} alt="Breakfast" className={styles.image} />
        </div>
        <div className={styles.content}>
          <Typography variant="h2" className={styles.title}>
            ŚNIADANIA & LUNCHE
          </Typography>
          <Typography variant="h3" className={styles.subtitle}>
            dla każdego apetytu
          </Typography>
          <Typography className={styles.description}>
            Od lekkich śniadaniowych kompozycji po sycące lunche. Proponujemy
            tartinki na świeżym żytnim chlebie, kremowe zupy dnia oraz
            rustykalne tarty warzywne. Każde danie to kulinarny hołd dla
            prostoty i sezonowości.
          </Typography>
        </div>
      </div>

      <div className={`${styles.section} ${styles.reverse}`}>
        <div className={styles.imageContainer}>
          <img src={coffee} alt="Coffee" className={styles.image} />
        </div>
        <div className={styles.content}>
          <Typography variant="h2" className={styles.title}>
            KAWA & HERBATA
          </Typography>
          <Typography variant="h3" className={styles.subtitle}>
            rytuał dnia codziennego
          </Typography>
          <Typography className={styles.description}>
            Nasze espresso parzymy ze starannie palonych mieszanek, a herbaty
            zaparzamy w szklanych dzbankach. W menu znajdziesz wyjątkowe dodatki
            jak domowy syrop z płatków kalifornijskiej róży czy miodu
            lawendowego z Prowansji.
          </Typography>
        </div>
      </div>

      <div className={styles.section}>
        <div className={styles.imageContainer}>
          <img src={sweets} alt="Sweets" className={styles.image} />
        </div>
        <div className={styles.content}>
          <Typography variant="h2" className={styles.title}>
            SŁODKOŚCI
          </Typography>
          <Typography variant="h3" className={styles.subtitle}>
            francuska słodka elegancja
          </Typography>
          <Typography className={styles.description}>
            Cienkie jak welon ciasto francuskie, kremowe fillingi i owoce w
            karmelu. W naszej cukierni odkryjesz legendarne desery jak canelé
            bordoskie, mini madeleines oraz czekoladowe religieuses. Wszystko w
            minimalistycznym, paryskim stylu.
          </Typography>
        </div>
      </div>
    </div>
  );
}
