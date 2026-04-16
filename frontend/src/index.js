import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import keycloak from './keycloak';

const root = ReactDOM.createRoot(document.getElementById('root'));

keycloak.init({ 
  onLoad: 'check-sso',
  checkLoginIframe: false
}).then((authenticated) => {
  root.render(
    <React.StrictMode>
      <App />
    </React.StrictMode>
  );
}).catch(console.error);
