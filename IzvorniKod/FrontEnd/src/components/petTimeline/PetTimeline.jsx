import { useContext, useEffect, useState } from "react";
import Post from "../post/Post";
import Share from "../share/Share";
import "./petTimeline.css";
import axios from "axios";
import { AuthContext } from "../../context/AuthContext";
import {useParams} from "react-router-dom";

export default function PetTimeline({ pet }) {
  const [posts, setPosts] = useState([]);
  const { user } = useContext(AuthContext);

  useEffect(() => {
    const fetchPosts = async () => {
      if (!pet.id) {
        return
      }
      const res =  await axios.get("/api/posts/pets/" + pet.id);
      setPosts(
        res.data.sort((p1, p2) => {
          return new Date(p2.createdAt) - new Date(p1.createdAt);
        })
      );
    };
    fetchPosts();
  }, [pet.petname]);

  return (
    <div className="feed">
      <div className="feedWrapper">
        {posts.map((p) => (
          <Post key={p.id} post={p} />
        ))}
      </div>
    </div>
  );
}
