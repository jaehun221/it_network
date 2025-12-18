/* @refresh reset */
import { createContext, useCallback, useContext, useEffect, useMemo, useState } from "react";

const API_BASE_URL = import.meta.env.VITE_API_URL ?? "http://localhost:9999";

const AuthContext = createContext(null);

const STORAGE_KEY_TOKEN = "accessToken";
const STORAGE_KEY_USER = "userInfo";

export function AuthProvider({ children }) {
  console.log(API_BASE_URL);
  const [accessToken, setAccessToken] = useState(null);
  const [status, setStatus] = useState("loading");
  const [userInfo, setUserInfo] = useState(null);

  const refreshAccessToken = useCallback(async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/auth/refresh`, {
        method: "POST",
        credentials: "include",
      });

      if (!response.ok) {
        throw new Error("Session refresh failed.");
      }

      const data = await response.json();

      if (!data?.accessToken) {
        throw new Error("Access token missing from response.");
      }

      setAccessToken(data.accessToken);
      localStorage.setItem(STORAGE_KEY_TOKEN, data.accessToken);
      setStatus("authenticated");
      setUserInfo(data.userInfo ?? null);
      if (data.userInfo) {
        localStorage.setItem(STORAGE_KEY_USER, JSON.stringify(data.userInfo));
      }
      return data.accessToken;
    } catch (error) {
      setAccessToken(null);
      setStatus("unauthenticated");
      setUserInfo(null);
      localStorage.removeItem(STORAGE_KEY_TOKEN);
      localStorage.removeItem(STORAGE_KEY_USER);
      return null;
    }
  }, []);

  useEffect(() => {
    const savedToken = localStorage.getItem(STORAGE_KEY_TOKEN);
    const savedUser = localStorage.getItem(STORAGE_KEY_USER);
    
    if (savedToken && savedUser) {
      setAccessToken(savedToken);
      setUserInfo(JSON.parse(savedUser));
      setStatus("authenticated");
    } else {
      refreshAccessToken();
    }
  }, [refreshAccessToken]);

  const login = useCallback(async (email, password) => {
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
      method: "POST",
      credentials: "include",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password }),
    });

    if (!response.ok) {
      const text = await response.text();
      throw new Error(text || "Login failed.");
    }

    const data = await response.json();

    if (!data?.accessToken) {
      throw new Error("Access token missing from response.");
    }

    setAccessToken(data.accessToken);
    localStorage.setItem(STORAGE_KEY_TOKEN, data.accessToken);
    setStatus("authenticated");
    setUserInfo(data.userInfo ?? null);
    if (data.userInfo) {
      localStorage.setItem(STORAGE_KEY_USER, JSON.stringify(data.userInfo));
    }

    return data.userInfo ?? null;
  }, []);

  const logout = useCallback(async () => {
    try {
      await fetch(`${API_BASE_URL}/auth/logout`, {
        method: "POST",
        credentials: "include",
      });
    } finally {
      setAccessToken(null);
      setStatus("unauthenticated");
      setUserInfo(null);
      localStorage.removeItem(STORAGE_KEY_TOKEN);
      localStorage.removeItem(STORAGE_KEY_USER);
    }
  }, []);

  const fetchWithAuth = useCallback(
    async (input, init = {}) => {
      const headers = new Headers(init.headers || {});
      if (accessToken) headers.set("Authorization", `Bearer ${accessToken}`);
      const res = await fetch(input, { ...init, headers, credentials: "include" });

      if (res.status === 401 || res.status === 403) {
        const newToken = await refreshAccessToken();
        if (newToken) {
          headers.set("Authorization", `Bearer ${newToken}`);
          return fetch(input, { ...init, headers, credentials: "include" });
        }
      }
      return res;
    },
    [accessToken, refreshAccessToken]
  );

  const value = useMemo(() => {
    return {
      accessToken,
      status,
      isAuthenticated: status === "authenticated",
      isLoading: status === "loading",
      userInfo, // userInfo 추가
      login,
      logout,
      refresh: refreshAccessToken,
      fetchWithAuth,
    };
  }, [accessToken, status, userInfo, login, logout, refreshAccessToken, fetchWithAuth]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within AuthProvider");
  }
  return context;
}
