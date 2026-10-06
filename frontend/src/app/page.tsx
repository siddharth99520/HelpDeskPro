import Link from 'next/link';

export default function Home() {
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

      <div style={{ textAlign: 'center', marginBottom: '1rem' }}>
        <span style={{ background: 'var(--bg-surface-hover)', padding: '0.2rem 0.5rem', borderRadius: 'var(--radius-sm)', fontSize: '0.8rem', color: 'var(--warning)' }}>
          Sample Dashboard (Mockup)
        </span>
      </div>
      <section style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '2rem' }}>
        <div className="card">
          <h3 style={{ color: 'var(--primary)', marginBottom: '0.5rem' }}>Active Tickets</h3>
          <p style={{ fontSize: '2rem', fontWeight: 'bold' }}>24</p>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>4 require immediate attention</p>
        </div>
        <div className="card">
          <h3 style={{ color: 'var(--accent)', marginBottom: '0.5rem' }}>Assigned Assets</h3>
          <p style={{ fontSize: '2rem', fontWeight: 'bold' }}>1,204</p>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Across 3 regional offices</p>
        </div>
        <div className="card">
          <h3 style={{ color: 'var(--success)', marginBottom: '0.5rem' }}>SLA Compliance</h3>
          <p style={{ fontSize: '2rem', fontWeight: 'bold' }}>98.2%</p>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Up 2.4% from last month</p>
        </div>
      </section>
    </div>
  );
}
