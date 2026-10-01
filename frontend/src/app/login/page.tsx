"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";

export default function LoginPage() {
  const [userId, setUserId] = useState("u1");
  const [password, setPassword] = useState("password");
  const [error, setError] = useState("");
  const router = useRouter();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");

    try {
      const res = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ userId, password }),
      });

      if (!res.ok) {
        throw new Error("Invalid credentials");
      }

      const data = await res.json();
      localStorage.setItem("token", data.accessToken);
      localStorage.setItem("userId", data.userId);
      localStorage.setItem("userName", data.name);
      
      // Force hard navigation to apply layout changes
      window.location.href = "/tickets";
    } catch (err: any) {
      setError(err.message || "Failed to login");
    }
  };

  return (
    <div className="flex justify-center items-center" style={{ minHeight: "70vh" }}>
      <div className="card animate-fade-in" style={{ width: "100%", maxWidth: "400px", padding: "2rem" }}>
        <h2 style={{ fontSize: "1.5rem", marginBottom: "1.5rem", textAlign: "center" }}>Sign In</h2>
        
        {error && (
          <div style={{ background: "rgba(239, 68, 68, 0.1)", color: "#ef4444", padding: "0.8rem", borderRadius: "0.5rem", marginBottom: "1rem", fontSize: "0.9rem" }}>
            {error}
          </div>
        )}

        <form onSubmit={handleLogin} className="flex flex-col gap-4">
          <div>
            <label style={{ display: "block", marginBottom: "0.5rem", fontSize: "0.9rem", color: "var(--text-muted)" }}>User ID</label>
            <input 
              type="text" 
              value={userId}
              onChange={(e) => setUserId(e.target.value)}
              style={{ width: "100%", padding: "0.8rem", borderRadius: "0.5rem", background: "var(--bg-base)", border: "1px solid var(--border)", color: "var(--text)" }}
              required
            />
            <small style={{ color: "var(--text-muted)", fontSize: "0.8rem" }}>Try: u1, u2, or u3</small>
          </div>
          
          <div>
            <label style={{ display: "block", marginBottom: "0.5rem", fontSize: "0.9rem", color: "var(--text-muted)" }}>Password</label>
            <input 
              type="password" 
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              style={{ width: "100%", padding: "0.8rem", borderRadius: "0.5rem", background: "var(--bg-base)", border: "1px solid var(--border)", color: "var(--text)" }}
              required
            />
            <small style={{ color: "var(--text-muted)", fontSize: "0.8rem" }}>Default: password</small>
          </div>

          <button type="submit" className="btn btn-primary" style={{ padding: "0.8rem", marginTop: "1rem" }}>
            Sign In
          </button>
        </form>

        <div style={{ textAlign: "center", marginTop: "1.5rem" }}>
          <Link href="/" style={{ color: "var(--text-muted)", fontSize: "0.9rem" }}>Back to Home</Link>
        </div>
      </div>
    </div>
  );
}
