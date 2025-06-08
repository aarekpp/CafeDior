import ApiClient from "./ApiClient";

const AboutContentService = {
  fetchAboutContent: async () => {
    try {
      const response = ApiClient.get("/about");
      return response;
    } catch (error) {
      console.log(error);
      return null;
    }
  },
  addAboutContent: async (data) => {
    try {
      const response = ApiClient.post("/about", data);
      return response;
    } catch (error) {
      console.log(error);
      return null;
    }
  },
  updateAboutContent: async (data) => {
    try {
      const response = ApiClient.put("/about", data);
      return response;
    } catch (error) {
      console.log(error);
      return null;
    }
  },
};

export default AboutContentService;
