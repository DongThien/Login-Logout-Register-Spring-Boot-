import { useEffect, useState } from "react";
import { loginApi, logoutApi, meApi, registerApi } from "./api/authApi";
import "./index.css";

const TOKEN_KEY = "accessToken";

export default function App() {
    const [tab, setTab] = useState("login");
    const [token, setToken] = useState(localStorage.getItem(TOKEN_KEY) || "");
    const [me, setMe] = useState(null);
    const [message, setMessage] = useState("");

    const [registerForm, setRegisterForm] = useState({
        username: "",
        password: "",
        email: "",
    });

    const [loginForm, setLoginForm] = useState({
        username: "",
        password: "",
    });

    useEffect(() => {
        if (!token) {
            setMe(null);
            return;
        }

        meApi(token)
            .then((data) => setMe(data))
            .catch(() => {
                localStorage.removeItem(TOKEN_KEY);
                setToken("");
                setMe(null);
            });
    }, [token]);

    const onRegister = async (e) => {
        e.preventDefault();
        setMessage("");
        try {
            const res = await registerApi(registerForm);
            if (res.token) {
                localStorage.setItem(TOKEN_KEY, res.token);
                setToken(res.token);
            }
            setMessage("Đăng ký thành công.");
        } catch (err) {
            setMessage(`Đăng ký thất bại: ${err.message}`);
        }
    };

    const onLogin = async (e) => {
        e.preventDefault();
        setMessage("");
        try {
            const res = await loginApi(loginForm);
            localStorage.setItem(TOKEN_KEY, res.token);
            setToken(res.token);
            setMessage("Đăng nhập thành công.");
        } catch (err) {
            setMessage(`Đăng nhập thất bại: ${err.message}`);
        }
    };

    const onLogout = async () => {
        setMessage("");
        try {
            await logoutApi(token);
        } catch {
            // ignore
        } finally {
            localStorage.removeItem(TOKEN_KEY);
            setToken("");
            setMe(null);
            setMessage("Đã đăng xuất.");
        }
    };

    return (
        <div className="container">
            <h1>JWT Auth Demo</h1>

            {!token ? (
                <>
                    <div className="tabs">
                        <button
                            className={tab === "login" ? "active" : ""}
                            onClick={() => setTab("login")}
                        >
                            Đăng nhập
                        </button>
                        <button
                            className={tab === "register" ? "active" : ""}
                            onClick={() => setTab("register")}
                        >
                            Đăng ký
                        </button>
                    </div>

                    {tab === "login" ? (
                        <form className="card" onSubmit={onLogin}>
                            <input
                                placeholder="Username"
                                value={loginForm.username}
                                onChange={(e) =>
                                    setLoginForm({ ...loginForm, username: e.target.value })
                                }
                            />
                            <input
                                type="password"
                                placeholder="Password"
                                value={loginForm.password}
                                onChange={(e) =>
                                    setLoginForm({ ...loginForm, password: e.target.value })
                                }
                            />
                            <button type="submit">Đăng nhập</button>
                        </form>
                    ) : (
                        <form className="card" onSubmit={onRegister}>
                            <input
                                placeholder="Username"
                                value={registerForm.username}
                                onChange={(e) =>
                                    setRegisterForm({ ...registerForm, username: e.target.value })
                                }
                            />
                            <input
                                type="password"
                                placeholder="Password"
                                value={registerForm.password}
                                onChange={(e) =>
                                    setRegisterForm({ ...registerForm, password: e.target.value })
                                }
                            />
                            <input
                                placeholder="Email"
                                value={registerForm.email}
                                onChange={(e) =>
                                    setRegisterForm({ ...registerForm, email: e.target.value })
                                }
                            />
                            <button type="submit">Đăng ký</button>
                        </form>
                    )}
                </>
            ) : (
                <div className="card">
                    <h3>Thông tin tài khoản</h3>
                    <p><b>Username:</b> {me?.username || "-"}</p>
                    <p><b>Email:</b> {me?.email || "-"}</p>
                    <p><b>Roles:</b> {me?.roles?.join(", ") || "-"}</p>
                    <button onClick={onLogout}>Đăng xuất</button>
                </div>
            )}

            {message && <p className="message">{message}</p>}
        </div>
    );
}