import { Link } from 'react-router-dom'

export default function NotFoundPage() {
  return (
    <div className="max-w-lg mx-auto px-4 py-24 text-center">
      <p className="dateline mb-2">404 · Wire Room</p>
      <h1 className="font-display text-3xl mb-3">This dispatch doesn't exist.</h1>
      <p className="text-muted text-sm mb-6">The page you're looking for was never filed, or has since been retracted.</p>
      <Link to="/" className="btn-primary inline-flex">
        Back to headlines
      </Link>
    </div>
  )
}
