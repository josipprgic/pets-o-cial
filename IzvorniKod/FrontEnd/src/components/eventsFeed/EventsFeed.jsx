import { useContext, useEffect, useState } from "react";
import Post from "../post/Post";
import Share from "../share/Share";
import "./eventsFeed.css";
import axios from "axios";
import { AuthContext } from "../../context/AuthContext";
import Event from "../event/Event";
import {Link} from "react-router-dom";

export default function EventsFeed() {
  const [events, setEvents] = useState([]);
  const { user } = useContext(AuthContext);

  useEffect(() => {
    const fetchEvents = async () => {
      const res = await axios.get("/api/events/");
      setEvents(
        res.data
      );
    };
    fetchEvents();
  }, []);

  return (
    <div className="feed">
      <div className="feedWrapper">
        <div>
          <Link
              to={"/addEvent"}
              style={{textDecoration: "none"}}
          >
            <button className="loginButton">
              Add Event
            </button>
          </Link>
        </div>
        {events.map((p) => (
          <Event key={p.id} post={p} />
        ))}
      </div>
    </div>
  );
}
