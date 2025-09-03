import React, { createContext, useContext, useMemo, useState } from 'react'
import client from '../lib/client'
import jwtDecode from 'jwt-decode'

type Decoded = { sub: string, role: string, exp: number }

type AuthContextType = {
  isAuthenticated: boolean
  userId?: string
  role?: string
  token?: string
  login: (email: string, password: string) => Promise<void>
  register: (email: string, password: string) => Promise<void>
  logout: () => void
}

const AuthContext = createContext<AuthContextType>({
  isAuthenticated: false,
  async login() {},
  async register() {},
  logout() {}
})

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [token, setToken] = useState<string | undefined>(() => localStorage.getItem('token') || undefined)
  const decoded: Decoded | undefined = useMemo(() => {
    if (!token) return undefined
    try { return jwtDecode(token) as Decoded } catch { return undefined }
  }, [token])

  const authedClient = useMemo(() => {
    client.interceptors.request.use(cfg => {
      if (token) cfg.headers.Authorization = `Bearer ${token}`
      return cfg
    })
    return client
  }, [token])

  async function login(email: string, password: string) {
    const res = await authedClient.post('/auth/login', { email, password })
    setToken(res.data.token)
    localStorage.setItem('token', res.data.token)
  }

  async function register(email: string, password: string) {
    const res = await authedClient.post('/auth/register', { email, password })
    setToken(res.data.token)
    localStorage.setItem('token', res.data.token)
  }

  function logout() {
    setToken(undefined)
    localStorage.removeItem('token')
  }

  const value: AuthContextType = {
    isAuthenticated: !!decoded && decoded.exp * 1000 > Date.now(),
    userId: decoded?.sub,
    role: decoded?.role,
    token,
    login,
    register,
    logout
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  return useContext(AuthContext)
}

