import React from 'react';
import { Link } from 'react-router-dom';

const microservices = [
  {
    to: '/users',
    icon: '👤',
    title: 'MS User',
    desc: 'Gestion des comptes, profils utilisateurs, rôles et permissions.',
    db: 'H2',
    tech: 'Spring Boot',
    features: ['Comptes', 'Profils', 'Rôles'],
    color: 'blue',
  },
  {
    to: '/cours',
    icon: '📚',
    title: 'MS Cours',
    desc: 'Cours, enseignants, supports pédagogiques et crédits académiques.',
    db: 'MySQL',
    tech: 'Spring Boot',
    features: ['Cours', 'Enseignants', 'Supports'],
    color: 'purple',
  },
  {
    to: '/club',
    icon: '🎯',
    title: 'MS Club',
    desc: 'Clubs étudiants, événements, membres et réunions.',
    db: 'H2',
    tech: 'Spring Boot',
    features: ['Clubs', 'Événements', 'Membres'],
    color: 'green',
  },
  {
    to: '/foyer',
    icon: '🏠',
    title: 'MS Foyer',
    desc: 'Logements, chambres, réclamations et attribution des lits.',
    db: 'MySQL',
    tech: 'Spring Boot',
    features: ['Logements', 'Chambres', 'Attribution'],
    color: 'orange',
  },
  {
    to: '/bibliotheque',
    icon: '📖',
    title: 'MS Bibliothèque',
    desc: 'Livres, emprunts, réservations et pénalités.',
    db: 'H2',
    tech: 'Spring Boot',
    features: ['Livres', 'Emprunts', 'Réservations'],
    color: 'pink',
  },
  {
    to: '/restaurant',
    icon: '🍽️',
    title: 'MS Restaurant',
    desc: 'Menus, commandes, tickets repas et statistiques.',
    db: 'MongoDB',
    tech: 'NestJS',
    features: ['Menus', 'Commandes', 'Tickets'],
    color: 'yellow',
  },
];

const colorMap = {
  blue: 'var(--accent)',
  purple: 'var(--accent2)',
  green: 'var(--green)',
  orange: 'var(--orange)',
  pink: 'var(--accent3)',
  yellow: 'var(--yellow)',
};

export default function Dashboard() {
  return (
    <div className="content">
      <div className="welcome-card">
        <div>
          <h2>Bienvenue sur Campus MS 🎓</h2>
          <p>Tableau de bord central pour gérer tous vos microservices universitaires.</p>
        </div>
        <div style={{ fontSize: '3rem' }}>🏫</div>
      </div>

      <div className="stats-grid">
        <div className="stat-card blue">
          <div className="stat-label">Microservices</div>
          <div className="stat-value">6</div>
          <div className="stat-sub">Actifs via API Gateway</div>
        </div>
        <div className="stat-card green">
          <div className="stat-label">Bases de données</div>
          <div className="stat-value">6</div>
          <div className="stat-sub">H2 · MySQL · MongoDB</div>
        </div>
        <div className="stat-card purple">
          <div className="stat-label">Frameworks</div>
          <div className="stat-value">2</div>
          <div className="stat-sub">Spring Boot · NestJS</div>
        </div>
        <div className="stat-card orange">
          <div className="stat-label">Auth</div>
          <div className="stat-value">JWT</div>
          <div className="stat-sub">Via Keycloak</div>
        </div>
      </div>

      <div style={{ marginBottom: 16 }}>
        <h2 style={{ fontFamily: 'Syne', fontSize: '1rem', fontWeight: 700, marginBottom: 16 }}>
          Microservices disponibles
        </h2>
      </div>

      <div className="ms-grid">
        {microservices.map(ms => (
          <Link key={ms.to} to={ms.to} className="ms-card" style={{ borderTop: `3px solid ${colorMap[ms.color]}` }}>
            <div className="ms-card-icon">{ms.icon}</div>
            <h3>{ms.title}</h3>
            <p>{ms.desc}</p>
            <div className="ms-card-footer">
              <span className="badge badge-blue">{ms.db}</span>
              <span className="badge badge-purple">{ms.tech}</span>
              {ms.features.map(f => (
                <span key={f} className="badge" style={{ background: 'var(--surface2)', color: 'var(--text2)' }}>{f}</span>
              ))}
            </div>
          </Link>
        ))}
      </div>

      <div className="card" style={{ marginTop: 24 }}>
        <div className="card-header">
          <span className="card-title">🏗️ Architecture</span>
        </div>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))', gap: 12 }}>
          {[
            { label: 'Frontend', value: 'React JS', icon: '⚛️' },
            { label: 'Gateway', value: 'API Gateway :8080', icon: '🔀' },
            { label: 'Auth', value: 'Keycloak', icon: '🔐' },
            { label: 'Config', value: 'Spring Config Server', icon: '⚙️' },
            { label: 'Container', value: 'Docker', icon: '🐳' },
          ].map(item => (
            <div key={item.label} style={{ background: 'var(--surface2)', border: '1px solid var(--border)', borderRadius: 10, padding: '14px 16px' }}>
              <div style={{ fontSize: '1.2rem', marginBottom: 6 }}>{item.icon}</div>
              <div style={{ fontSize: '0.72rem', color: 'var(--text3)', textTransform: 'uppercase', letterSpacing: '0.06em', marginBottom: 3 }}>{item.label}</div>
              <div style={{ fontFamily: 'Syne', fontWeight: 700, fontSize: '0.9rem' }}>{item.value}</div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
