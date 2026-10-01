"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";

export default function AuthNav() {
  const [userName, setUserName] = useState<string | null>(null);
  const router = useRouter();

  useEffect(() => {
    // Check if user is logged in
    const storedName = localStorage.getItem("userName");
    const token = localStorage.getItem("token");
    
    if (token && storedName) {
      setUserName(storedName);
    }
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("userId");
    localStorage.removeItem("userName");
    setUserName(null);
    router.push("/login");
  };

  if (userName) {
    return (
      <div className="flex items-center gap-4">
        <span style={{ color: "var(--text-muted)", fontSize: "0.9rem" }}>Welcome, {userName}</span>
        <button onClick={handleLogout} className="btn" style={{ background: "transparent", border: "1px solid var(--border)" }}>Sign Out</button>
      </div>
    );
  }

  return (
    <Link href="/login" className="btn btn-primary">Sign In</Link>
  );
}
