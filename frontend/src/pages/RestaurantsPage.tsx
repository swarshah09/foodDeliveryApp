import { Card, CardActionArea, CardContent, Grid, Stack, Typography } from '@mui/material'
import { useEffect, useState } from 'react'
import client from '../lib/client'

type Restaurant = { id: number, name: string, address?: string }

export default function RestaurantsPage() {
  const [restaurants, setRestaurants] = useState<Restaurant[]>([])
  useEffect(() => {
    client.get('/restaurants').then(r => setRestaurants(r.data))
  }, [])

  return (
    <Stack spacing={3}>
      <Typography variant="h4" fontWeight={800}>Restaurants</Typography>
      <Grid container spacing={2}>
        {restaurants.map(r => (
          <Grid item xs={12} sm={6} md={4} key={r.id}>
            <Card variant="outlined">
              <CardActionArea>
                <CardContent>
                  <Typography variant="h6" fontWeight={700}>{r.name}</Typography>
                  <Typography color="text.secondary">{r.address || '—'}</Typography>
                </CardContent>
              </CardActionArea>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Stack>
  )
}

