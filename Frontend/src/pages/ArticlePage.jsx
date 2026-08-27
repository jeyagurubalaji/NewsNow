import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { newsApi } from '../api/newsApi'
import BookmarkButton from '../components/BookmarkButton'

export default function ArticlePage() {
  const { id } = useParams()
  const [article, setArticle] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    let cancelled = false
    setLoading(true)
    setError(null)
    newsApi
      .getArticle(id)
      .then((data) => !cancelled && setArticle(data))
      .catch(() => !cancelled && setError('This dispatch could not be found.'))
      .finally(() => !cancelled && setLoading(false))
    return () => {
      cancelled = true
    }
  }, [id])

  if (loading) {
    return <div className="max-w-3xl mx-auto px-4 sm:px-6 py-16 animate-pulse text-muted">Loading dispatch…</div>
  }

  if (error || !article) {
    return (
      <div className="max-w-3xl mx-auto px-4 sm:px-6 py-16 text-center">
        <p className="font-display text-2xl mb-2">{error}</p>
        <Link to="/" className="btn-ghost inline-flex mt-4">
          Back to headlines
        </Link>
      </div>
    )
  }

  return (
    <article className="max-w-3xl mx-auto px-4 sm:px-6 py-10">
      <div className="flex items-center justify-between mb-4">
        <span className="dateline flex items-center gap-1.5">
          {article.breaking && <span className="h-1.5 w-1.5 rounded-full bg-alert animate-blink" aria-hidden />}
          {article.country?.toUpperCase()} · {article.category} · {article.sourceName || 'Wire report'}
        </span>
        <BookmarkButton articleId={article.id} initialBookmarked={article.bookmarked} />
      </div>

      <h1 className="font-display text-3xl sm:text-4xl leading-tight mb-4">{article.title}</h1>

      {article.imageUrl && (
        <div className="aspect-[16/9] surface rounded-md overflow-hidden mb-6">
          <img src={article.imageUrl} alt="" className="w-full h-full object-cover" />
        </div>
      )}

      {article.aiSummary && (
        <div className="panel p-4 mb-6 border-teal/30">
          <p className="dateline text-teal mb-1">AI Summary</p>
          <p className="text-sm text-body">{article.aiSummary}</p>
        </div>
      )}

      {article.description && <p className="text-lg text-body mb-4 leading-relaxed">{article.description}</p>}

      <a
        href={article.url}
        target="_blank"
        rel="noopener noreferrer"
        className="btn-primary inline-flex"
      >
        Read full story at source →
      </a>
    </article>
  )
}
