import axios from "axios";
import {useContext, useRef} from "react";
import "./addPet.css";
import {useHistory} from "react-router";
import {AuthContext} from "../../context/AuthContext";

export default function AddPet() {
    const petname = useRef();
    const petType = useRef();
    const age = useRef();
    const gender = useRef();
    const description = useRef();
    const breed = useRef();
    const history = useHistory();
    const { user } = useContext(AuthContext);

    const handleClick = async (e) => {
        e.preventDefault();

            const pet = {
                petname: petname.current.value,
                petType: petType.current.value,
                age: age.current.value,
                gender: gender.current.value,
                description: description.current.value,
                breed: breed.current.value,
            };
            try {
                await axios.post("/api/pets/add", pet);
                history.push("/pet/" + user.username + "/" + petname.current.value);
            } catch (err) {
                console.log(err);
                alert("Failed to register: " + err)
            }

    };

    return (
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
                            placeholder="Pet Name"
                            required
                            ref={petname}
                            className="loginInput"
                        />
                        <select placeholder="Pet Type"
                                required
                                ref={petType}
                                className="loginInput">
                            <option value="DOG">DOG</option>
                            <option value="CAT">CAT</option>
                            <option value="RODENT">RODENT</option>
                            <option value="BIRD">BIRD</option>
                            <option value="REPTILE">REPTILE</option>
                            <option value="EXOTIC">EXOTIC</option>
                        </select>
                        <input
                            placeholder="Age"
                            required
                            ref={age}
                            className="loginInput"
                            type="number"
                        />
                        <select placeholder="Gender"
                                required
                                ref={gender}
                                className="loginInput">
                            <option value="MALE">MALE</option>
                            <option value="FEMALE">FEMALE</option>
                        </select>
                        <input
                            placeholder="Description"
                            required
                            ref={description}
                            className="loginInput"
                        />
                        <input
                            placeholder="Breed"
                            ref={breed}
                            className="loginInput"
                        />
                        <button className="loginButton" type="submit">
                            Submit
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
}
