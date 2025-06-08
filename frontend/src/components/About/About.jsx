import React, { useEffect, useState } from "react";
import styles from "./About.module.scss";
import { Typography, Button } from "@mui/material";
import about from "../../icons/about.png";
import { useSelector } from "react-redux";
import AboutContentService from "src/services/AboutContentService";

export default function About() {
  const { role } = useSelector((state) => state.auth);
  const [isEditable, setIsEditable] = useState(false);
  const [text, setText] = useState("");
  const [editedText, setEditedText] = useState("");

  const validateInput = () => {
    const textRegexp = /^[\p{L}0-9\s.,?!;:'"()\-\u2013]*$/u;
    if (editedText.length > 0 && textRegexp.test(editedText)) {
      return true;
    } else {
      return false;
    }
  };

  const saveAboutContent = async () => {
    if (!validateInput()) {
      return;
    }

    const dataToSend = {
      text: editedText,
    };

    if (text === null || text.length === 0) {
      const response = await AboutContentService.addAboutContent(dataToSend);
      if (response?.status === 200) {
        setText(response.data);
        setEditedText(response.data);
        setIsEditable(false);
      }
    } else {
      const response = await AboutContentService.updateAboutContent(dataToSend);
      if (response?.status === 200) {
        setText(response.data);
        setEditedText(response.data);
        setIsEditable(false);
      }
    }
  };

  const handleCancelButton = () => {
    setIsEditable(false);
    setEditedText(text);
  };

  useEffect(() => {
    const fetchAboutContent = async () => {
      const response = await AboutContentService.fetchAboutContent();
      if (response?.status === 200) {
        setText(response.data);
        setEditedText(response.data);
      }
    };

    fetchAboutContent();
  }, []);

  return (
    <div className={styles.about}>
      <div className={styles.textBox}>
        {isEditable ? (
          <div>
            <div>
              <textarea
                value={editedText}
                onChange={(e) => setEditedText(e.target.value)}
              >
                {editedText}
              </textarea>
            </div>
            <div>
              <Button onClick={handleCancelButton}>Anuluj</Button>
            </div>
            <div>
              <Button onClick={saveAboutContent}>Zapisz</Button>
            </div>
          </div>
        ) : (
          <Typography>
            {text === null || text.length === 0 ? (
              <p className={styles.text}>Nie wprowadzono opisu sekcji</p>
            ) : (
              <p className={styles.text}>{text}</p>
            )}
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
