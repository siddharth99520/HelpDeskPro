"use client";

import Link from 'next/link';
import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { buttonVariants } from '@/components/ui/button';

interface DashboardStats {
  activeTickets: number;
  immediateAttentionTickets: number;
  assignedAssets: number;
  slaCompliance: number;
}

export default function Home() {
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [loading, setLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    const fetchStats = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        setLoading(false);
        return;
      }

      try {
        const res = await fetch('http://localhost:8080/api/dashboard/stats', {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });

        if (res.status === 401 || res.status === 403) {
          localStorage.removeItem("token");
          router.push("/login");
          return;
        }

        if (res.ok) {
          const data = await res.json();
          setStats(data);
        }
      } catch (error) {
        console.error("Error fetching dashboard stats:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, [router]);

  return (
    <div className="flex flex-col gap-12 animate-fade-in py-16">
      <section className="text-center space-y-6">
        <h2 className="text-4xl md:text-5xl font-extrabold tracking-tight">Manage IT Frictionlessly.</h2>
        <p className="text-xl text-muted-foreground max-w-2xl mx-auto">
          Streamline your ticketing, securely track assets, and automatically enforce SLAs with an enterprise-grade platform.
        </p>
        <div className="flex justify-center gap-4 pt-4">
          <Link href="/tickets" className={buttonVariants({ size: "lg", className: "px-8 text-base" })}>
            View Tickets
          </Link>
          <Link href="/assets" className={buttonVariants({ variant: "outline", size: "lg", className: "px-8 text-base" })}>
            View Assets
          </Link>
        </div>
      </section>

      <section className="grid grid-cols-1 md:grid-cols-3 gap-6 max-w-5xl mx-auto w-full">
        <Card className="hover:shadow-md transition-shadow">
          <CardHeader className="pb-2">
            <CardTitle className="text-primary font-medium text-lg">Active Tickets</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-4xl font-bold">
              {loading ? '...' : (stats?.activeTickets ?? 0)}
            </div>
            <p className="text-sm text-muted-foreground mt-2">
              {loading ? '...' : (stats?.immediateAttentionTickets ?? 0)} require immediate attention
            </p>
          </CardContent>
        </Card>
        
        <Card className="hover:shadow-md transition-shadow">
          <CardHeader className="pb-2">
            <CardTitle className="text-indigo-500 font-medium text-lg">Assigned Assets</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-4xl font-bold">
              {loading ? '...' : (stats?.assignedAssets ?? 0)}
            </div>
            <p className="text-sm text-muted-foreground mt-2">Across your organization</p>
          </CardContent>
        </Card>
        
        <Card className="hover:shadow-md transition-shadow">
          <CardHeader className="pb-2">
            <CardTitle className="text-emerald-500 font-medium text-lg">SLA Compliance</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-4xl font-bold">
              {loading ? '...' : `${stats?.slaCompliance ?? 0}%`}
            </div>
            <p className="text-sm text-muted-foreground mt-2">Overall resolution performance</p>
          </CardContent>
        </Card>
      </section>
    </div>
  );
}
