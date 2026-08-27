import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import ArticleCard from '../components/ArticleCard'
import { bookmarkApi } from '../api/userApi'

export default function BookmarksPage() {
  const [articles, setArticles] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  const load = () => {
    setLoading(true)
    setError(null)
    bookmarkApi
      .list({ size: 50 })
      .then((data) => setArticles(data.content.filter(Boolean)))
      .catch(() => setError('Could not load your saved articles.'))
      .finally(() => setLoading(false))
  }

  useEffect(load, [])

  const handleBookmarkChange = (articleId, saved) => {
    if (!saved) {
      setArticles((prev) => prev.filter((a) => a.id !== articleId))
    }
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 py-8">
      <h1 className="font-display text-3xl mb-6">Saved</h1>

      {error && <p className="text-alert text-sm mb-6">{error}</p>}

      {!loading && articles.length === 0 && !error && (
        <div className="text-center py-24">
          <p className="font-display text-2xl mb-2">No saved dispatches yet.</p>
          <p className="text-muted text-sm mb-6">Bookmark headlines as you read to build your reading list.</p>
          <Link to="/" className="btn-primary inline-flex">
            Browse headlines
          </Link>
        </div>
      )}

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
        {articles.map((article) => (
          <ArticleCard key={article.id} article={article} onBookmarkChange={handleBookmarkChange} />
        ))}
      </div>

      {loading && (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5">
          {Array.from({ length: 6 }).map((_, i) => (
            <div key={i} className="panel aspect-[4/5] animate-pulse surface" />
          ))}
        </div>
      )}
    </div>
  )
}
