import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function RegisterPage() {
  const { register } = useAuth()
  const navigate = useNavigate()

  const [fullName, setFullName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState(null)
  const [fieldErrors, setFieldErrors] = useState({})
  const [submitting, setSubmitting] = useState(false)

  const onSubmit = async (e) => {
    e.preventDefault()
    setError(null)
    setFieldErrors({})
    setSubmitting(true)
    try {
      await register({ fullName, email, password })
      navigate('/', { replace: true })
    } catch (err) {
      setError(err.response?.data?.message || 'Could not create your account.')
      setFieldErrors(err.response?.data?.fieldErrors || {})
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="max-w-sm mx-auto px-4 py-16">
      <h1 className="font-display text-3xl mb-1">Create account</h1>
      <p className="text-muted text-sm mb-8">Save headlines and personalize your dateline.</p>

      <form onSubmit={onSubmit} className="flex flex-col gap-4">
        {error && <p className="text-alert text-sm">{error}</p>}

        <div>
          <label className="dateline-muted block mb-1.5">Full name</label>
          <input
            required
            value={fullName}
            onChange={(e) => setFullName(e.target.value)}
            className="input-field"
            autoComplete="name"
          />
          {fieldErrors.fullName && <p className="text-alert text-xs mt-1">{fieldErrors.fullName}</p>}
        </div>

        <div>
          <label className="dateline-muted block mb-1.5">Email</label>
          <input
            type="email"
            required
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="input-field"
            autoComplete="email"
          />
          {fieldErrors.email && <p className="text-alert text-xs mt-1">{fieldErrors.email}</p>}
        </div>

        <div>
          <label className="dateline-muted block mb-1.5">Password</label>
          <input
            type="password"
            required
            minLength={8}
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="input-field"
            autoComplete="new-password"
          />
          {fieldErrors.password && <p className="text-alert text-xs mt-1">{fieldErrors.password}</p>}
          <p className="dateline-muted mt-1">At least 8 characters</p>
        </div>

        <button type="submit" disabled={submitting} className="btn-primary mt-2">
          {submitting ? 'Creating account…' : 'Create account'}
        </button>
      </form>

      <p className="text-muted text-sm mt-6 text-center">
        Already have an account?{' '}
        <Link to="/login" className="text-teal hover:underline">
          Sign in
        </Link>
      </p>
    </div>
  )
}
