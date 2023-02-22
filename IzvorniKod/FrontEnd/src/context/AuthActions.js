export const LoginStart = (userCredentials) => ({
  type: "LOGIN_START",
});

export const LoginSuccess = (user) => ({
  type: "LOGIN_SUCCESS",
  payload: user,
});

export const LoginFailure = () => ({
  type: "LOGIN_FAILURE",
});

export const SendRequest = (userId) => ({
  type: "SEND REQUEST",
  payload: userId,
});

export const Unfriend = (userId) => ({
  type: "UNFRIEND",
  payload: userId,
});
