import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { newsApi } from '../api/newsApi'

export default function BreakingTicker() {
  const [items, setItems] = useState([])

  useEffect(() => {
    let mounted = true
    newsApi
      .getBreaking()
      .then((data) => mounted && setItems(data))
      .catch(() => mounted && setItems([]))
    const interval = setInterval(() => {
      newsApi.getBreaking().then((data) => mounted && setItems(data)).catch(() => {})
    }, 5 * 60 * 1000)
    return () => {
      mounted = false
      clearInterval(interval)
    }
  }, [])

  if (items.length === 0) return null

  const doubled = [...items, ...items]

  return (
    <div className="w-full bg-alert text-white overflow-hidden border-b border-black/20">
      <div className="flex items-center">
        <div className="shrink-0 flex items-center gap-2 bg-alert-dim px-3 py-2 z-10">
          <span className="h-2 w-2 rounded-full bg-white animate-blink" />
          <span className="font-mono text-[11px] uppercase tracking-wire font-semibold">
            Wire
          </span>
        </div>
        <div className="flex-1 overflow-hidden py-2">
          <div className="flex whitespace-nowrap animate-ticker">
            {doubled.map((item, i) => (
              <Link
                key={`${item.id}-${i}`}
                to={`/article/${item.id}`}
                className="mx-6 font-mono text-[12px] uppercase tracking-wire hover:underline"
              >
                ▸ {item.country?.toUpperCase()} · {item.title}
              </Link>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}
