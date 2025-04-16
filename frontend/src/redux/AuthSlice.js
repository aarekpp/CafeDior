import { createSlice } from "@reduxjs/toolkit";

export const authSlice = createSlice({
  name: "auth",
  initialState: {
    role: null,
    isLoggedIn: false,
    isAccountActive: false,
    user: null,
  },
  reducers: {
    setLoginState: (state, action) => {
      state.role = action.payload.role;
      state.isLoggedIn = action.payload.isLoggedIn;
      state.isAccountActive = action.payload.isAccountActive;
      state.user = action.payload.user;
    },
    setAccountActive: (state, action) => {
      state.isAccountActive = action.payload.isAccountActive;
    },
    setUser: (state, action) => {
      state.user = action.payload.user;
    },
    logout: (state) => {
      state.role = null;
      state.isLoggedIn = false;
      state.isAccountActive = false;
      state.user = null;
    },
  },
});

export const { setLoginState, setAccountActive, setUser, logout } =
  authSlice.actions;
export default authSlice.reducer;
