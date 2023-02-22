import "./profile.css";
import Topbar from "../../components/topbar/Topbar";
import Sidebar from "../../components/sidebar/Sidebar";
import Feed from "../../components/feed/Feed";
import Rightbar from "../../components/rightbar/Rightbar";
import {useContext, useEffect, useRef, useState} from "react";
import axios from "axios";
import { useParams } from "react-router";
import {PermMedia} from "@material-ui/icons";
import {AuthContext} from "../../context/AuthContext";
import ProfileRightbar from "../../components/profileRightbar/ProfileRightbar";
import {useHistory} from "react-router-dom";

export default function Profile() {
  const PF = process.env.REACT_APP_PUBLIC_FOLDER;
  const [user, setUser] = useState({});
  const username = useParams().username;
  const {user: currentUser, dispatch} = useContext(AuthContext);
  const history = useHistory()

  const [file, setFile] = useState(null);

  const submitHandlerProfile = async (e) => {
    e.preventDefault();
    const newPost = {
      userId: user.id,
    };
    if (file) {
      const data = new FormData();
      const fileName = Date.now() + file.name;
      data.append("name", fileName);
      data.append("file", file);
      newPost.img = fileName;
      console.log(newPost);
      try {
        await axios.post("/api/users/uploadProfile", data);
        user.profilePicture = file
        window.location.reload();
      } catch (err) {}
    }
  };

  useEffect(() => {
    const fetchUser = async () => {
      const res = await axios.get(`/api/users?username=${username}`);
      setUser(res.data);
      if (!res.data) {
        alert("No profile")
        history.push("/")
      }
    };
    fetchUser();
  }, [username]);

  return (
    <>
      <Topbar />
      <div className="profile">
        <Sidebar username={username}/>
        <div className="profileRight">
          <div className="profileRightTop">
            <div className="profileCover">
              <img
                className="profileCoverImg"
                src={
                  user.coverPicture
                    ? PF + user.coverPicture
                    : "./icons/noCover.png"
                }
                alt=""
              />
              <img
                className="profileUserImg"
                src={
                  user.profilePicture
                    ? "data:image/png;base64, " + user.profilePicture
                    : "./icons/noAvatar.png"
                }
                alt=""
              />
              {user.username === currentUser.username && (
                  <form className="shareBottom" onSubmit={submitHandlerProfile}>
                <div className="shareOptions">
                  <label htmlFor="file" className="shareOption">
                    <PermMedia htmlColor="tomato" className="shareIcon" />
                    <span className="shareOptionText">Profile Picture</span>
                    <input
                        style={{ display: "none" }}
                        type="file"
                        id="file"
                        accept=".png,.jpeg,.jpg"
                        onChange={(e) => setFile(e.target.files[0])}
                    />
                  </label>
                </div>
                <button className="shareButton" type="submit">
                  Update
                </button>
              </form>)}

            </div>
            <div className="profileInfo">
              <h4 className="profileInfoName">{user.username}</h4>
              <span className="profileInfoDesc">{user.desc}</span>
            </div>
          </div>
          <div className="profileRightBottom">
            <Feed username={username} />
            <ProfileRightbar user={user} />
          </div>
        </div>
      </div>
    </>
  );
}
