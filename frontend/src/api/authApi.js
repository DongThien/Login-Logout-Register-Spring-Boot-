const BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

async function request(url, options) {
    try {
        const res = await fetch(url, options);
        return await handleResponse(res);
    } catch (e) {
        throw new Error("Không kết nối được backend (check backend chạy, URL, CORS).");
    }
}

export async function registerApi(payload) {
    return request(`${BASE_URL}/api/auth/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
    });
}

export async function loginApi(payload) {
    return request(`${BASE_URL}/api/auth/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
    });
}

export async function logoutApi(token) {
    const res = await fetch(`${BASE_URL}/api/auth/logout`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: token ? `Bearer ${token}` : "",
        },
    });
    return handleResponse(res);
}

export async function meApi(token) {
    const res = await fetch(`${BASE_URL}/api/users/me`, {
        headers: { Authorization: `Bearer ${token}` },
    });
    return handleResponse(res);
}

async function handleResponse(res) {
    const data = await res.json().catch(() => ({}));
    if (!res.ok) throw new Error(data.message || `HTTP ${res.status}`);
    return data;
}