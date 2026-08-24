import { apiClient } from './client'

export const bookmarkApi = {
  add: (articleId) => apiClient.post(`/bookmarks/${articleId}`).then((r) => r.data),
  remove: (articleId) => apiClient.delete(`/bookmarks/${articleId}`).then((r) => r.data),
  list: ({ page = 0, size = 20 } = {}) =>
    apiClient.get('/bookmarks', { params: { page, size } }).then((r) => r.data),
  count: () => apiClient.get('/bookmarks/count').then((r) => r.data),
}

export const userApi = {
  getProfile: () => apiClient.get('/users/me').then((r) => r.data),
  updatePreferences: (payload) =>
    apiClient.patch('/users/me/preferences', payload).then((r) => r.data),
}
