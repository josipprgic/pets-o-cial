import "./petProfile.css";
import Topbar from "../../components/topbar/Topbar";
import Sidebar from "../../components/sidebar/Sidebar";

import Feed from "../../components/feed/Feed";
import Rightbar from "../../components/rightbar/Rightbar";
import {useContext, useEffect, useState} from "react";
import axios from "axios";
import {useParams} from "react-router";
import {PermMedia, Timeline} from "@material-ui/icons";
import {AuthContext} from "../../context/AuthContext";
import PetTimeline from "../../components/petTimeline/PetTimeline";

export default function PetProfile() {
    const PF = process.env.REACT_APP_PUBLIC_FOLDER;
    const [pet, setPet] = useState({});
    const petname = useParams().petname;
    const {user} = useContext(AuthContext);

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
            data.append("petname", petname)
            newPost.img = fileName;
            console.log(newPost);
            try {
                await axios.post("/api/pets/uploadProfile", data);
                user.profilePicture = file
                window.location.reload();
            } catch (err) {
            }
        }
    };

    useEffect(() => {
        const fetchPet = async () => {
            const res = await axios.get(`/api/pets?petName=${petname}`);
            setPet(res.data);
        };
        fetchPet();
    }, [petname]);

    return (
        <>
            <Topbar/>
            <div className="profile">
                <Sidebar/>
                <div className="profileRight">
                    <div className="profileRightTop">
                        <img
                            className="profileUserImg"
                            src={
                                pet.profilePicture
                                    ? "data:image/png;base64, " + pet.profilePicture
                                    : "./icons/noAvatar.png"
                            }
                            alt=""
                        />
                        <form className="shareBottom" onSubmit={submitHandlerProfile}>
                            <div className="shareOptions">
                                <label htmlFor="file" className="shareOption">
                                    <PermMedia htmlColor="tomato" className="shareIcon"/>
                                    <span className="shareOptionText">Profile Picture</span>
                                    <input
                                        style={{display: "none"}}
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
                        </form>
                    </div>
                    <div className="profileInfo">
                        <div className="rightbarInfoItem">
                            <span className="rightbarInfoKey">Pet Name:</span>
                            <span className="rightbarInfoValue">{pet.petname}</span>
                        </div>
                        <div className="rightbarInfoItem">
                            <span className="rightbarInfoKey">Pet Owner:</span>
                            <span className="rightbarInfoValue">{pet.owner}</span>
                        </div>
                        <div className="rightbarInfoItem">
                            <span className="rightbarInfoKey">Pet Type:</span>
                            <span className="rightbarInfoValue">{pet.petType}</span>
                        </div>
                        <div className="rightbarInfoItem">
                            <span className="rightbarInfoKey">Pet gender:</span>
                            <span className="rightbarInfoValue">{pet.gender}</span>
                        </div>
                        <div className="rightbarInfoItem">
                            <span className="rightbarInfoKey">Pet Breed:</span>
                            <span className="rightbarInfoValue">{pet.breed}</span>
                        </div>
                        <div className="rightbarInfoItem">
                            <span className="rightbarInfoKey">Pet Description:</span>
                            <span className="rightbarInfoValue">{pet.description}</span>
                        </div>
                    </div>
                </div>
            </div>
            <div className="profileRightBottom">
                <Timeline pet={pet}/>
                <PetTimeline pet={pet}/>
            </div>
        </>
    );
}
