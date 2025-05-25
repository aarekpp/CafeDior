import React, { useEffect, useState } from "react";
import styles from "./About.module.scss";
import { Typography, Button } from "@mui/material";
import about from "../../icons/about.png";

export default function About() {
  const [role, setRole] = useState("MODERATOR");
  const [isEditable, setIsEditable] = useState(false);
  const [text, setText] = useState("");
  const [editedText, setEditedText] = useState("");

  useEffect(() => {
    setRole("MODERATOR");
    setText("");
  }, []);

  return (
    <div className={styles.about}>
      <div className={styles.textBox}>
        {isEditable ? (
          <div>
            <div>
              <textarea onChange={(e) => setEditedText(e.target.value)}>
                {editedText}
              </textarea>
            </div>
            <div>
              <Button onClick={() => setIsEditable(false)}>Anuluj</Button>
            </div>
            <div>
              <Button onClick={() => setIsEditable(false)}>Zapisz</Button>
            </div>
          </div>
        ) : (
          <Typography>
            {text}
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
            {role === "MODERATOR" && (
              <div>
                <Button onClick={() => setIsEditable(true)}>Edytuj</Button>
              </div>
            )}
          </Typography>
        )}
      </div>
      <div className={styles.photo}>
        <img className={styles.image} src={about} alt="About" />
      </div>
    </div>
  );
}
