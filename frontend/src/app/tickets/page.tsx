import Link from 'next/link';

interface Ticket {
  id: string;
  title: string;
  description: string;
  status: string;
  priority: string;
}

async function getTickets() {
  try {
    const res = await fetch('http://localhost:8080/api/tickets', {
      headers: {
        'X-User-Id': 'u1',
        'Content-Type': 'application/json'
      },
      cache: 'no-store'
    });
    
    if (!res.ok) {
      console.error("Failed to fetch tickets", res.status);
      return [];
    }
    
    return res.json();
  } catch (error) {
    console.error("Error fetching tickets:", error);
    return [];
  }
}

export default async function TicketsPage() {
  const tickets: Ticket[] = await getTickets();

  return (
    <div className="animate-fade-in flex flex-col gap-4">
      <div className="flex justify-between items-center" style={{ marginTop: '2rem' }}>
        <h2 style={{ fontSize: '2rem' }}>Active Tickets</h2>
        <Link href="/" className="btn" style={{ background: 'var(--border)' }}>Back</Link>
      </div>
      
      {tickets.length === 0 ? (
        <div className="card" style={{ textAlign: 'center', padding: '3rem' }}>
          <p style={{ color: 'var(--text-muted)' }}>No tickets found or backend is offline.</p>
        </div>
      ) : (
        <div style={{ display: 'grid', gap: '1rem' }}>
          {tickets.map(ticket => (
            <div key={ticket.id} className="card flex justify-between items-center">
              <div>
                <h3 style={{ fontSize: '1.2rem', marginBottom: '0.2rem' }}>{ticket.title}</h3>
                <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>{ticket.description}</p>
              </div>
              <div className="flex gap-2">
                <span style={{ 
                  padding: '0.3rem 0.6rem', 
                  borderRadius: '1rem', 
                  fontSize: '0.8rem',
                  background: 'var(--bg-base)',
                  border: '1px solid var(--border)'
                }}>
                  {ticket.priority}
                </span>
                <span style={{ 
                  padding: '0.3rem 0.6rem', 
                  borderRadius: '1rem', 
                  fontSize: '0.8rem',
                  background: 'rgba(59, 130, 246, 0.1)',
                  color: 'var(--primary)'
                }}>
                  {ticket.status}
                </span>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
