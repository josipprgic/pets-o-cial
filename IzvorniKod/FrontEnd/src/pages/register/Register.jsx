import axios from "axios";
import {useRef, useState} from "react";
import "./register.css";
import {useHistory} from "react-router";

export default function Register() {
    const username = useRef();
    const email = useRef();
    const password = useRef();
    const passwordAgain = useRef();
    const firstName = useRef();
    const lastName = useRef();
    const type = useRef();
    const companyName = useRef();
    const companyAddress = useRef();
    const companyContact = useRef();
    const history = useHistory();
    const [userType, setUserType] = useState('USER');

    const handleLogin = () => {
        history.push("/login");
    };

    const handleClick = async (e) => {
        e.preventDefault();
        if (passwordAgain.current.value !== password.current.value) {
            passwordAgain.current.setCustomValidity("Passwords don't match!");
        } else {
            const user = userType === 'USER' ? {
                username: username.current.value,
                email: email.current.value,
                password: password.current.value,
                firstname: firstName.current.value,
                lastname: lastName.current.value,
                userType: type.current.value,
            } : {
                    username: username.current.value,
                    email: email.current.value,
                    password: password.current.value,
                    firstname: firstName.current.value,
                    lastname: lastName.current.value,
                    userType: type.current.value,
                    companyAddress: companyAddress.current.value,
                    companyName: companyName.current.value,
                    companyContact: companyContact.current.value,
                }
            ;
            try {
                const res = userType === 'USER' ? await axios.post("/api/users/register", user) : await axios.post("/api/users/registerCompany", user) ;
                history.push("/login");
            } catch (err) {
                console.log(err);
                alert("Failed to register: " + err)
            }
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
                            placeholder="Username"
                            required
                            ref={username}
                            className="loginInput"
                        />
                        <input
                            placeholder="Email"
                            required
                            ref={email}
                            className="loginInput"
                            type="email"
                        />
                        <input
                            placeholder="Password"
                            required
                            ref={password}
                            className="loginInput"
                            type="password"
                            minLength="6"
                        />
                        <input
                            placeholder="Password Again"
                            required
                            ref={passwordAgain}
                            className="loginInput"
                            type="password"
                        />
                        <input
                            placeholder="First Name"
                            required
                            ref={firstName}
                            className="loginInput"
                        />
                        <input
                            placeholder="Last Name"
                            required
                            ref={lastName}
                            className="loginInput"
                        />
                        <select placeholder="Type"
                                required
                                ref={type}
                                className="loginInput"
                                onChange={(e) => setUserType(type.current.value)}>
                            <option value="USER">USER</option>
                            <option value="COMPANY">COMPANY</option>
                        </select>
                        {
                            userType == 'COMPANY' && (
                                <input
                                    placeholder="Comapny Name"
                                    required
                                    ref={companyName}
                                    className="loginInput"
                                />)}
                        {
                            userType == 'COMPANY' && (
                                <input
                                    placeholder="Company Address"
                                    required
                                    ref={companyAddress}
                                    className="loginInput"
                                />)}
                        {
                            userType == 'COMPANY' && (
                                <input
                                    placeholder="Company Contact"
                                    required
                                    ref={companyContact}
                                    className="loginInput"
                                />)}

                        <button className="loginButton" type="submit">
                            Sign Up
                        </button>
                        <button className="loginRegisterButton"
                                onClick={handleLogin}>Log into Account</button>
                    </form>
                </div>
            </div>
        </div>
    );
}
