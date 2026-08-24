import { apiClient } from './client'

export const authApi = {
  register: (payload) => apiClient.post('/auth/register', payload).then((r) => r.data),
  login: (payload) => apiClient.post('/auth/login', payload).then((r) => r.data),
  googleLogin: (idToken) => apiClient.post('/auth/google', { idToken }).then((r) => r.data),
  refresh: (refreshToken) => apiClient.post('/auth/refresh', { refreshToken }).then((r) => r.data),
}
