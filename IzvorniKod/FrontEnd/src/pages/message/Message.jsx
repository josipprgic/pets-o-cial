import Topbar from "../../components/topbar/Topbar";
import "./message.css"
import MessageSidebar from "../../components/messageSidebar/MessageSidebar";
import Messages from "../../components/message/Messages";
import {useParams} from "react-router-dom";

export default function Message() {
    const username = useParams().username;

    return (
        <>
            <Topbar />
            <div className="homeContainer">
                <MessageSidebar />
                <Messages username={username}/>
            </div>
        </>
    );
}
