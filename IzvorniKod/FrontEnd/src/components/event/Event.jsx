import "./event.css";
import { MoreVert } from "@material-ui/icons";
import { useContext, useEffect, useState } from "react";
import axios from "axios";
import { format } from "timeago.js";
import { Link } from "react-router-dom";
import { AuthContext } from "../../context/AuthContext";
import Online from "../online/Online";

export default function Event({ post }) {
  const [coming, setComing] = useState(post.coming?.length);
  const [user, setUser] = useState({});
  const PF = process.env.REACT_APP_PUBLIC_FOLDER;
  const { user: currentUser } = useContext(AuthContext);

  useEffect(() => {
    const fetchUser = async () => {
      const res = await axios.get(`/api/users?username=${post.organizer}`);
      setUser(res.data);
      setComing(post.coming?.length)
    };
    fetchUser();
  }, [post.organizer, post.coming]);

  const handleApprove = async () => {
    try {
      await axios.post("/api/events/coming", { postId: post.id})
      window.location.reload();
      setComing(coming + 1)
    } catch (e) {
      alert("Failed to approve Friend request")
    }
  }

  const handleDecline = async () => {
    try {
      await axios.post("/api/events/notComing", { postId: post.id})
      window.location.reload();
      setComing(coming - 1)
    } catch (e) {
      alert("Failed to decline Friend request")
    }
  }

  const handleMaybe = async () => {
    try {
      await axios.post("/api/events/maybe", { postId: post.id})
      window.location.reload();
    } catch (e) {
      alert("Failed to block user")
    }
  }

  return (
    <div className="post">
      <div className="postWrapper">
        <div className="postTop">
          <div className="postTopLeft">
            <Link to={`/profile/${user.username}`}>
              <img
                className="postProfileImg"
                src={
                  user.profilePicture
                    ? "data:image/png;base64, " + user.profilePicture
                    : "./icons/noAvatar.png"
                }
                alt=""
              />
            </Link>
            <span className="postUsername">{user.username}</span>
            <span className="postDate">{post.createdAt}</span>
          </div>
          <div className="postTopRight">
            <Link to={`/posts/${post.id}`}>
            <MoreVert />
            </Link>
          </div>
        </div>
        <div className="postCenter">
          <p className="postText">Organizer: {post.organizer}</p>
          <p className="postText">Description: {post.description}</p>
          <p className="postText">Location: {post.location}</p>
          <p className="postText">Starts At: {post.startsAt}</p>
          <p className="postText">Duration: {post.duration}</p>
          <p>Coming</p>
          {post.coming.map((friend) => (
              <Link
                  to={"/profile/" + friend}
                  style={{textDecoration: "none"}}
              >
                <div className="rightbarFollowing">
                  <span className="rightbarFollowingName">{friend}</span>
                </div>
              </Link>
          ))}
        </div>
        <div className="postBottom">
          <div className="postBottomLeft">
            <div>
              <img
                  className="likeIcon"
                  src={`./icons/accept.png`}
                  onClick={handleApprove}
                  alt=""
              />
              <span className="postLikeCounter">{coming} people are coming</span>
            </div>
            <div>
              <img
                  className="likeIcon"
                  src={`./icons/deny.png`}
                  onClick={handleDecline}
                  alt=""
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
