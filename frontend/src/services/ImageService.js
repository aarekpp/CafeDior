import ApiClient from "./ApiClient";

const ImageService = {
  addImage: async (formData) => {
    try {
      const response = await ApiClient.post("/images", formData);
      return response;
    } catch (error) {
      console.log(error);
      return null;
    }
  },
  deleteImage: async (id) => {
    try {
      const response = await ApiClient.delete(`/images/${id}`);
      return response;
    } catch (error) {
      console.log(error);
      return null;
    }
  },
};

export default ImageService;
