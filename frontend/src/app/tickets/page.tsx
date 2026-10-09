"use client";

import { useEffect, useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@/components/ui/card';
import { Button, buttonVariants } from '@/components/ui/button';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';

interface Ticket {
  id: string;
  title: string;
  description: string;
  status: string;
  priority: string;
}

const STATUSES = ['OPEN', 'ASSIGNED', 'IN_PROGRESS', 'RESOLVED', 'CLOSED'];

export default function TicketsPage() {
  const [tickets, setTickets] = useState<Ticket[]>([]);
  const [loading, setLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    const fetchTickets = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        router.push("/login");
        return;
      }

      try {
        const res = await fetch('http://localhost:8080/api/tickets', {
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

        if (!res.ok) {
          console.error("Failed to fetch tickets", res.status);
          setLoading(false);
          return;
        }
        
        const data = await res.json();
        setTickets(data);
      } catch (error) {
        console.error("Error fetching tickets:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchTickets();
  }, [router]);

  const updateTicketStatus = async (ticketId: string, newStatus: string) => {
    const token = localStorage.getItem("token");
    try {
      const res = await fetch(`http://localhost:8080/api/tickets/${ticketId}/status`, {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ status: newStatus })
      });
      if (res.ok) {
        setTickets(tickets.map(t => t.id === ticketId ? { ...t, status: newStatus } : t));
      } else {
        const err = await res.json();
        alert(err.message || 'Failed to update status');
      }
    } catch (err) {
      alert('Error updating status');
    }
  };

  if (loading) {
    return <div className="text-center p-16 text-muted-foreground">Loading tickets...</div>;
  }

  return (
    <div className="flex flex-col gap-6 animate-fade-in py-8 h-full">
      <div className="flex justify-between items-center">
        <div>
          <h2 className="text-3xl font-bold tracking-tight">Ticket Kanban</h2>
          <p className="text-muted-foreground">Manage and triage active tickets.</p>
        </div>
        <div className="flex gap-4">
          <Link href="/" className={buttonVariants({ variant: "outline" })}>
            Back
          </Link>
          <Link href="/tickets/new" className={buttonVariants()}>
            Create Ticket
          </Link>
        </div>
      </div>
      
      {tickets.length === 0 ? (
        <Card className="text-center p-12 mt-8">
          <p className="text-muted-foreground">No tickets found or backend is offline.</p>
        </Card>
      ) : (
        <div className="flex gap-4 overflow-x-auto pb-4 h-[calc(100vh-200px)]">
          {STATUSES.map(status => {
            const columnTickets = tickets.filter(t => t.status === status);
            return (
              <div key={status} className="flex-1 min-w-[320px] bg-muted/30 rounded-xl p-4 flex flex-col gap-4 border">
                <div className="flex items-center justify-between px-2">
                  <h3 className="font-semibold text-sm tracking-wide text-muted-foreground">{status.replace('_', ' ')}</h3>
                  <span className="text-xs bg-muted px-2 py-1 rounded-full font-medium">{columnTickets.length}</span>
                </div>
                
                <div className="flex flex-col gap-3 overflow-y-auto">
                  {columnTickets.map(ticket => (
                    <Card key={ticket.id} className="cursor-grab hover:shadow-md transition-all">
                      <CardHeader className="p-4 pb-2">
                        <div className="flex justify-between items-start">
                          <CardTitle className="text-base font-medium line-clamp-1">{ticket.title}</CardTitle>
                        </div>
                        <CardDescription className="line-clamp-2 text-xs mt-1.5">{ticket.description}</CardDescription>
                      </CardHeader>
                      <CardContent className="p-4 pt-0 flex justify-between items-center">
                        <span className="text-xs font-medium px-2 py-1 bg-secondary rounded-md">
                          {ticket.priority}
                        </span>
                        
                        <Select value={ticket.status} onValueChange={(val) => updateTicketStatus(ticket.id, val)}>
                          <SelectTrigger className="w-[110px] h-7 text-xs border-dashed">
                            <SelectValue placeholder="Status" />
                          </SelectTrigger>
                          <SelectContent>
                            {STATUSES.map(s => (
                              <SelectItem key={s} value={s} className="text-xs">{s.replace('_', ' ')}</SelectItem>
                            ))}
                          </SelectContent>
                        </Select>
                      </CardContent>
                    </Card>
                  ))}
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}
