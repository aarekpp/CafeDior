import React, { useRef } from "react";
import styles from "./Menu.module.scss";
import { useSelector } from "react-redux";
import ImageService from "src/services/ImageService";

export default function Menu({ images = [] }) {
  const { role } = useSelector((state) => state.auth);

  const inputRefMenu1 = useRef(null);
  const inputRefMenu2 = useRef(null);

  const handleFileChange = async (e, section) => {
    const file = e.target.files[0];
    e.target.value = null;
    if (!file) return;
    if (!file.type.startsWith("image/")) {
      alert("Można przesyłać tylko pliki graficzne!");
      return;
    }

    const allowedExt = ["jpg", "jpeg", "png", "gif"];
    const ext = file.name.split(".").pop().toLowerCase();
    if (!allowedExt.includes(ext)) {
      alert(`Niedozwolone rozszerzenie. Dozwolone: ${allowedExt.join(", ")}`);
      return;
    }

    const maxSize = 5;
    if (file.size > maxSize * 1024 * 1024) {
      alert(`Maksymalny rozmiar to ${maxSize} MB.`);
      return;
    }

    const formData = new FormData();
    formData.append("files", file);
    formData.append("sections", section);

    try {
      const response = await ImageService.addImage(formData);
      if (response?.status === 200) {
        console.log("Upload success");
      } else {
        alert("Błąd podczas przesyłania");
      }
    } catch (err) {
      console.log(err);
    }
  };

  const handleDeleteButton = async (id) => {
    try {
      const response = await ImageService.deleteImage(id);
      if (response?.status === 204) {
        console.log("removed");
      }
    } catch (error) {
      console.log(error);
    }
  };

  if (images.length === 0 && role !== "MODERATOR") {
    return <p>Brak dodanych zdjęć MENU</p>;
  }

  const slots = [
    { section: "MENU1", className: styles.menuLeft, ref: inputRefMenu1 },
    { section: "MENU2", className: styles.menuRight, ref: inputRefMenu2 },
  ];

  return (
    <div className={styles.menu}>
      {slots.map(({ section, className, ref }, idx) => {
        const img = images[idx];
        return (
          <div key={section} className={className}>
            {img ? (
              <>
                <img
                  className={styles.menuPhoto}
                  src={import.meta.env.VITE_REACT_APP_API + img.url}
                  alt={section}
                />
                {role === "MODERATOR" && (
                  <div>
                    <button onClick={() => handleDeleteButton(img.id)}>
                      Usuń
                    </button>
                  </div>
                )}
              </>
            ) : role === "MODERATOR" ? (
              <>
                <button onClick={() => ref.current.click()}>
                  Dodaj {section}
                </button>
                <input
                  ref={ref}
                  type="file"
                  accept="image/*"
                  style={{ display: "none" }}
                  onChange={(e) => handleFileChange(e, section)}
                />
              </>
            ) : null}
          </div>
        );
      })}
    </div>
  );
}
