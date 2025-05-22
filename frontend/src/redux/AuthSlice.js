import { createSlice } from "@reduxjs/toolkit";

export const authSlice = createSlice({
  name: "auth",
  initialState: {
    role: null,
    isLoggedIn: false,
    user: null,
  },
  reducers: {
    setLoginState: (state, action) => {
      state.role = action.payload.role;
      state.isLoggedIn = action.payload.isLoggedIn;
      state.user = action.payload.user;
    },
    setUser: (state, action) => {
      state.user = action.payload.user;
    },
    logout: (state) => {
      state.role = null;
      state.isLoggedIn = false;
      state.user = null;
    },
  },
});

export const { setLoginState, setUser, logout } = authSlice.actions;
export default authSlice.reducer;
