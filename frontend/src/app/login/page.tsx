"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { Card, CardContent, CardDescription, CardHeader, CardTitle, CardFooter } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";

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
    <div className="flex justify-center items-center min-h-[70vh]">
      <Card className="w-full max-w-[400px] animate-fade-in">
        <CardHeader className="text-center">
          <CardTitle className="text-2xl">Sign In</CardTitle>
          <CardDescription>Enter your credentials to access the portal</CardDescription>
        </CardHeader>
        
        <CardContent>
          {error && (
            <div className="bg-destructive/15 text-destructive p-3 rounded-md mb-4 text-sm font-medium text-center">
              {error}
            </div>
          )}

          <form onSubmit={handleLogin} className="flex flex-col gap-4">
            <div className="space-y-1.5">
              <label className="text-sm font-medium text-muted-foreground">User ID</label>
              <Input 
                type="text" 
                value={userId}
                onChange={(e) => setUserId(e.target.value)}
                required
                className="bg-muted/50"
              />
              <p className="text-xs text-muted-foreground">Try: u1, u2, or u3</p>
            </div>
            
            <div className="space-y-1.5">
              <label className="text-sm font-medium text-muted-foreground">Password</label>
              <Input 
                type="password" 
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                className="bg-muted/50"
              />
              <p className="text-xs text-muted-foreground">Default: password</p>
            </div>

            <Button type="submit" className="w-full mt-2">
              Sign In
            </Button>
          </form>
        </CardContent>

        <CardFooter className="flex justify-center">
          <Link href="/" className="text-sm text-muted-foreground hover:text-primary transition-colors">
            Back to Home
          </Link>
        </CardFooter>
      </Card>
    </div>
  );
}
