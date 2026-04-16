import Keycloak from 'keycloak-js';

const keycloakConfig = {
  url: 'http://localhost:9090',
  realm: 'University_realm',
  clientId: 'user-service'
};

const keycloak = new Keycloak(keycloakConfig);

export default keycloak;
