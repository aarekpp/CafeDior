import ApiClient from "./ApiClient";

const ReservationService = {
  fetchAll: async () => {
    try {
      const response = ApiClient.get("/reservations");
      return response;
    } catch (error) {
      console.log(error);
      return null;
    }
  },
  createReservation: async (data) => {
    try {
      const response = await ApiClient.post("/reservations", data);
      return response;
    } catch (error) {
      console.log(error);
      return null;
    }
  },
};

export default ReservationService;
