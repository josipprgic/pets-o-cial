import "./post.css";
import { MoreVert } from "@material-ui/icons";
import { useContext, useEffect, useState } from "react";
import axios from "axios";
import { format } from "timeago.js";
import {Link, useParams} from "react-router-dom";
import { AuthContext } from "../../context/AuthContext";
import {dark} from "@material-ui/core/styles/createPalette";
import Topbar from "../../components/topbar/Topbar";
import PostsSidebar from "../../components/postsSidebar/PostsSidebar";

export default function Posts( ) {
    const [like, setLike] = useState(0);
    const [isLiked, setIsLiked] = useState(false);
    const [user, setUser] = useState({});
    const [post, setPost] = useState({});

    const postId = useParams().id
    const PF = process.env.REACT_APP_PUBLIC_FOLDER;
    const { user: currentUser } = useContext(AuthContext);

    useEffect(() => {
      setIsLiked(post.likes?.includes(currentUser.username));
    }, [currentUser.id, post.likes]);

    useEffect(() => {
        const fetchPost = async () => {
            const res = await axios.get(`/api/posts?id=${postId}`);
            setPost(res.data);
            setLike(res.data.likes?.length)
        };
        fetchPost();
    }, [postId]);

    useEffect(() => {
        const fetchUser = async () => {
            if (!post.publisher) {
                return
            }
            const res = await axios.get(`/api/users?username=${post.publisher}`);
            setUser(res.data);
        };
        fetchUser();
    }, [post.publisher]);

    const likeHandler = () => {
        try {
            const data = {userId: currentUser.id,
            postId: post.id}
            isLiked ? axios.post("/api/posts/unlike", data) : axios.post("/api/posts/like", data);
        } catch (err) {}
        setLike(isLiked ? like - 1 : like + 1);
        setIsLiked(!isLiked);
    };
    return (
        <>
        <Topbar/>
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
                        <span className="postUsername">{post.petName}</span>
                        <span className="postDate">{format(post.createdAt)}</span>
                    </div>createdAt
                    <div className="postTopRight">
                        <Link to={`/posts/${post.id}`}>
                            <MoreVert />
                        </Link>
                    </div>
                </div>
                <div className="postCenter">
                    <span className="postText">{post?.desc}</span>
                    <img className="postImg" src={"data:image/png;base64, " + post.content} alt="" />
                </div>
                <div className="postBottom">
                    <div className="postBottomLeft">
                        <img
                            className="likeIcon"
                            src={`./icons/like.png`}
                            onClick={likeHandler}
                            alt=""
                        />
                        <span className="postLikeCounter">{like} people like it</span>
                    </div>
                    <div className="postBottomRight">
                        <span className="postCommentText">{post.comment} comments</span>
                    </div>
                </div>
            </div>
            <div>
                <PostsSidebar post={post}/>
            </div>
        </div>
            </>
    );
}
