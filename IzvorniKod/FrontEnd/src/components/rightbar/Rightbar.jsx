import "./rightbar.css";
import Online from "../online/Online";
import {useContext, useEffect, useRef, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import {AuthContext} from "../../context/AuthContext";
import {Add, Remove} from "@material-ui/icons";

export default function Rightbar({user}) {
    const PF = process.env.REACT_APP_PUBLIC_FOLDER;
    const [friends, setFriends] = useState([]);
    const [companyInfo, setCompanyInfo] = useState([]);

    const {user: currentUser, dispatch} = useContext(AuthContext);
    const [followed, setFollowed] = useState(
        currentUser.followings?.includes(user?.username)
    );
    const [blocked, setBlocked] = useState(false);

    const [services, setServices] = useState([]);
    const description = useRef();
    const serviceType = useRef();

    useEffect(() => {
        const getFriends = async () => {
            try {
                await axios.get("/api/users/friends/" + (!user ? currentUser.username : user.username))
                    .then(value => {
                        setFriends(value.data)
                        setFollowed(friends.includes(currentUser.username))
                    });
                await axios.get("/api/users/requestsSent")
                    .then(value => {
                        if (value.data.includes(currentUser.username))
                        setFollowed(value.data.includes(currentUser.username))

                    })
                if (user?.userType === "COMPANY") {
                    await axios.get("/api/users/company/services")
                        .then(value => {
                                setServices(value.data)
                        })
                    await axios.get("/api/users/company/info")
                        .then(value => {
                            setCompanyInfo(value.data)
                        })
                }
                await axios.get("/api/users/isBlocked/" + (!user ? currentUser.username : user.username))
                    .then(value => {
                        setBlocked(value.data.isBlocked)
                    });
            } catch (err) {
                console.log(err);
            }
        };
        getFriends().then(r => {
        });
    }, [user]);

    const handleClick = async () => {
        try {
            if (followed) {
                await axios.post(`/api/users/unfriend`, {
                    to: user.username,
                });
                dispatch({type: "UNFRIEND", payload: user.username});
            } else {
                await axios.post(`/api/users/requestFriendship`, {
                    to: user.username,
                });
                dispatch({type: "SEND REQUEST", payload: user.username});
            }
            setFollowed(!followed);
            window.location.reload()
        } catch (err) {
        }
    };

    const handleBlock = async () => {
        try {
                blocked ? await axios.post(`/api/users/unblock`, {
                    to: user.username,
                }) : await axios.post(`/api/users/block`, {
                    to: user.username,
                });
                setBlocked(!blocked)
        } catch (err) {
        }
    };

    const HomeRightbar = () => {
        return (
            <>
                <h4 className="rightbarTitle">Online Friends</h4>
                <ul className="rightbarFriendList">
                    {friends.map((u) => (
                        <Online key={u.username} user={u}/>
                    ))}
                </ul>
            </>
        );
    };

    const handleAddService = async (e) => {
        e.preventDefault();

        try {
            await axios.post(`/api/users/company/addService`, {
                serviceType: serviceType.current.value,
                description: description.current.value,
            });
            window.location.reload()
        } catch (err) {
        }
    }

    const ProfileRightbar = () => {
        const getProfilePic = async (friend) => {
            const res = await axios.get("/api/users/profilePicture?username=" + friend)
            return res.data.picture
                ? "data:image/png;base64, " + res.data.picture
                : "./icons/noAvatar.png"
        };

        return (
            <>
                {user.username !== currentUser.username && user.userType === 'USER' && currentUser.userType === 'USER' && (
                    <button className="rightbarFollowButton" onClick={handleClick}>
                        {followed ? "Unfriend" : "SendRequest"}
                        {followed ? <Remove/> : <Add/>}
                    </button>
                )}
                {user.username === currentUser.username && user.userType === 'USER' && (
                    <Link
                        to={"/addPets"}
                        style={{textDecoration: "none"}}
                    >
                        <button className="loginButton">
                            Add new pet
                        </button>
                    </Link>
                )}
                {user.username !== currentUser.username && (
                    <div>
                        <button className="rightbarBlockButton" onClick={handleBlock}>
                            {blocked ? "Unblock" : "Block"}
                        </button>
                    <Link
                        to={"/messages/" + user.username}
                        style={{textDecoration: "none"}}
                    >
                        <button className="loginButton">
                            Message
                        </button>
                    </Link>
                    </div>
                )}
                {user.username === currentUser.username && (
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
                )}


                <h4 className="rightbarTitle">User information</h4>
                <div className="rightbarInfo">
                    <div className="rightbarInfoItem">
                        <span className="rightbarInfoKey">Email:</span>
                        <span className="rightbarInfoValue">{user.email}</span>
                    </div>
                    <div className="rightbarInfoItem">
                        <span className="rightbarInfoKey">First Name:</span>
                        <span className="rightbarInfoValue">{user.firstname}</span>
                    </div>
                    <div className="rightbarInfoItem">
                        <span className="rightbarInfoKey">Last Name:</span>
                        <span className="rightbarInfoValue">{user.lastname}</span>
                    </div>
                    <div className="rightbarInfoItem">
                        <span className="rightbarInfoKey">Type:</span>
                        <span className="rightbarInfoValue">{user.userType}</span>
                    </div>
                </div>
                {user.userType === "COMPANY" && (
                    <div className="rightbarInfo">
                        <div className="rightbarInfoItem">
                            <span className="rightbarInfoKey">Company name:</span>
                            <span className="rightbarInfoValue">{companyInfo.name}</span>
                        </div>
                        <div className="rightbarInfoItem">
                            <span className="rightbarInfoKey">Company Address:</span>
                            <span className="rightbarInfoValue">{companyInfo.address}</span>
                        </div>
                        <div className="rightbarInfoItem">
                            <span className="rightbarInfoKey">Company contact:</span>
                            <span className="rightbarInfoValue">{companyInfo.contact}</span>
                        </div>
                    </div>
                )}
                {user.userType === 'USER' && (
                    <div>
                        <h4 className="rightbarTitle">User friends</h4>
                        <div className="rightbarFollowings">
                            {friends.map((friend) => (
                                <Link
                                    to={"/profile/" + friend}
                                    style={{textDecoration: "none"}}
                                >
                                    <div className="rightbarFollowing">
                                        <span className="rightbarFollowingName">{friend}</span>
                                    </div>
                                </Link>
                            ))}
                        </div>
                    </div>
                )}

                {currentUser.userType === 'COMPANY' && currentUser.username === user.username && (
                    <div>
                        <h4 className="rightbarTitle">Company services</h4>
                        <div className="rightbarFollowings">
                            {services?.map((service) => (
                                <div>
                                <div className="rightbarInfoItem">
                                    <span className="rightbarInfoKey">Service type:</span>
                                    <span className="rightbarInfoValue">{service.type}</span>
                                </div>
                                <div className="rightbarInfoItem">
                                <span className="rightbarInfoKey">Service description:</span>
                                <span className="rightbarInfoValue">{service.description}</span>
                                </div>
                                </div>
                            ))}
                            <form className="serviceBox" onSubmit={handleAddService}>
                                <input
                                    placeholder="Description"
                                    required
                                    ref={description}
                                    className="serviceInput"
                                />
                                <select placeholder="Service Type"
                                        required
                                        ref={serviceType}
                                        className="serviceInput">
                                    <option value="PET_SITTING">PET_SITTING</option>
                                    <option value="TRAINING">TRAINING</option>
                                    <option value="VETERINARY">VETERINARY</option>
                                </select>
                                <button className="serviceButton" type="submit">
                                    Add Service
                                </button>
                            </form>
                        </div>
                    </div>
                )}

            </>
        );
    };
    return (
        <div className="rightbar">
            <div className="rightbarWrapper">
                {user ? <ProfileRightbar/> : <HomeRightbar/>}
            </div>
        </div>
    );
}
