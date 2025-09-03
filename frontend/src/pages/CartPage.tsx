import { Alert, Button, Card, CardContent, Stack, TextField, Typography } from '@mui/material'
import { useState } from 'react'
import client from '../lib/client'
import { useAuth } from '../contexts/AuthContext'

export default function CartPage() {
  const { isAuthenticated } = useAuth()
  const [restaurantId, setRestaurantId] = useState('')
  const [amount, setAmount] = useState('')
  const [message, setMessage] = useState<string | undefined>()
  const [error, setError] = useState<string | undefined>()

  async function checkout() {
    setError(undefined); setMessage(undefined)
    try {
      const order = await client.post('/orders', {
        restaurantId: Number(restaurantId), totalAmount: Number(amount)
      })
      await client.post('/payments', { orderId: order.data.id, amount: Number(amount) })
      setMessage(`Order #${order.data.id} paid successfully!`)
    } catch (e: any) {
      setError(e?.response?.data?.message || 'Checkout failed')
    }
  }

  return (
    <Stack spacing={3}>
      <Typography variant="h4" fontWeight={800}>Cart / Checkout</Typography>
      {!isAuthenticated && <Alert severity="info">Login to place orders for a personalized experience.</Alert>}
      <Card variant="outlined">
        <CardContent>
          <Stack spacing={2}>
            {message && <Alert severity="success">{message}</Alert>}
            {error && <Alert severity="error">{error}</Alert>}
            <TextField label="Restaurant ID" value={restaurantId} onChange={e => setRestaurantId(e.target.value)} fullWidth />
            <TextField label="Total Amount" value={amount} onChange={e => setAmount(e.target.value)} fullWidth />
            <Button variant="contained" size="large" onClick={checkout}>Pay & Place Order</Button>
          </Stack>
        </CardContent>
      </Card>
    </Stack>
  )
}

