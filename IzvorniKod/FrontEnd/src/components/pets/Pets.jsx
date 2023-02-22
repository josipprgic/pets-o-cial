import "./pets.css";

export default function Pets({pet}) {
    const PF = process.env.REACT_APP_PUBLIC_FOLDER;
    return (
        <li className="sidebarFriend">
            <img className="sidebarFriendImg" src={"data:image/png;base64, " +pet.profilePicture} alt="" />
            <span className="sidebarFriendName">{pet.name}</span>
        </li>
    );
}
