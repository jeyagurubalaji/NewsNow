import CountryRail from '../components/CountryRail'
import CategoryTabs from '../components/CategoryTabs'
import ArticleCard from '../components/ArticleCard'
import { usePreferences } from '../context/PreferencesContext'
import { useHeadlines } from '../hooks/useHeadlines'
import { useInfiniteScroll } from '../hooks/useInfiniteScroll'

export default function HomePage() {
  const { country, category, language } = usePreferences()
  const { articles, setArticles, loading, error, hasMore, loadMore, refresh } = useHeadlines({
    country,
    category,
    language,
  })

  const sentinelRef = useInfiniteScroll({ onIntersect: loadMore, hasMore, loading })

  const handleBookmarkChange = (articleId, saved) => {
    setArticles((prev) => prev.map((a) => (a.id === articleId ? { ...a, bookmarked: saved } : a)))
  }

  return (
    <div>
      <CountryRail />
      <CategoryTabs />

      <div className="max-w-7xl mx-auto px-4 sm:px-6 pb-16">
        {error && (
          <div className="panel border-alert/40 p-4 mb-6 flex items-center justify-between">
            <p className="text-sm text-alert">{error}</p>
            <button onClick={refresh} className="btn-ghost text-alert border-alert/40">
              Retry
            </button>
          </div>
        )}

        {!error && articles.length === 0 && !loading && (
          <div className="text-center py-24">
            <p className="font-display text-2xl mb-2">No dispatches yet for this dateline.</p>
            <p className="text-muted text-sm">
              This country may not have been ingested yet — try again shortly, or pick another
              country above.
            </p>
          </div>
        )}

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5 pt-6">
          {articles.map((article) => (
            <ArticleCard key={article.id} article={article} onBookmarkChange={handleBookmarkChange} />
          ))}
        </div>

        {loading && (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5 pt-6">
            {Array.from({ length: 6 }).map((_, i) => (
              <div key={i} className="panel aspect-[4/5] animate-pulse surface" />
            ))}
          </div>
        )}

        <div ref={sentinelRef} className="h-1" />
      </div>
    </div>
  )
}
