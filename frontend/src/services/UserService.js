import ApiClient from "./ApiClient";

const UserService = {
  getUserData: async (userId) => {
    try {
      const response = await ApiClient.get(`/user/${userId}`);
      return response;
    } catch (error) {
      console.error("Error during fetch user data: ", error);
    }
  },
  updateUserData: async (userId, data) => {
    try {
      const response = await ApiClient.put(`/user/${userId}`, data);
      return response;
    } catch (error) {
      console.error("Error during update user data: ", error);
      return null;
    }
  },
};

export default UserService;
