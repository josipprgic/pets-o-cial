import Home from "./pages/home/Home";
import Login from "./pages/login/Login";
import Profile from "./pages/profile/Profile";
import Register from "./pages/register/Register";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect,
} from "react-router-dom";
import { useContext } from "react";
import { AuthContext } from "./context/AuthContext";
import ProfileRequests from "./pages/profileRequests/ProfileRequests";
import AddPet from "./pages/addPet/AddPet";
import PetProfile from "./pages/petProfile/PetProfile";
import Posts from "./pages/posts/Posts";
import Message from "./pages/message/Message";
import axios from "axios";
import AddEvent from "./pages/addEvent/AddEvent";
import Events from "./pages/events/Events";

function App() {
  const { user } = useContext(AuthContext);
  return (
    <Router>
      <Switch>
        <Route exact path="/">
          {user ? <Home /> : <Register />}
        </Route>
        <Route path="/login">{user ? <Redirect to="/" /> : <Login />}</Route>
        <Route path="/register">
          {user ? <Redirect to="/" /> : <Register />}
        </Route>
        <Route path="/profile/:username">
          <Profile />
        </Route>
        <Route path="/profileRequests">
          <ProfileRequests />
        </Route>
        <Route path="/addPets">
          <AddPet />
        </Route>
        <Route path="/pet/:username/:petname">
          <PetProfile />
        </Route>
        <Route path="/posts/:id">
          <Posts />
        </Route>
        <Route path="/messages/:username">
          <Message />
        </Route>
        <Route path="/messages">
          <Message />
        </Route>
        <Route path="/addEvent">
          <AddEvent />
        </Route>
        <Route path="/events">
          <Events />
        </Route>
      </Switch>
    </Router>
  );
}

export default App;
