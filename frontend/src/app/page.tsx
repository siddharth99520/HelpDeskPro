"use client";

import Link from 'next/link';
import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';

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
    <div className="animate-fade-in flex flex-col gap-4">
      <section style={{ textAlign: 'center', padding: '4rem 0' }}>
        <h2 style={{ fontSize: '3rem', marginBottom: '1rem' }}>Manage IT Frictionlessly.</h2>
        <p style={{ color: 'var(--text-muted)', fontSize: '1.2rem', maxWidth: '600px', margin: '0 auto' }}>
          Streamline your ticketing, securely track assets, and automatically enforce SLAs with an enterprise-grade platform.
        </p>
        <div style={{ marginTop: '2rem' }} className="flex justify-center gap-2">
          <Link href="/tickets" className="btn btn-primary" style={{ padding: '0.8rem 2rem', fontSize: '1.1rem' }}>
            View Tickets
          </Link>
        </div>
      </section>

      <section style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '2rem' }}>
        <div className="card">
          <h3 style={{ color: 'var(--primary)', marginBottom: '0.5rem' }}>Active Tickets</h3>
          <p style={{ fontSize: '2rem', fontWeight: 'bold' }}>
            {loading ? '...' : (stats?.activeTickets ?? 0)}
          </p>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>
            {loading ? '...' : (stats?.immediateAttentionTickets ?? 0)} require immediate attention
          </p>
        </div>
        <div className="card">
          <h3 style={{ color: 'var(--accent)', marginBottom: '0.5rem' }}>Assigned Assets</h3>
          <p style={{ fontSize: '2rem', fontWeight: 'bold' }}>
            {loading ? '...' : (stats?.assignedAssets ?? 0)}
          </p>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Across your organization</p>
        </div>
        <div className="card">
          <h3 style={{ color: 'var(--success)', marginBottom: '0.5rem' }}>SLA Compliance</h3>
          <p style={{ fontSize: '2rem', fontWeight: 'bold' }}>
            {loading ? '...' : `${stats?.slaCompliance ?? 0}%`}
          </p>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Overall resolution performance</p>
        </div>
      </section>
    </div>
  );
}
