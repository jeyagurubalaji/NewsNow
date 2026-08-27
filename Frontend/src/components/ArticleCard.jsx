import { Link } from 'react-router-dom'
import BookmarkButton from './BookmarkButton'

function timeAgo(iso) {
  if (!iso) return ''
  const diffMs = Date.now() - new Date(iso).getTime()
  const mins = Math.floor(diffMs / 60000)
  if (mins < 1) return 'just now'
  if (mins < 60) return `${mins}m ago`
  const hours = Math.floor(mins / 60)
  if (hours < 24) return `${hours}h ago`
  const days = Math.floor(hours / 24)
  return `${days}d ago`
}

export default function ArticleCard({ article, onBookmarkChange }) {
  return (
    <article className="panel overflow-hidden flex flex-col group transition hover:border-teal/50">
      <Link to={`/article/${article.id}`} className="block">
        <div className="aspect-[16/9] surface overflow-hidden">
          {article.imageUrl ? (
            <img
              src={article.imageUrl}
              alt=""
              loading="lazy"
              className="w-full h-full object-cover transition duration-500 group-hover:scale-105"
              onError={(e) => {
                e.currentTarget.style.display = 'none'
              }}
            />
          ) : (
            <div className="w-full h-full flex items-center justify-center">
              <span className="font-display text-4xl text-ink-700">NN</span>
            </div>
          )}
        </div>
      </Link>

      <div className="p-4 flex flex-col gap-2 flex-1">
        <div className="flex items-center justify-between gap-2">
          <span className="dateline flex items-center gap-1.5">
            {article.breaking && (
              <span className="h-1.5 w-1.5 rounded-full bg-alert animate-blink" aria-hidden />
            )}
            {article.country?.toUpperCase()} · {article.category}
          </span>
          <span className="dateline-muted shrink-0">{timeAgo(article.publishedAt)}</span>
        </div>

        <Link to={`/article/${article.id}`}>
          <h3 className="font-display text-lg leading-snug line-clamp-3 text-body group-hover:text-teal transition">
            {article.title}
          </h3>
        </Link>

        {article.aiSummary ? (
          <p className="text-sm text-muted line-clamp-3">{article.aiSummary}</p>
        ) : article.description ? (
          <p className="text-sm text-muted line-clamp-3">{article.description}</p>
        ) : null}

        <div className="mt-auto pt-2 flex items-center justify-between">
          <span className="dateline-muted truncate">{article.sourceName || 'Wire report'}</span>
          <BookmarkButton
            articleId={article.id}
            initialBookmarked={article.bookmarked}
            onChange={onBookmarkChange}
          />
        </div>
      </div>
    </article>
  )
}
