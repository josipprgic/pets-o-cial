import "./topbar.css";
import { Search, Person, Chat, Notifications } from "@material-ui/icons";
import {Link, useHistory} from "react-router-dom";
import {useContext, useRef} from "react";
import { AuthContext } from "../../context/AuthContext";
import axios from "axios";

export default function Topbar() {
  const { user } = useContext(AuthContext);
  const PF = process.env.REACT_APP_PUBLIC_FOLDER;
  const pattern = useRef();
  const history = useHistory()

  const handleSearch = async () => {
    try {
      const res = await axios.post(`/api/users/find`, {pattern: pattern.current.value});
      if (!res.data) {
        alert("No users found for pattern: " + pattern.current.value)
      }
      else {
        history.push("/profile/" + res.data);
      }
    } catch (err) {
      console.log(err);
      alert("Failed to search pattern: " + pattern.current.value + err)
    }
  }

  const signOut = () => {
    localStorage.setItem("user", null)
    history.push("/login")
  }

  axios.defaults.headers['Authorization'] = localStorage.getItem("app_token");

  return (
    <div className="topbarContainer">
      <div className="topbarLeft">
        <Link to="/" style={{ textDecoration: "none" }}>
          <span className="logo">NasiLjubimci</span>
        </Link>
      </div>
      <div className="topbarCenter">
        <div className="searchbar">
          <Search className="searchIcon"
          onClick={handleSearch}/>
          <input
            placeholder="Search for friend, post or video"
            className="searchInput"
            ref={pattern}
          />
        </div>
      </div>
      <div className="topbarRight">
        <div className="topbarLinks">
          <Link to={`/profile/${user.username}`}>
            <span className="topbarLink">Timeline</span>
          </Link>
          <Link to={`/events`}>
            <span className="topbarLink">Events</span>
          </Link>
        </div>
        <div className="topbarIcons">
          <Link to={`/profileRequests/${user.username}`}>
            <div className="topbarIconItem">
              <Person />
              <span className="topbarIconBadge">1</span>
            </div>
          </Link>
          <div className="topbarIconItem">
            <Link to={`/messages`}>
              <div className="topbarIconItem">
                <Chat />
                <span className="topbarIconBadge">2</span>
              </div>
            </Link>
          </div>
          <div className="topbarIconItem">
            <Notifications />
            <span className="topbarIconBadge">1</span>
          </div>
        </div>
        <Link to={`/profile/${user.username}`}>
          <img
            src={
              user.profilePicture
                ? "data:image/png;base64, " + user.profilePicture
                : "./icons/noAvatar.png"
            }
            alt=""
            className="topbarImg"
          />
        </Link>
          <button className="logoutButton" onClick={signOut}>
            Sign Out
          </button>
      </div>
    </div>
  );
}
