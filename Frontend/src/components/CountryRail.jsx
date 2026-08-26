import { useEffect, useRef, useState } from 'react'
import { newsApi } from '../api/newsApi'
import { usePreferences } from '../context/PreferencesContext'

// Converts an ISO 3166-1 alpha-2 code to its flag emoji (regional indicator symbols)
function flagEmoji(code) {
  if (!code || code.length !== 2) return '🏳'
  const base = 0x1f1e6
  const chars = code
    .toUpperCase()
    .split('')
    .map((c) => String.fromCodePoint(base + (c.charCodeAt(0) - 65)))
  return chars.join('')
}

export default function CountryRail() {
  const [countries, setCountries] = useState({})
  const [query, setQuery] = useState('')
  const [open, setOpen] = useState(false)
  const { country, setCountry } = usePreferences()
  const wrapRef = useRef(null)

  useEffect(() => {
    newsApi.getCountries().then(setCountries).catch(() => setCountries({}))
  }, [])

  useEffect(() => {
    const onClick = (e) => {
      if (wrapRef.current && !wrapRef.current.contains(e.target)) setOpen(false)
    }
    document.addEventListener('mousedown', onClick)
    return () => document.removeEventListener('mousedown', onClick)
  }, [])

  const entries = Object.entries(countries)
  const filtered = query
    ? entries.filter(([code, name]) => name.toLowerCase().includes(query.toLowerCase()) || code.includes(query.toLowerCase()))
    : entries

  // Countries shown as quick-access chips before the "more" picker
  const quickCodes = ['us', 'gb', 'in', 'au', 'ca', 'de', 'fr', 'jp', 'br', 'ng']
  const quick = quickCodes.filter((c) => countries[c])

  return (
    <div className="border-b divider" ref={wrapRef}>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 py-2.5 flex items-center gap-2 overflow-x-auto">
        <span className="dateline-muted shrink-0 mr-1">Dateline</span>

        {quick.map((code) => (
          <button
            key={code}
            onClick={() => setCountry(code)}
            className={`shrink-0 flex items-center gap-1.5 px-2.5 py-1 rounded-full font-mono text-xs uppercase tracking-wire border transition ${
              country === code
                ? 'border-teal text-teal bg-teal/10'
                : 'border-ink-border text-muted hover:text-body hover:border-ink-700'
            }`}
          >
            <span aria-hidden>{flagEmoji(code)}</span>
            {code}
          </button>
        ))}

        <div className="relative shrink-0">
          <button
            onClick={() => setOpen((v) => !v)}
            className="flex items-center gap-1.5 px-2.5 py-1 rounded-full font-mono text-xs uppercase tracking-wire border border-ink-border text-muted hover:text-body hover:border-ink-700 transition"
          >
            {countries[country] && !quick.includes(country) ? (
              <>
                <span aria-hidden>{flagEmoji(country)}</span>
                {country}
              </>
            ) : (
              'All 195 →'
            )}
          </button>

          {open && (
            <div className="absolute left-0 mt-2 w-72 max-h-80 overflow-y-auto panel shadow-xl p-2 z-50">
              <input
                autoFocus
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                placeholder="Search country..."
                className="input-field mb-2 text-xs"
              />
              <ul>
                {filtered.map(([code, name]) => (
                  <li key={code}>
                    <button
                      onClick={() => {
                        setCountry(code)
                        setOpen(false)
                        setQuery('')
                      }}
                      className={`w-full flex items-center gap-2 px-2 py-1.5 rounded-sm text-sm text-left hover-row ${
                        country === code ? 'text-teal' : ''
                      }`}
                    >
                      <span aria-hidden>{flagEmoji(code)}</span>
                      <span className="truncate">{name}</span>
                      <span className="dateline-muted ml-auto">{code}</span>
                    </button>
                  </li>
                ))}
                {filtered.length === 0 && (
                  <li className="px-2 py-3 text-sm text-muted">No matches.</li>
                )}
              </ul>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
