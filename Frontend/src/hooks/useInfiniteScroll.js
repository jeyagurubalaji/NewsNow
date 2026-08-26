import { useEffect, useRef } from 'react'

/**
 * Calls onIntersect() when the returned sentinel ref enters the viewport,
 * as long as hasMore is true and it's not already loading.
 */
export function useInfiniteScroll({ onIntersect, hasMore, loading, rootMargin = '400px' }) {
  const sentinelRef = useRef(null)

  useEffect(() => {
    const node = sentinelRef.current
    if (!node || !hasMore) return

    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && !loading) {
          onIntersect()
        }
      },
      { rootMargin }
    )

    observer.observe(node)
    return () => observer.disconnect()
  }, [onIntersect, hasMore, loading, rootMargin])

  return sentinelRef
}
