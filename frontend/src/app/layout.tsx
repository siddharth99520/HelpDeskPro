import type { Metadata } from "next";
import "./globals.css";

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
    <html lang="en">
      <body>
        <header className="header-glass">
          <div className="container flex justify-between items-center" style={{ padding: '1rem 2rem' }}>
            <h1 style={{ fontSize: '1.2rem', fontWeight: 700, background: 'linear-gradient(to right, #3b82f6, #8b5cf6)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>
              HelpDeskPro
            </h1>
            <nav className="flex gap-2">
              <button className="btn btn-primary">Sign In</button>
            </nav>
          </div>
        </header>
        <main className="container">
          {children}
        </main>
      </body>
    </html>
  );
}
