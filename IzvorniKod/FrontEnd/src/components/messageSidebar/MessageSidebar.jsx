import "./messageSidebar.css";
import {
  RssFeed,
  Chat,
  Event,
} from "@material-ui/icons";
import axios from "axios";
import Pets from "../pets/Pets";
import {useContext, useEffect, useState} from "react";
import {Link, useParams} from "react-router-dom";
import {AuthContext} from "../../context/AuthContext";

export default function MessageSidebar({username}) {
  const [messagedPeople, setMessagedPeople] = useState([]);
  const {user} = useContext(AuthContext);

  useEffect(() => {
    const fetchMessagedPeople = async () => {
      const res = await axios.get(`/api/users/messagedPeople`);
      setMessagedPeople(res.data);
    };
    fetchMessagedPeople();
  }, []);

  return (
    <div className="sidebar">
      <div className="sidebarWrapper">
        <ul className="sidebarList">
          <h4 className="rightbarTitle">Messaged Users</h4>
          {messagedPeople.map((person) => (
              <Link
                  to={"/messages/" + person.username}
                  style={{textDecoration: "none"}}
              >
                <img
                    src={"data:image/png;base64, " + person.profilePicture}
                    loading="lazy"
                    alt=""
                    className="rightbarFollowingImg"
                />
                <div className="sidebarListItem">
                  <span className="sidebarListItem">{person.username}</span>
                </div>
              </Link>
          ))}
        </ul>
      </div>
    </div>
  );
}
