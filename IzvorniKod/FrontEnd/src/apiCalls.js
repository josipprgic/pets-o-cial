import axios from "axios";

export const loginCall = async (userCredential, dispatch) => {
  dispatch({ type: "LOGIN_START" });
  try {
    const res = await axios.post("/api/login", userCredential);
    axios.defaults.headers['Authorization'] = "Bearer " + res.data.token;
    localStorage.setItem("app_token", "Bearer " + res.data.token)
    dispatch({ type: "LOGIN_SUCCESS", payload: res.data.user });
  } catch (err) {
    dispatch({ type: "LOGIN_FAILURE", payload: err });
  }
};

