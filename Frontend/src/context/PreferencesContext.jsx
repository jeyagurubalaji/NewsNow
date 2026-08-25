import { createContext, useContext, useEffect, useState } from 'react'
import { useAuth } from './AuthContext'
import { userApi } from '../api/userApi'

const PreferencesContext = createContext(null)

const DEFAULTS = { country: 'us', category: 'top', language: '' }

export function PreferencesProvider({ children }) {
  const { user, isAuthenticated, updateUser } = useAuth()

  const [country, setCountryState] = useState(
    () => user?.preferredCountry || localStorage.getItem('newsnow_country') || DEFAULTS.country
  )
  const [category, setCategoryState] = useState(
    () => localStorage.getItem('newsnow_category') || DEFAULTS.category
  )
  const [language, setLanguageState] = useState(
    () => user?.preferredLanguage || localStorage.getItem('newsnow_language') || DEFAULTS.language
  )

  // Sync from user profile once authenticated (e.g. right after login)
  useEffect(() => {
    if (user?.preferredCountry) setCountryState(user.preferredCountry)
    if (user?.preferredLanguage) setLanguageState(user.preferredLanguage)
  }, [user?.preferredCountry, user?.preferredLanguage])

  const setCountry = (code) => {
    setCountryState(code)
    localStorage.setItem('newsnow_country', code)
    if (isAuthenticated) {
      userApi.updatePreferences({ preferredCountry: code }).then(() => updateUser({ preferredCountry: code }))
    }
  }

  const setCategory = (cat) => {
    setCategoryState(cat)
    localStorage.setItem('newsnow_category', cat)
  }

  const setLanguage = (lang) => {
    setLanguageState(lang)
    localStorage.setItem('newsnow_language', lang)
    if (isAuthenticated) {
      userApi.updatePreferences({ preferredLanguage: lang }).then(() => updateUser({ preferredLanguage: lang }))
    }
  }

  return (
    <PreferencesContext.Provider
      value={{ country, setCountry, category, setCategory, language, setLanguage }}
    >
      {children}
    </PreferencesContext.Provider>
  )
}

export function usePreferences() {
  const ctx = useContext(PreferencesContext)
  if (!ctx) throw new Error('usePreferences must be used within PreferencesProvider')
  return ctx
}
