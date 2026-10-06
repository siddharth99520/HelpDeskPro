"use client";

import { useEffect, useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';

interface Asset {
  id: string;
  name: string;
  status: string;
  assignedTo?: string;
}

export default function AssetsPage() {
  const [assets, setAssets] = useState<Asset[]>([]);
  const [loading, setLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    const fetchAssets = async () => {
      const token = localStorage.getItem("token");
      if (!token) {
        router.push("/login");
        return;
      }

      try {
        const res = await fetch('http://localhost:8080/api/assets', {
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
          console.error("Failed to fetch assets", res.status);
          setLoading(false);
          return;
        }
        
        const data = await res.json();
        setAssets(data);
      } catch (error) {
        console.error("Error fetching assets:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchAssets();
  }, [router]);

  const updateAssetStatus = async (assetId: string, action: string) => {
    const token = localStorage.getItem("token");
    try {
      const res = await fetch(`http://localhost:8080/api/assets/${assetId}/${action}`, {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
      if (res.ok) {
        let newStatus = action.toUpperCase();
        if (action === 'return') newStatus = 'AVAILABLE';
        if (action === 'repair') newStatus = 'IN_REPAIR';
        if (action === 'retire') newStatus = 'RETIRED';

        setAssets(assets.map(a => a.id === assetId ? { ...a, status: newStatus, assignedTo: action === 'return' ? undefined : a.assignedTo } : a));
      } else {
        alert(`Failed to ${action} asset`);
      }
    } catch (err) {
      alert(`Error trying to ${action} asset`);
    }
  };

  if (loading) {
    return <div style={{ textAlign: "center", padding: "4rem" }}>Loading assets...</div>;
  }

  return (
    <div className="animate-fade-in flex flex-col gap-4">
      <div className="flex justify-between items-center" style={{ marginTop: '2rem' }}>
        <h2 style={{ fontSize: '2rem' }}>Assets Inventory</h2>
        <div className="flex gap-2">
          <Link href="/assets/new" className="btn btn-primary">Add Asset</Link>
          <Link href="/" className="btn" style={{ background: 'var(--border)' }}>Back</Link>
        </div>
      </div>
      
      {assets.length === 0 ? (
        <div className="card" style={{ textAlign: 'center', padding: '3rem' }}>
          <p style={{ color: 'var(--text-muted)' }}>No assets found in inventory.</p>
        </div>
      ) : (
        <div style={{ display: 'grid', gap: '1rem' }}>
          {assets.map(asset => (
            <div key={asset.id} className="card flex justify-between items-center">
              <div>
                <h3 style={{ fontSize: '1.2rem', marginBottom: '0.2rem' }}>{asset.name}</h3>
                <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>ID: {asset.id}</p>
                {asset.assignedTo && <p style={{ color: 'var(--text-muted)', fontSize: '0.9rem' }}>Assigned to: {asset.assignedTo}</p>}
              </div>
              <div className="flex gap-2">
                <span style={{ 
                  padding: '0.3rem 0.6rem', 
                  borderRadius: '1rem', 
                  fontSize: '0.8rem',
                  background: 'rgba(59, 130, 246, 0.1)',
                  color: 'var(--primary)'
                }}>
                  {asset.status}
                </span>

                <select
                  value=""
                  onChange={(e) => {
                    const action = e.target.value;
                    if (action) {
                      updateAssetStatus(asset.id, action);
                    }
                  }}
                  style={{ 
                    padding: '0.3rem 0.6rem', 
                    borderRadius: '1rem', 
                    fontSize: '0.8rem',
                    background: 'var(--bg-base)',
                    border: '1px solid var(--border)',
                    outline: 'none',
                    cursor: 'pointer'
                  }}
                >
                  <option value="" disabled>Actions...</option>
                  {asset.status !== 'AVAILABLE' && <option value="return">Return Asset</option>}
                  {asset.status !== 'IN_REPAIR' && asset.status !== 'RETIRED' && <option value="repair">Send to Repair</option>}
                  {asset.status !== 'RETIRED' && <option value="retire">Retire Asset</option>}
                </select>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
