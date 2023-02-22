import Topbar from "../../components/topbar/Topbar";
import Sidebar from "../../components/sidebar/Sidebar";
import Rightbar from "../../components/rightbar/Rightbar";
import "./events.css"
import EventsFeed from "../../components/eventsFeed/EventsFeed";

export default function Home() {
    return (
        <>
            <Topbar />
            <div className="homeContainer">
                <Sidebar />
                <EventsFeed/>
                <Rightbar/>
            </div>
        </>
    );
}
