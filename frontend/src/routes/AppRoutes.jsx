import React from "react";
import { BrowserRouter, Route, Routes } from "react-router";
import SignIn from "../pages/SignIn/SignIn";
import SignUp from "../pages/SignUp/SignUp";
import Home from "../pages/Home/Home";
import Profile from "src/pages/Profile/Profile";
import History from "src/pages/History/History";
import Reservation from "src/components/Reservation/Reservation";
import RequireGuest from "./RequireGuest";
import RequireAuth from "./RequireAuth";

export default function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route element={<RequireGuest />}>
          <Route path="/signin" element={<SignIn />} />
          <Route path="/signup" element={<SignUp />} />
        </Route>

        <Route element={<RequireAuth />}>
          <Route path="/profile" element={<Profile />} />
          <Route path="/history" element={<History />} />
          <Route path="/reservation" element={<Reservation />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
