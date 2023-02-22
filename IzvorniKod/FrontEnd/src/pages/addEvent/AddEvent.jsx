import axios from "axios";
import {useContext, useRef} from "react";
import "./addEvent.css";
import {useHistory} from "react-router";
import {AuthContext} from "../../context/AuthContext";
import Topbar from "../../components/topbar/Topbar";

export default function AddEvent() {
    const startDate = useRef();
    const startTime = useRef();
    const duration = useRef();
    const description = useRef();
    const location = useRef();
    const history = useHistory();

    const handleClick = async (e) => {
        e.preventDefault();

            const event = {
                startDate: startDate.current.value,
                startTime: startTime.current.value,
                duration: duration.current.value,
                description: description.current.value,
                location: location.current.value,
            };
            try {
                await axios.post("/api/events/add", event);
                history.push("/events");
            } catch (err) {
                console.log(err);
                alert("Failed to add event: " + err)
            }

    };

    return (
        <>
            <Topbar/>
        <div className="login">
            <div className="loginWrapper">
                <div className="loginLeft">
                    <h3 className="loginLogo">NasiLjubimci</h3>
                    <span className="loginDesc">
            Connect with friends and pet owners around you on NasiLjubimci.
          </span>
                </div>
                <div className="loginRight">
                    <form className="registerBox" onSubmit={handleClick}>
                        <input
                            placeholder="Description"
                            required
                            ref={description}
                            className="loginInput"
                        />
                        <input
                            placeholder="Start date"
                            required
                            ref={startDate}
                            className="loginInput"
                            type="date"
                        />
                        <input
                            placeholder="Start time"
                            required
                            ref={startTime}
                            className="loginInput"
                            type="time"
                        />
                        <input
                            placeholder="Duration in Hrs"
                            required
                            ref={duration}
                            className="loginInput"
                            type="number"
                        />
                        <input
                            placeholder="Location"
                            required
                            ref={location}
                            className="loginInput"
                        />
                        <button className="loginButton" type="submit">
                            Submit
                        </button>
                    </form>
                </div>
            </div>
        </div>
        </>
    );
}
