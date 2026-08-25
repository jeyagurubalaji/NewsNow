import { createContext, useContext, useEffect, useState, useCallback } from 'react'
import { authApi } from '../api/authApi'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const raw = localStorage.getItem('newsnow_user')
    return raw ? JSON.parse(raw) : null
  })
  const [loading, setLoading] = useState(false)

  const persistSession = (data) => {
    localStorage.setItem('newsnow_access_token', data.accessToken)
    localStorage.setItem('newsnow_refresh_token', data.refreshToken)
    localStorage.setItem('newsnow_user', JSON.stringify(data.user))
    setUser(data.user)
  }

  const login = useCallback(async (email, password) => {
    setLoading(true)
    try {
      const data = await authApi.login({ email, password })
      persistSession(data)
      return data.user
    } finally {
      setLoading(false)
    }
  }, [])

  const register = useCallback(async (payload) => {
    setLoading(true)
    try {
      const data = await authApi.register(payload)
      persistSession(data)
      return data.user
    } finally {
      setLoading(false)
    }
  }, [])

  const logout = useCallback(() => {
    localStorage.removeItem('newsnow_access_token')
    localStorage.removeItem('newsnow_refresh_token')
    localStorage.removeItem('newsnow_user')
    setUser(null)
  }, [])

  const updateUser = useCallback((partial) => {
    setUser((prev) => {
      const next = { ...prev, ...partial }
      localStorage.setItem('newsnow_user', JSON.stringify(next))
      return next
    })
  }, [])

  useEffect(() => {
    const onForcedLogout = () => setUser(null)
    window.addEventListener('newsnow:logout', onForcedLogout)
    return () => window.removeEventListener('newsnow:logout', onForcedLogout)
  }, [])

  return (
    <AuthContext.Provider
      value={{ user, isAuthenticated: !!user, loading, login, register, logout, updateUser }}
    >
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within AuthProvider')
  return ctx
}
