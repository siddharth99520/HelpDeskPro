import type { Metadata } from "next";
import Link from "next/link";
import AuthNav from "./components/AuthNav";
import "./globals.css";
import { Geist } from "next/font/google";
import { cn } from "@/lib/utils";

const geist = Geist({subsets:['latin'],variable:'--font-sans'});

export const metadata: Metadata = {
  title: "HelpDeskPro | Enterprise Service Desk",
  description: "Modern, secure, and blazing fast IT Help Desk and Asset Management",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" className={cn("font-sans dark", geist.variable)}>
      <body className="min-h-screen bg-background font-sans antialiased text-foreground">
        <header className="sticky top-0 z-50 w-full border-b border-border/40 bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
          <div className="container flex h-14 max-w-screen-2xl items-center justify-between">
            <h1 className="text-xl font-bold bg-gradient-to-r from-blue-500 to-indigo-500 bg-clip-text text-transparent">
              HelpDeskPro
            </h1>
            <nav className="flex items-center gap-4">
              <AuthNav />
            </nav>
          </div>
        </header>
        <main className="container max-w-screen-2xl mx-auto flex-1 h-[calc(100vh-3.5rem)]">
          {children}
        </main>
      </body>
    </html>
  );
}
