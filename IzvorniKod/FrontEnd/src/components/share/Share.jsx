import "./share.css";
import {
    PermMedia,
    Label,
    Room,
    EmojiEmotions,
    Cancel,
} from "@material-ui/icons";
import {useContext, useEffect, useRef, useState} from "react";
import {AuthContext} from "../../context/AuthContext";
import axios from "axios";
import {Link} from "react-router-dom";

export default function Share() {
    const {user} = useContext(AuthContext);
    const PF = process.env.REACT_APP_PUBLIC_FOLDER;
    const desc = useRef();
    const pet = useRef();
    const [file, setFile] = useState(null);
    const [pets, setPets] = useState([]);

    useEffect(() => {
        const getPets = async () => {
            try {
                await axios.get("/api/users/pets/" + user.username)
                    .then(value => {
                        setPets(value.data)
                    });
            } catch (err) {
                console.log(err);
            }
        };
        getPets().then(r => {
        });
    }, [user]);

    const submitHandler = async (e) => {
        e.preventDefault();
        const newPost = {
            userId: user.id,
            desc: desc.current.value,
            pet: pet.current ? pet.current.value : null,
        };
        if (file) {
            const data = new FormData();
            const fileName = Date.now() + file.name;
            data.append("name", fileName);
            data.append("file", file);
            data.append("desc", newPost.desc);
            data.append("pet", newPost.pet);
            newPost.img = fileName;
            console.log(newPost);
            try {
                await axios.post("/api/posts/upload", data);
                window.location.reload();
            } catch (err) {
            }
        } else {
            alert("You're missing some mandatory fields!")
        }
    };

    return (
        <div className="share">
            <div className="shareWrapper">
                <div className="shareTop">
                    <img
                        className="shareProfileImg"
                        src={
                            user.profilePicture
                                ? "data:image/png;base64, " + user.profilePicture
                                : "./icons/noAvatar.png"
                        }
                        alt=""
                    />
                    <input
                        placeholder={"What's on your mind " + user.username + "?"}
                        className="shareInput"
                        ref={desc}
                    />
                </div>
                <hr className="shareHr"/>
                {file && (
                    <div className="shareImgContainer">
                        <img className="shareImg" src={URL.createObjectURL(file)} alt=""/>
                        <Cancel className="shareCancelImg" onClick={() => setFile(null)}/>
                    </div>
                )}
                <form className="shareBottom" onSubmit={submitHandler}>
                    <div className="shareOptions">
                        <label htmlFor="file" className="shareOption">
                            <PermMedia htmlColor="tomato" className="shareIcon"/>
                            <span className="shareOptionText">Photo or Video</span>
                            <input
                                style={{display: "none"}}
                                type="file"
                                id="file"
                                accept=".png,.jpeg,.jpg"
                                onChange={(e) => setFile(e.target.files[0])}
                            />
                        </label>
                        {user.userType === "USER" && (
                            <div className="shareOption">

                                <select placeholder="Pet"
                                        required
                                        ref={pet}
                                        className="shareOption">
                                    {pets.map((pet) => (
                                        <option value={pet}>{pet}</option>
                                    ))}
                                </select>
                                <span className="shareOptionText">Pet</span>
                            </div>
                        )}

                    </div>
                    <button className="shareButton" type="submit">
                        Share
                    </button>
                </form>
            </div>
        </div>
    );
}
