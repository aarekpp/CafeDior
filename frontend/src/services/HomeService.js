import ApiClient from "./ApiClient";

const HomeService = {
  fetchData: async () => {
    try {
      const response = ApiClient.get("/home");
      return response;
    } catch (error) {
      console.log(error);
      return null;
    }
  },
};

export default HomeService;
