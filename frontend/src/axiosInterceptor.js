import axios from 'axios';

// Configure Axios with default gateway URL from env or fallback
axios.defaults.baseURL = process.env.REACT_APP_GATEWAY_URL || 'http://localhost:8089';

import keycloak from './keycloak';

// Add a request interceptor
axios.interceptors.request.use(
  (config) => {
    if (keycloak.token) {
      config.headers['Authorization'] = 'Bearer ' + keycloak.token;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default axios;
