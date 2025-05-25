import React, { useEffect, useState } from "react";
import AppRoutes from "./routes/AppRoutes";
import { useDispatch } from "react-redux";
import Loader from "./components/Loader/Loader";
import AuthService from "./services/AuthService";
import { setLoginState } from "./redux/AuthSlice";

export default function App() {
  const dispatch = useDispatch();
  const [isAppInitialized, setIsAppInitialized] = useState(false);

  useEffect(() => {
    const validateToken = async () => {
      try {
        const response = await AuthService.verifyToken();
        const isEmpty = !response || Object.keys(response).length === 0;
        if (!isEmpty) {
          dispatch(
            setLoginState({
              role: response.role,
              isLoggedIn: true,
              user: response.userId,
            })
          );
        }
      } catch (error) {
        console.log(error);
      } finally {
        setIsAppInitialized(true);
      }
    };

    validateToken();
  }, [dispatch]);

  if (!isAppInitialized) return <Loader />;

  return <AppRoutes />;
}
