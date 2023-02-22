import "./sidebar.css";
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

export default function Sidebar({username}) {
  const [pets, setPets] = useState([]);
  const {user} = useContext(AuthContext);

  useEffect(() => {
    const fetchPets = async () => {
      const res = await axios.get(`/api/users/pets/${username ? username : user.username}` );
      setPets(res.data);
    };
    fetchPets();
  }, []);

  return (
    <div className="sidebar">
      <div className="sidebarWrapper">
        <ul className="sidebarList">
          <li className="sidebarListItem">
            <RssFeed className="sidebarIcon" />
            <span className="sidebarListItemText">Feed</span>
          </li>
          <li className="sidebarListItem">
            <Chat className="sidebarIcon" />
            <span className="sidebarListItemText">Chats</span>
          </li>
          <li className="sidebarListItem">
            <Event className="sidebarIcon" />
            <span className="sidebarListItemText">Events</span>
          </li>
        </ul>
        <hr className="sidebarHr" />
        <h4 className="rightbarTitle">User pets</h4>
        <div className="sidebarPetList">
          {pets.map((pet) => (
              <Link
                  to={"/pet/" + (username ? username : user.username) + "/" + pet}
                  style={{textDecoration: "none"}}
              >
                <div className="sidebarListItem">
                  <span className="sidebarListItem">{pet}</span>
                </div>
              </Link>
          ))}
        </div>
      </div>
    </div>
  );
}
