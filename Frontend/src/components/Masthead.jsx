import { Link, NavLink, useNavigate } from 'react-router-dom'
import { useState, useRef, useEffect } from 'react'
import { useAuth } from '../context/AuthContext'
import { useTheme } from '../context/ThemeContext'

function WorldClock() {
  const [now, setNow] = useState(new Date())
  useEffect(() => {
    const t = setInterval(() => setNow(new Date()), 1000 * 30)
    return () => clearInterval(t)
  }, [])
  const utc = now.toUTCString().slice(17, 22)
  return <span className="dateline-muted hidden md:inline">UTC {utc}</span>
}

export default function Masthead() {
  const { user, isAuthenticated, logout } = useAuth()
  const { theme, toggleTheme } = useTheme()
  const [menuOpen, setMenuOpen] = useState(false)
  const menuRef = useRef(null)
  const navigate = useNavigate()

  useEffect(() => {
    const onClick = (e) => {
      if (menuRef.current && !menuRef.current.contains(e.target)) setMenuOpen(false)
    }
    document.addEventListener('mousedown', onClick)
    return () => document.removeEventListener('mousedown', onClick)
  }, [])

  const navLinkClass = ({ isActive }) =>
    `font-mono text-xs uppercase tracking-wire px-1 pb-1 border-b-2 transition ${
      isActive
        ? 'border-amber text-amber'
        : 'border-transparent text-muted hover:text-body'
    }`

  return (
    <header className="sticky top-0 z-40 border-b border-ink-border header-bar backdrop-blur">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 py-3 flex items-center justify-between gap-4">
        <Link to="/" className="flex items-center gap-3 shrink-0">
          <span className="h-2.5 w-2.5 rounded-full bg-amber animate-blink" />
          <span className="font-display text-2xl sm:text-3xl tracking-tight leading-none">
            NewsNow
          </span>
        </Link>

        <nav className="hidden md:flex items-center gap-6">
          <NavLink to="/" end className={navLinkClass}>
            Headlines
          </NavLink>
          <NavLink to="/search" className={navLinkClass}>
            Search
          </NavLink>
          {isAuthenticated && (
            <NavLink to="/bookmarks" className={navLinkClass}>
              Saved
            </NavLink>
          )}
        </nav>

        <div className="flex items-center gap-3">
          <WorldClock />

          <button
            onClick={toggleTheme}
            aria-label="Toggle dark/light mode"
            className="h-9 w-9 flex items-center justify-center rounded-sm border border-ink-border hover:border-amber transition"
          >
            {theme === 'dark' ? '☀' : '☾'}
          </button>

          {isAuthenticated ? (
            <div className="relative" ref={menuRef}>
              <button
                onClick={() => setMenuOpen((v) => !v)}
                className="h-9 w-9 rounded-full bg-amber text-ink-950 font-mono text-xs font-bold flex items-center justify-center"
              >
                {user?.fullName?.[0]?.toUpperCase() || 'U'}
              </button>
              {menuOpen && (
                <div className="absolute right-0 mt-2 w-48 panel shadow-xl py-1">
                  <div className="px-3 py-2 border-b border-ink-border">
                    <p className="text-sm truncate">{user?.fullName}</p>
                    <p className="dateline-muted truncate">{user?.email}</p>
                  </div>
                  <Link
                    to="/settings"
                    onClick={() => setMenuOpen(false)}
                    className="block px-3 py-2 text-sm hover-row"
                  >
                    Settings
                  </Link>
                  <button
                    onClick={() => {
                      logout()
                      setMenuOpen(false)
                      navigate('/')
                    }}
                    className="w-full text-left px-3 py-2 text-sm text-alert hover-row"
                  >
                    Sign out
                  </button>
                </div>
              )}
            </div>
          ) : (
            <Link to="/login" className="btn-ghost">
              Sign in
            </Link>
          )}
        </div>
      </div>

      <nav className="md:hidden flex items-center gap-5 px-4 pb-3 overflow-x-auto">
        <NavLink to="/" end className={navLinkClass}>
          Headlines
        </NavLink>
        <NavLink to="/search" className={navLinkClass}>
          Search
        </NavLink>
        {isAuthenticated && (
          <NavLink to="/bookmarks" className={navLinkClass}>
            Saved
          </NavLink>
        )}
      </nav>
    </header>
  )
}
