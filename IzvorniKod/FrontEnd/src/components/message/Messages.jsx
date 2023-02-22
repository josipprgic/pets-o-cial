import "./messages.css";
import {
    RssFeed, Chat, Event,
} from "@material-ui/icons";
import axios from "axios";
import Pets from "../pets/Pets";
import {useContext, useEffect, useRef, useState} from "react";
import {Link, useParams} from "react-router-dom";
import {AuthContext} from "../../context/AuthContext";
import Share from "../share/Share";
import Post from "../post/Post";

export default function Messages({username}) {
    const [messages, setMessages] = useState([]);
    const {user} = useContext(AuthContext);
    const content = useRef();

    useEffect(() => {
        const fetchMessages = async () => {
            if (!username) {
            return
            }
            const res = await axios.get(`/api/users/messages?username=${username}`);
            setMessages(res.data);
        };
        fetchMessages();
    }, []);

    const handleClick = async (e) => {
        e.preventDefault();
            const data = {
                toUser: username,
                content: content.current.value,
            };
            try {
                await axios.post("/api/users/sendMessage", data);
                window.location.reload();
            } catch (err) {
                console.log(err);
                alert("Failed to send message: " + err)
            }
    };

    return (
        <div className="feed">
            {username && (
                <div className="feedWrapper">
                    <h4 className="rightbarTitle">Messages with {username}</h4>
                    <div>

                        {messages.map((message) => (
                            <div className={message.from.username === user.username ? "container" : "container-darker"}>

                                <img
                                    src={"data:image/png;base64, " + message.from.profilePicture}
                                    loading="lazy"
                                    alt=""
                                    className={message.from.username === user.username ? "right" : "left"}
                                />
                                <p>{message.content}</p>
                                <span className={message.from.username === user.username ? "time-right" : "time-left"}>{message.sentAt}</span>
                            </div>
                    ))}
                    </div>
                    <div className="loginRight">
                        <form className="messageBox" onSubmit={handleClick}>
                            <input
                                placeholder="Message"
                                required
                                ref={content}
                                className="loginInput"
                            />
                            <button className="loginButton" type="submit">
                                Send
                            </button>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}
