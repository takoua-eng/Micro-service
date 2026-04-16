import React from 'react';
import { NavLink, useLocation } from 'react-router-dom';

const navItems = [
  {
    section: 'Vue d\'ensemble',
    links: [
      { to: '/', label: 'Dashboard', icon: '⬡' },
    ],
  },
  {
    section: 'Microservices',
    links: [
      { to: '/users', label: 'MS User', icon: '👤' },
      { to: '/cours', label: 'MS Cours', icon: '📚' },
      { to: '/club', label: 'MS Club', icon: '🎯' },
      { to: '/foyer', label: 'MS Foyer', icon: '🏠' },
      { to: '/bibliotheque', label: 'MS Bibliothèque', icon: '📖' },
      { to: '/restaurant', label: 'MS Restaurant', icon: '🍽️' },
    ],
  },
];

export default function Sidebar() {
  const location = useLocation();

  return (
    <aside className="sidebar">
      <div className="sidebar-logo">
        <h2>Campus MS</h2>
        <p>Microservices Dashboard</p>
      </div>
      <nav className="sidebar-nav">
        {navItems.map(section => (
          <div className="nav-section" key={section.section}>
            <div className="nav-section-label">{section.section}</div>
            {section.links.map(link => (
              <NavLink
                key={link.to}
                to={link.to}
                end={link.to === '/'}
                className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}
              >
                <span className="nav-icon">{link.icon}</span>
                {link.label}
              </NavLink>
            ))}
          </div>
        ))}
      </nav>
      <div style={{ padding: '16px 20px', borderTop: '1px solid var(--border)' }}>
        <div style={{ fontSize: '0.72rem', color: 'var(--text3)' }}>
          API Gateway: <span style={{ color: 'var(--green)' }}>●</span> localhost:8080
        </div>
      </div>
    </aside>
  );
}
