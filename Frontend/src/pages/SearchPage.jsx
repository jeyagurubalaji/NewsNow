import { useEffect, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import SearchBar from '../components/SearchBar'
import ArticleCard from '../components/ArticleCard'
import { newsApi } from '../api/newsApi'
import { usePreferences } from '../context/PreferencesContext'

export default function SearchPage() {
  const [params] = useSearchParams()
  const q = params.get('q') || ''
  const { country } = usePreferences()
  const [scopeToCountry, setScopeToCountry] = useState(false)

  const [articles, setArticles] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  useEffect(() => {
    if (!q) {
      setArticles([])
      return
    }
    let cancelled = false
    setLoading(true)
    setError(null)
    newsApi
      .search(q, { country: scopeToCountry ? country : undefined, size: 30 })
      .then((data) => !cancelled && setArticles(data.content))
      .catch((e) => !cancelled && setError(e.response?.data?.message || 'Search failed. Try again.'))
      .finally(() => !cancelled && setLoading(false))
    return () => {
      cancelled = true
    }
  }, [q, scopeToCountry, country])

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 py-8">
      <div className="max-w-xl mb-6">
        <SearchBar initialValue={q} autoFocus />
      </div>

      {q && (
        <div className="flex items-center justify-between mb-6">
          <h1 className="font-display text-2xl">
            Results for <span className="text-teal">&ldquo;{q}&rdquo;</span>
          </h1>
          <label className="flex items-center gap-2 dateline-muted cursor-pointer">
            <input
              type="checkbox"
              checked={scopeToCountry}
              onChange={(e) => setScopeToCountry(e.target.checked)}
            />
            Limit to {country?.toUpperCase()}
          </label>
        </div>
      )}

      {error && <p className="text-alert text-sm mb-6">{error}</p>}

      {!q && (
        <p className="text-muted text-center py-24">Enter a keyword to search headlines worldwide.</p>
      )}

      {q && !loading && articles.length === 0 && !error && (
        <p className="text-muted text-center py-24">No results found for &ldquo;{q}&rdquo;.</p>
      )}

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
        {articles.map((article) => (
          <ArticleCard key={article.id} article={article} />
        ))}
      </div>

      {loading && (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5 mt-2">
          {Array.from({ length: 6 }).map((_, i) => (
            <div key={i} className="panel aspect-[4/5] animate-pulse surface" />
          ))}
        </div>
      )}
    </div>
  )
}
