import { useEffect, useState } from 'react'
import { newsApi } from '../api/newsApi'
import { usePreferences } from '../context/PreferencesContext'

export default function CategoryTabs() {
  const [categories, setCategories] = useState([])
  const { category, setCategory } = usePreferences()

  useEffect(() => {
    newsApi.getCategories().then(setCategories).catch(() => setCategories([]))
  }, [])

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 py-3 flex items-center gap-5 overflow-x-auto">
      {categories.map((cat) => (
        <button
          key={cat}
          onClick={() => setCategory(cat)}
          className={`shrink-0 font-mono text-xs uppercase tracking-wire pb-1 border-b-2 transition ${
            category === cat
              ? 'border-amber text-amber'
              : 'border-transparent text-muted hover:text-body'
          }`}
        >
          {cat}
        </button>
      ))}
    </div>
  )
}
