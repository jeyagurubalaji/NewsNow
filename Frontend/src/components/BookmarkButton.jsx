import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { bookmarkApi } from '../api/userApi'
import { useAuth } from '../context/AuthContext'

export default function BookmarkButton({ articleId, initialBookmarked = false, onChange }) {
  const { isAuthenticated } = useAuth()
  const navigate = useNavigate()
  const [saved, setSaved] = useState(initialBookmarked)
  const [busy, setBusy] = useState(false)

  const toggle = async (e) => {
    e.preventDefault()
    e.stopPropagation()

    if (!isAuthenticated) {
      navigate('/login')
      return
    }
    if (busy) return

    setBusy(true)
    const next = !saved
    setSaved(next) // optimistic
    try {
      if (next) {
        await bookmarkApi.add(articleId)
      } else {
        await bookmarkApi.remove(articleId)
      }
      onChange?.(articleId, next)
    } catch {
      setSaved(!next) // revert on failure
    } finally {
      setBusy(false)
    }
  }

  return (
    <button
      onClick={toggle}
      disabled={busy}
      aria-pressed={saved}
      aria-label={saved ? 'Remove bookmark' : 'Save article'}
      className={`h-7 w-7 flex items-center justify-center rounded-sm border transition disabled:opacity-50 ${
        saved
          ? 'border-amber text-amber bg-amber/10'
          : 'border-ink-border text-muted hover:text-amber hover:border-amber'
      }`}
    >
      <svg width="13" height="13" viewBox="0 0 24 24" fill={saved ? 'currentColor' : 'none'} stroke="currentColor" strokeWidth="2">
        <path d="M6 3h12a1 1 0 0 1 1 1v17l-7-4-7 4V4a1 1 0 0 1 1-1Z" strokeLinejoin="round" />
      </svg>
    </button>
  )
}
