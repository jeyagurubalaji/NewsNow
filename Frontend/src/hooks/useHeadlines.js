import { useCallback, useEffect, useRef, useState } from 'react'
import { newsApi } from '../api/newsApi'

/**
 * Fetches paginated headlines for a country/category/language combination.
 * Resets automatically when any of those filters change.
 */
export function useHeadlines({ country, category, language }) {
  const [articles, setArticles] = useState([])
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(1)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  const requestId = useRef(0)

  const load = useCallback(
    async (pageToLoad, replace) => {
      const currentRequest = ++requestId.current
      setLoading(true)
      setError(null)
      try {
        const data = await newsApi.getHeadlines(country, {
          category: category === 'top' ? undefined : category,
          language: language || undefined,
          page: pageToLoad,
          size: 20,
        })
        if (currentRequest !== requestId.current) return // stale response, ignore
        setArticles((prev) => (replace ? data.content : [...prev, ...data.content]))
        setTotalPages(data.totalPages ?? 1)
        setPage(pageToLoad)
      } catch (e) {
        if (currentRequest !== requestId.current) return
        setError(e.response?.data?.message || 'Could not load headlines. Try again.')
      } finally {
        if (currentRequest === requestId.current) setLoading(false)
      }
    },
    [country, category, language]
  )

  useEffect(() => {
    if (!country) return
    load(0, true)
  }, [country, category, language]) // eslint-disable-line react-hooks/exhaustive-deps

  const loadMore = useCallback(() => {
    if (page + 1 < totalPages && !loading) load(page + 1, false)
  }, [page, totalPages, loading, load])

  const refresh = useCallback(() => load(0, true), [load])

  return {
    articles,
    setArticles,
    loading,
    error,
    hasMore: page + 1 < totalPages,
    loadMore,
    refresh,
  }
}
