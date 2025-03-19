import axios from "axios";

const ApiClient = axios.create({
  baseURL: import.meta.env.VITE_REACT_APP_API,
  withCredentials: true,
});

ApiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    return Promise.reject(error);
  }
);

export default ApiClient;
