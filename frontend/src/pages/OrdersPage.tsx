import { Card, CardContent, Grid, Stack, Typography } from '@mui/material'
import { useEffect, useState } from 'react'
import client from '../lib/client'

type Order = { id: number, restaurantId: number, totalAmount: number, status: string }

export default function OrdersPage() {
  const [orders, setOrders] = useState<Order[]>([])
  useEffect(() => {
    client.get('/orders').then(r => setOrders(r.data))
  }, [])
  return (
    <Stack spacing={3}>
      <Typography variant="h4" fontWeight={800}>Your Orders</Typography>
      <Grid container spacing={2}>
        {orders.map(o => (
          <Grid item xs={12} md={6} key={o.id}>
            <Card variant="outlined">
              <CardContent>
                <Typography variant="h6" fontWeight={700}>Order #{o.id}</Typography>
                <Typography color="text.secondary">Restaurant: {o.restaurantId}</Typography>
                <Typography color="text.secondary">Total: ${o.totalAmount}</Typography>
                <Typography>Status: {o.status}</Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Stack>
  )
}

