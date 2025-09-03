import { Box, Button, Stack, Typography } from '@mui/material'
import { useNavigate } from 'react-router-dom'

export default function HomePage() {
  const navigate = useNavigate()
  return (
    <Stack alignItems="center" spacing={3} textAlign="center" sx={{ py: 8 }}>
      <Typography variant="h2" fontWeight={800}>
        Delicious food, delivered fast
      </Typography>
      <Typography variant="h6" color="text.secondary">
        Browse top restaurants near you and track your orders in real time.
      </Typography>
      <Box>
        <Button size="large" variant="contained" onClick={() => navigate('/restaurants')} sx={{ mr: 2 }}>Explore Restaurants</Button>
        <Button size="large" variant="outlined" onClick={() => navigate('/orders')}>View Orders</Button>
      </Box>
    </Stack>
  )
}

