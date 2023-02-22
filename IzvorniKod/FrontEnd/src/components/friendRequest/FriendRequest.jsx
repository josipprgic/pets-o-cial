import "./friendRequest.css";
import {CircularProgress} from "@material-ui/core";
import axios from "axios";

export default function FriendRequest({user}) {

    const handleApprove = async () => {
        try {
            await axios.post("/api/users/approveRequest", {to: user.username})
            window.location.reload();
        } catch (e) {
            alert("Failed to approve Friend request")
        }
    }

    const handleDecline = async () => {
        try {
            await axios.post("/api/users/declineRequest", {to: user.username})
            window.location.reload();
        } catch (e) {
            alert("Failed to decline Friend request")
        }
    }

    const handleBlock = async () => {
        try {
            await axios.post("/api/users/block", {to: user.username})
            window.location.reload();
        } catch (e) {
            alert("Failed to block user")
        }
    }

    return (
        <div className="friendRequestAction">
        <button className="approve"
                onClick={handleApprove}>
            {"Approve"}
        </button>
        <button className="decline"
                onClick={handleDecline}>
            {"Decline"}
        </button>
            <button className="block"
                    onClick={handleBlock}>
                {"Block"}
            </button>
        </div>
    );
}
