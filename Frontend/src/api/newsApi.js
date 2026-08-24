import { apiClient } from './client'

export const newsApi = {
  getCountries: () => apiClient.get('/news/countries').then((r) => r.data),
  getCategories: () => apiClient.get('/news/categories').then((r) => r.data),
  getLanguages: () => apiClient.get('/news/languages').then((r) => r.data),

  getHeadlines: (country, { category, language, page = 0, size = 20 } = {}) =>
    apiClient
      .get(`/news/headlines/${country}`, { params: { category, language, page, size } })
      .then((r) => r.data),

  search: (q, { country, page = 0, size = 20 } = {}) =>
    apiClient.get('/news/search', { params: { q, country, page, size } }).then((r) => r.data),

  getBreaking: () => apiClient.get('/news/breaking').then((r) => r.data),

  getArticle: (id) => apiClient.get(`/news/article/${id}`).then((r) => r.data),
}
