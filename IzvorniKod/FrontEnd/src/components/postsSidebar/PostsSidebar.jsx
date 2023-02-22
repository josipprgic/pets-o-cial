import "./postsSidebar.css";
import Online from "../online/Online";
import {useContext, useEffect, useRef, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import {AuthContext} from "../../context/AuthContext";
import {Add, Remove} from "@material-ui/icons";

export default function PostsSidebar({post}) {
    const [comments, setComments] = useState([]);
    const {user: currentUser} = useContext(AuthContext);
    const content = useRef()

    useEffect(() => {
        const getComments = async () => {
            try {
                await axios.get("/api/posts/comments/" + post.id)
                    .then(value => {
                        setComments(value.data)
                    });
            } catch (err) {
                console.log(err);
            }
        };
        getComments().then(r => {
        });
    }, [post.id]);

    const handleClick = async (e) => {
        e.preventDefault();

        try {
                const res = await axios.post(`/api/posts/uploadComment`, {
                    postId: post.id,
                    content: content.current.value
                });
            window.location.reload()
        } catch (err) {
        }
    };
        return (
            <>
                <h4 className="rightbarTitle">Comment Section</h4>

                <div className="rightbarInfo">
                    <form className="rightbarInfoItem" onSubmit={handleClick}>
                        <input
                            placeholder="Comment"
                            required
                            ref={content}
                            className="loginInput"
                        />
                        <button className="loginButton" type="submit">
                            Comment
                        </button>
                    </form>
                </div>
                <div className="rightbarInfo">
                {comments.map((comment) => (
                    <div className="rightbarInfo">
                        <div className="rightbarInfoItem">
                            <span className="rightbarInfoValue">{comment.username}</span>
                        </div>
                        <div className="rightbarInfoItem">
                            <span className="rightbarInfoValue">{comment.content}</span>
                        </div>
                    </div>
                ))}
                </div>
            </>
        );
}
