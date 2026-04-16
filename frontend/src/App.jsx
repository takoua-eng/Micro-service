import React from 'react';
import { BrowserRouter, Routes, Route, useLocation, Navigate } from 'react-router-dom';
import './axiosInterceptor';
import Sidebar from './components/Sidebar';
import Dashboard from './pages/Dashboard';
import UsersPage from './pages/users/UsersPage';
import CoursPage from './pages/cours/CoursPage';
import ClubPage from './pages/club/ClubPage';
import FoyerPage from './pages/foyer/FoyerPage';
import BibliothequePage from './pages/bibliotheque/BibliothequePage';
import RestaurantPage from './pages/restaurant/RestaurantPage';
import Login from './pages/Login';

const titles = {
  '/': 'Dashboard',
  '/users': 'MS User',
  '/cours': 'MS Cours',
  '/club': 'MS Club',
  '/foyer': 'MS Foyer',
  '/bibliotheque': 'MS Bibliothèque',
  '/restaurant': 'MS Restaurant',
};

function Layout({ children }) {
  const location = useLocation();
  const isLoginPage = location.pathname === '/login';

  if (isLoginPage) {
    return <>{children}</>;
  }

  return (
    <div className="layout">
      <Sidebar />
      <main className="main">
        {children}
      </main>
    </div>
  );
}

import keycloak from './keycloak';

function ProtectedRoute({ children }) {
  if (!keycloak.authenticated) {
    return <Navigate to="/login" replace />;
  }
  return children;
}

export default function App() {
  return (
    <BrowserRouter>
      <Layout>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
          <Route path="/users" element={<ProtectedRoute><UsersPage /></ProtectedRoute>} />
          <Route path="/cours" element={<ProtectedRoute><CoursPage /></ProtectedRoute>} />
          <Route path="/club" element={<ProtectedRoute><ClubPage /></ProtectedRoute>} />
          <Route path="/foyer" element={<ProtectedRoute><FoyerPage /></ProtectedRoute>} />
          <Route path="/bibliotheque" element={<ProtectedRoute><BibliothequePage /></ProtectedRoute>} />
          <Route path="/restaurant" element={<ProtectedRoute><RestaurantPage /></ProtectedRoute>} />
        </Routes>
      </Layout>
    </BrowserRouter>
  );
}
