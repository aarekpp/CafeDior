import ApiClient from "./ApiClient";

const AuthService = {
  signIn: async (data) => {
    try {
      const response = await ApiClient.post("/auth/signin", data);
      return response.data;
    } catch (error) {
      console.error("Sign in failed: ", error);
      return null;
    }
  },
  signUp: async (data) => {
    try {
      const response = await ApiClient.post("/auth/signup", data);
      return response.data;
    } catch (error) {
      console.error("Sign up failed: ", error);
      return null;
    }
  },
  verifyToken: async () => {
    try {
      const response = await ApiClient.post("/auth/verify-token", {});
      return response.data;
    } catch (error) {
      console.error("Token verification failed: ", error);
    }
  },
  logout: async () => {
    try {
      const response = await ApiClient.post("/auth/logout", {});
      return response;
    } catch (error) {
      console.error("Logout failed: ", error);
      return null;
    }
  },
};

export default AuthService;
