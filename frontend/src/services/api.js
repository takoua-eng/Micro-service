import axios from 'axios';

import keycloak from '../keycloak';

const GATEWAY = process.env.REACT_APP_GATEWAY_URL || 'http://localhost:8089';

const api = axios.create({ baseURL: GATEWAY });

// Attach JWT token if present
api.interceptors.request.use(cfg => {
  if (keycloak && keycloak.token) {
    cfg.headers.Authorization = `Bearer ${keycloak.token}`;
  }
  return cfg;
});

// ─── MS User ───────────────────────────────────────────────────────────────
export const userService = {
  getAll:    ()       => api.get('/users'),
  getById:   (id)     => api.get(`/users/${id}`),
  create:    (data)   => api.post('/users/add', data),
  update:    (id, d)  => api.put(`/users/updateuser/${id}`, d),
  delete:    (id)     => api.delete(`/users/deleteuser/${id}`),
  getRoles:  ()       => api.get('/users/roles'),
};

// ─── MS Cours ──────────────────────────────────────────────────────────────
export const coursService = {
  getAll:          ()       => api.get('/api/cours'),
  getById:         (id)     => api.get(`/api/cours/${id}`),
  create:          (data)   => api.post('/api/cours', data),
  update:          (id, d)  => api.put(`/api/cours/${id}`, d),
  delete:          (id)     => api.delete(`/api/cours/${id}`),
  getEnseignants:  ()       => api.get('/api/cours/enseignants'),
  createEnseignant:(data)   => api.post('/api/cours/enseignants', data),
  deleteEnseignant:(id)     => api.delete(`/api/cours/enseignants/${id}`),
  getSupports:     (coursId)=> api.get(`/api/cours/${coursId}/supports`),
  uploadSupport:   (cId, f) => api.post(`/api/cours/${cId}/supports`, f, { headers: {'Content-Type':'multipart/form-data'}}),
};

// ─── MS Club ───────────────────────────────────────────────────────────────
export const clubService = {
  getAll:        ()       => api.get('/clubs'),
  getById:       (id)     => api.get(`/clubs/${id}`),
  create:        (data)   => api.post('/clubs', data),
  update:        (id, d)  => api.put(`/clubs/${id}`, d),
  delete:        (id)     => api.delete(`/clubs/${id}`),
  getEvenements: (clubId) => api.get(`/evenements/club/${clubId}`),
  addEvenement:  (cId, d) => api.post(`/evenements`, { ...d, club: { id: cId } }),
  deleteEvenement:(cId,eId)=> api.delete(`/evenements/${eId}`),
  getMembres:    (clubId) => api.get(`/membres/club/${clubId}`),
  addMembre:     (cId, d) => api.post(`/membres`, { ...d, club: { id: cId } }),
  deleteMembre:  (cId,mId)=> api.delete(`/membres/${mId}`),
};

// ─── MS Foyer ──────────────────────────────────────────────────────────────
export const foyerService = {
  getLogements:      ()       => api.get('/api/foyer/logements'),
  getLogement:       (id)     => api.get(`/api/foyer/logements/${id}`),
  createLogement:    (data)   => api.post('/api/foyer/logements', data),
  updateLogement:    (id, d)  => api.put(`/api/foyer/logements/${id}`, d),
  deleteLogement:    (id)     => api.delete(`/api/foyer/logements/${id}`),
  getChambres:       ()       => api.get('/api/foyer/chambres'),
  getChambre:        (id)     => api.get(`/api/foyer/chambres/${id}`),
  createChambre:     (data)   => api.post('/api/foyer/chambres', data),
  updateChambre:     (id, d)  => api.put(`/api/foyer/chambres/${id}`, d),
  deleteChambre:     (id)     => api.delete(`/api/foyer/chambres/${id}`),
  getReclamations:   ()       => api.get('/api/foyer/reclamations'),
  createReclamation: (data)   => api.post('/api/foyer/reclamations', data),
  updateReclamation: (id, d)  => api.put(`/api/foyer/reclamations/${id}`, d),
  deleteReclamation: (id)     => api.delete(`/api/foyer/reclamations/${id}`),
  getAttributions:   ()       => api.get('/api/foyer/attributions'),
  createAttribution: (data)   => api.post('/api/foyer/attributions', data),
  deleteAttribution: (id)     => api.delete(`/api/foyer/attributions/${id}`),
};

// ─── MS Bibliothèque ───────────────────────────────────────────────────────
export const bibliothequeService = {
  getLivres:      ()       => api.get('/api/bibliotheque/livres'),
  getLivre:       (id)     => api.get(`/api/bibliotheque/livres/${id}`),
  createLivre:    (data)   => api.post('/api/bibliotheque/livres', data),
  updateLivre:    (id, d)  => api.put(`/api/bibliotheque/livres/${id}`, d),
  deleteLivre:    (id)     => api.delete(`/api/bibliotheque/livres/${id}`),
  getEmprunts:    ()       => api.get('/api/bibliotheque/emprunts'),
  createEmprunt:  (data)   => api.post('/api/bibliotheque/emprunts', data),
  returnEmprunt:  (id)     => api.put(`/api/bibliotheque/emprunts/${id}/retour`),
  deleteEmprunt:  (id)     => api.delete(`/api/bibliotheque/emprunts/${id}`),
  getReservations:()       => api.get('/api/bibliotheque/reservations'),
  createReservation:(data) => api.post('/api/bibliotheque/reservations', data),
  deleteReservation:(id)   => api.delete(`/api/bibliotheque/reservations/${id}`),
  getPenalites:   ()       => api.get('/api/bibliotheque/penalites'),
  deletePenalite: (id)     => api.delete(`/api/bibliotheque/penalites/${id}`),
};

// ─── MS Restaurant ─────────────────────────────────────────────────────────
export const restaurantService = {
  getMenus:        ()       => api.get('/api/restaurant/menus'),
  getMenu:         (id)     => api.get(`/api/restaurant/menus/${id}`),
  createMenu:      (data)   => api.post('/api/restaurant/menus', data),
  updateMenu:      (id, d)  => api.put(`/api/restaurant/menus/${id}`, d),
  deleteMenu:      (id)     => api.delete(`/api/restaurant/menus/${id}`),
  getCommandes:    ()       => api.get('/api/restaurant/commandes'),
  createCommande:  (data)   => api.post('/api/restaurant/commandes', data),
  updateCommande:  (id, d)  => api.put(`/api/restaurant/commandes/${id}`, d),
  deleteCommande:  (id)     => api.delete(`/api/restaurant/commandes/${id}`),
  getTickets:      ()       => api.get('/api/restaurant/tickets'),
  createTicket:    (data)   => api.post('/api/restaurant/tickets', data),
  deleteTicket:    (id)     => api.delete(`/api/restaurant/tickets/${id}`),
  getHistorique:   ()       => api.get('/api/restaurant/historique'),
  getStatistiques: ()       => api.get('/api/restaurant/statistiques'),
};

export default api;
