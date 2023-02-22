import axios from "axios";
import {useContext, useEffect, useState} from "react";
import "./profileRequests.css";
import {AuthContext} from "../../context/AuthContext";
import {Link} from "react-router-dom";
import Topbar from "../../components/topbar/Topbar";
import FriendRequest from "../../components/friendRequest/FriendRequest";

export default function ProfileRequests() {
    const PF = process.env.REACT_APP_PUBLIC_FOLDER;
    const [requests, setRequests] = useState([]);
    const {user: currentUser} = useContext(AuthContext);

    useEffect(() => {
        const getRequests = async () => {
            try {
                const requestList = await axios.get("/api/users/requests");
                setRequests(requestList.data);
            } catch (err) {
                console.log(err);
            }
        };
        getRequests();
    }, [currentUser]);

    return (
        <>
            <Topbar />
            <h4 className="rightbarTitle">Friend Requests</h4>
            <div className="rightbarFollowings">
                {requests.map((request) => (
                    <Link
                        to={"/profile/" + request.username}
                        style={{textDecoration: "none"}}
                    >
                        <div className="rightbarFollowing">
                            <img
                                src={
                                    request.profilePicture
                                        ? "data:image/png;base64, " + request.profilePicture
                                        : "./icons/noAvatar.png"
                                }
                                alt=""
                                className="rightbarFollowingImg"
                            />
                            <span className="rightbarFollowingName">{request.username}</span>
                        </div>
                        <h4 className="rightbarTitle">User information</h4>
                        <div className="rightbarInfo">
                            <div className="rightbarInfoItem">
                                <span className="rightbarInfoKey">Email:</span>
                                <span className="rightbarInfoValue">{request.email}</span>
                            </div>
                            <div className="rightbarInfoItem">
                                <span className="rightbarInfoKey">First Name:</span>
                                <span className="rightbarInfoValue">{request.firstname}</span>
                            </div>
                            <div className="rightbarInfoItem">
                                <span className="rightbarInfoKey">Last Name:</span>
                                <span className="rightbarInfoValue">{request.lastname}</span>
                            </div>
                        </div>
                        <FriendRequest key={request.id} user={request}/>
                    </Link>
                ))}
            </div>
        </>
    );
}
