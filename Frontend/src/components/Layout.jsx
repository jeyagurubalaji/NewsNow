import { Outlet } from 'react-router-dom'
import Masthead from './Masthead'
import BreakingTicker from './BreakingTicker'

export default function Layout() {
  return (
    <div className="min-h-screen flex flex-col">
      <Masthead />
      <BreakingTicker />
      <main className="flex-1">
        <Outlet />
      </main>
      <footer className="border-t divider py-6">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 flex flex-col sm:flex-row items-center justify-between gap-2">
          <span className="dateline-muted">NewsNow — 195 countries, wired in real time</span>
          <span className="dateline-muted">Headlines via newsdata.io</span>
        </div>
      </footer>
    </div>
  )
}
