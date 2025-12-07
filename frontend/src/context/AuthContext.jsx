/* @refresh reset */
import { createContext, useCallback, useContext, useEffect, useMemo, useState } from "react";

const API_BASE_URL = import.meta.env.VITE_API_URL ?? "http://localhost:9999";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [accessToken, setAccessToken] = useState(null);
  const [status, setStatus] = useState("loading");

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
      setStatus("authenticated");
      return data.accessToken;
    } catch (error) {
      setAccessToken(null);
      setStatus("unauthenticated");
      return null;
    }
  }, []);

  useEffect(() => {
    refreshAccessToken();
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
    setStatus("authenticated");
    return data.accessToken;
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
    }
  }, []);

  const fetchWithAuth = useCallback(
    async (input, init = {}) => {
      const headers = new Headers(init.headers || {});
      if (accessToken) headers.set("Authorization", `Bearer ${accessToken}`);
      const res = await fetch(input, { ...init, headers, credentials: "include" });

      if (res.status === 401) {
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
      login,
      logout,
      refresh: refreshAccessToken,
      fetchWithAuth,
    };
  }, [accessToken, status, login, logout, refreshAccessToken, fetchWithAuth]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within AuthProvider");
  }
  return context;
}
