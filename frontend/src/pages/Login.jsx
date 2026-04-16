import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import keycloak from '../keycloak';

export default function Login() {
  const navigate = useNavigate();

  useEffect(() => {
    if (keycloak.authenticated) {
      navigate('/');
    }
  }, [navigate]);

  const handleKeycloakLogin = async () => {
    console.log("Tentative de redirection Keycloak...");
    try {
      await keycloak.login({ redirectUri: 'http://localhost:3001/' });
    } catch (e) {
      console.error("Erreur critique sur keycloak.login()", e);
      alert("Erreur Keycloak. Vérifiez la console.");
    }
  };

  return (
    <div className="login-container">
      <div className="login-blob blob-1"></div>
      <div className="login-blob blob-2"></div>
      
      <div className="login-card">
        <div className="login-header">
          <div className="login-logo-icon">🎓</div>
          <h2>Campus MS</h2>
          <p>Connectez-vous à votre espace universitaire</p>
        </div>

        <div className="login-options" style={{ textAlign: 'center', marginTop: '30px' }}>
          <button type="button" onClick={handleKeycloakLogin} className="btn login-keycloak-btn" style={{ padding: '15px' }}>
            <span className="keycloak-icon">🔐</span> Connexion avec Keycloak (SSO)
          </button>
        </div>
      </div>
    </div>
  );
}
