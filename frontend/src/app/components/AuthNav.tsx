"use client";

import { useEffect, useState } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";

import { Button } from "@/components/ui/button";

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
        <span className="text-muted-foreground text-sm font-medium">Welcome, {userName}</span>
        <Button onClick={handleLogout} variant="outline" size="sm">Sign Out</Button>
      </div>
    );
  }

  return (
    <Button asChild size="sm">
      <Link href="/login">Sign In</Link>
    </Button>
  );
}
