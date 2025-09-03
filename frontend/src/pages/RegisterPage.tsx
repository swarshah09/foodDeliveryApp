import { Alert, Box, Button, Card, CardContent, Stack, TextField, Typography } from '@mui/material'
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../contexts/AuthContext'

export default function RegisterPage() {
  const { register } = useAuth()
  const navigate = useNavigate()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState<string | undefined>()

  async function onSubmit(e: React.FormEvent) {
    e.preventDefault()
    setError(undefined)
    try {
      await register(email, password)
      navigate('/')
    } catch (err: any) {
      setError(err?.response?.data?.message || 'Registration failed')
    }
  }

  return (
    <Box maxWidth={420} mx="auto">
      <Card variant="outlined">
        <CardContent>
          <Stack spacing={2} component="form" onSubmit={onSubmit}>
            <Typography variant="h5" fontWeight={700}>Create your account</Typography>
            {error && <Alert severity="error">{error}</Alert>}
            <TextField label="Email" value={email} onChange={e => setEmail(e.target.value)} type="email" required fullWidth />
            <TextField label="Password" value={password} onChange={e => setPassword(e.target.value)} type="password" required fullWidth />
            <Button type="submit" variant="contained" size="large">Sign Up</Button>
          </Stack>
        </CardContent>
      </Card>
    </Box>
  )
}

