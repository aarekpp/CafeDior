import React from "react";
import "./Loader.module.scss";
import { CircularProgress } from "@mui/material";

export default function Loader() {
  return (
    <div className="loaderContainer">
      <CircularProgress />
    </div>
  );
}
