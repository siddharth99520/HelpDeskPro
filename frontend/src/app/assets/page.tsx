"use client";

import { useEffect, useState } from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@/components/ui/card';
import { Button, buttonVariants } from '@/components/ui/button';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';

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
    return <div className="text-center p-16 text-muted-foreground">Loading assets...</div>;
  }

  return (
    <div className="flex flex-col gap-6 animate-fade-in py-8">
      <div className="flex justify-between items-center">
        <div>
          <h2 className="text-3xl font-bold tracking-tight">Assets Inventory</h2>
          <p className="text-muted-foreground">Manage and track hardware across the organization.</p>
        </div>
        <div className="flex gap-4">
          <Link href="/" className={buttonVariants({ variant: "outline" })}>
            Back
          </Link>
          <Link href="/assets/new" className={buttonVariants()}>
            Add Asset
          </Link>
        </div>
      </div>
      
      {assets.length === 0 ? (
        <Card className="text-center p-12 mt-8">
          <p className="text-muted-foreground">No assets found in inventory.</p>
        </Card>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {assets.map(asset => (
            <Card key={asset.id} className="hover:shadow-md transition-shadow flex flex-col justify-between">
              <CardHeader className="pb-4">
                <CardTitle className="text-xl font-medium">{asset.name}</CardTitle>
                <CardDescription className="text-xs">ID: {asset.id}</CardDescription>
                {asset.assignedTo && (
                  <p className="text-sm text-muted-foreground pt-2">Assigned to: <span className="font-semibold text-foreground">{asset.assignedTo}</span></p>
                )}
              </CardHeader>
              <CardContent className="flex justify-between items-center pb-6">
                <span className="text-xs font-semibold px-2.5 py-1 rounded-md bg-secondary text-secondary-foreground border border-border">
                  {asset.status}
                </span>

                <Select onValueChange={(val) => updateAssetStatus(asset.id, val)} value="">
                  <SelectTrigger className="w-[140px] h-8 text-xs">
                    <SelectValue placeholder="Actions..." />
                  </SelectTrigger>
                  <SelectContent>
                    {asset.status !== 'AVAILABLE' && <SelectItem value="return">Return Asset</SelectItem>}
                    {asset.status !== 'IN_REPAIR' && asset.status !== 'RETIRED' && <SelectItem value="repair">Send to Repair</SelectItem>}
                    {asset.status !== 'RETIRED' && <SelectItem value="retire">Retire Asset</SelectItem>}
                  </SelectContent>
                </Select>
              </CardContent>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
}
