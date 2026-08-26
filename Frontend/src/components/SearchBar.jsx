import { useState } from 'react'
import { useNavigate } from 'react-router-dom'

export default function SearchBar({ initialValue = '', autoFocus = false }) {
  const [value, setValue] = useState(initialValue)
  const navigate = useNavigate()

  const submit = (e) => {
    e.preventDefault()
    const q = value.trim()
    if (!q) return
    navigate(`/search?q=${encodeURIComponent(q)}`)
  }

  return (
    <form onSubmit={submit} className="flex gap-2">
      <input
        autoFocus={autoFocus}
        value={value}
        onChange={(e) => setValue(e.target.value)}
        placeholder="Search worldwide headlines..."
        className="input-field"
        type="search"
      />
      <button type="submit" className="btn-primary shrink-0">
        Search
      </button>
    </form>
  )
}
