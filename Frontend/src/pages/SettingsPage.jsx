import { useEffect, useState } from 'react'
import { useAuth } from '../context/AuthContext'
import { useTheme } from '../context/ThemeContext'
import { newsApi } from '../api/newsApi'
import { userApi } from '../api/userApi'

export default function SettingsPage() {
  const { user, updateUser } = useAuth()
  const { theme, toggleTheme } = useTheme()

  const [categories, setCategories] = useState([])
  const [favorites, setFavorites] = useState(user?.favoriteCategories || [])
  const [notifications, setNotifications] = useState(user?.notificationsEnabled ?? true)
  const [saving, setSaving] = useState(false)
  const [saved, setSaved] = useState(false)

  useEffect(() => {
    newsApi.getCategories().then(setCategories).catch(() => setCategories([]))
  }, [])

  const toggleFavorite = (cat) => {
    setFavorites((prev) => (prev.includes(cat) ? prev.filter((c) => c !== cat) : [...prev, cat]))
    setSaved(false)
  }

  const save = async () => {
    setSaving(true)
    try {
      const updated = await userApi.updatePreferences({
        favoriteCategories: favorites,
        notificationsEnabled: notifications,
      })
      updateUser(updated)
      setSaved(true)
    } finally {
      setSaving(false)
    }
  }

  return (
    <div className="max-w-xl mx-auto px-4 sm:px-6 py-10">
      <h1 className="font-display text-3xl mb-8">Settings</h1>

      <section className="mb-8">
        <h2 className="dateline mb-3">Appearance</h2>
        <div className="panel p-4 flex items-center justify-between">
          <div>
            <p className="text-sm text-body">Theme</p>
            <p className="dateline-muted mt-0.5">{theme === 'dark' ? 'Newsroom (dark)' : 'Broadsheet (light)'}</p>
          </div>
          <button onClick={toggleTheme} className="btn-ghost">
            Switch to {theme === 'dark' ? 'light' : 'dark'}
          </button>
        </div>
      </section>

      <section className="mb-8">
        <h2 className="dateline mb-3">Favorite categories</h2>
        <div className="panel p-4 flex flex-wrap gap-2">
          {categories.map((cat) => (
            <button
              key={cat}
              onClick={() => toggleFavorite(cat)}
              className={`px-3 py-1.5 rounded-full font-mono text-xs uppercase tracking-wire border transition ${
                favorites.includes(cat)
                  ? 'border-amber text-amber bg-amber/10'
                  : 'border-ink-border text-muted hover:text-body'
              }`}
            >
              {cat}
            </button>
          ))}
        </div>
      </section>

      <section className="mb-8">
        <h2 className="dateline mb-3">Notifications</h2>
        <div className="panel p-4 flex items-center justify-between">
          <div>
            <p className="text-sm text-body">Breaking news alerts</p>
            <p className="dateline-muted mt-0.5">Get notified when a story is flagged breaking</p>
          </div>
          <button
            onClick={() => {
              setNotifications((v) => !v)
              setSaved(false)
            }}
            aria-pressed={notifications}
            className={`h-6 w-11 rounded-full relative transition ${notifications ? 'bg-amber' : 'surface'}`}
          >
            <span
              className={`absolute top-0.5 h-5 w-5 rounded-full bg-white transition ${
                notifications ? 'left-5' : 'left-0.5'
              }`}
            />
          </button>
        </div>
      </section>

      <button onClick={save} disabled={saving} className="btn-primary">
        {saving ? 'Saving…' : saved ? 'Saved ✓' : 'Save changes'}
      </button>
    </div>
  )
}
