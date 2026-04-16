import React, { useState, useEffect } from 'react';
import CrudTable from '../../components/CrudTable';
import Modal from '../../components/Modal';
import { restaurantService } from '../../services/api';

export default function RestaurantPage() {
  const [tab, setTab] = useState('menus');
  const [menus, setMenus] = useState([]);
  const [commandes, setCommandes] = useState([]);
  const [tickets, setTickets] = useState([]);
  const [historique, setHistorique] = useState([]);
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [modal, setModal] = useState(null);
  const [form, setForm] = useState({});
  const [editId, setEditId] = useState(null);
  const [error, setError] = useState('');

  const load = async () => {
    setLoading(true);
    try {
      const [m, c, t, h] = await Promise.all([
        restaurantService.getMenus(), restaurantService.getCommandes(),
        restaurantService.getTickets(), restaurantService.getHistorique(),
      ]);
      setMenus(m.data); setCommandes(c.data); setTickets(t.data); setHistorique(h.data);
      try { const s = await restaurantService.getStatistiques(); setStats(s.data); } catch {}
    } catch { }
    finally { setLoading(false); }
  };
  useEffect(() => { load(); }, []);

  const set = k => e => setForm(f => ({ ...f, [k]: e.target.value }));
  const close = () => { setModal(null); setError(''); };

  const saveMenu = async () => {
    try {
      editId ? await restaurantService.updateMenu(editId, form) : await restaurantService.createMenu(form);
      close(); load();
    } catch (e) { setError(e.response?.data?.message || 'Erreur'); }
  };

  const saveCommande = async () => {
    try {
      editId ? await restaurantService.updateCommande(editId, form) : await restaurantService.createCommande(form);
      close(); load();
    } catch (e) { setError(e.response?.data?.message || 'Erreur'); }
  };

  const saveTicket = async () => {
    try { await restaurantService.createTicket(form); close(); load(); }
    catch (e) { setError(e.response?.data?.message || 'Erreur'); }
  };

  const menuColumns = [
    { key: 'nom', label: 'Nom', accessor: 'nom' },
    { key: 'type', label: 'Type', render: r => <span className="badge badge-orange">{r.type || r.categorie || '—'}</span> },
    { key: 'prix', label: 'Prix', render: r => <span className="badge badge-green">{r.prix} DT</span> },
    { key: 'date', label: 'Date', accessor: 'date' },
    { key: 'dispo', label: 'Disponible', render: r => <span className={`badge ${r.disponible !== false ? 'badge-green' : 'badge-pink'}`}>{r.disponible !== false ? 'Oui' : 'Non'}</span> },
  ];

  const commandeColumns = [
    { key: 'etudiantId', label: 'Étudiant', accessor: 'etudiantId' },
    { key: 'menuId', label: 'Menu', render: r => { const m = menus.find(mn => mn.id === r.menuId); return m?.nom || r.menuId || '—'; } },
    { key: 'date', label: 'Date', accessor: 'dateCommande' },
    { key: 'statut', label: 'Statut', render: r => (
      <span className={`badge ${r.statut === 'LIVRE' ? 'badge-green' : r.statut === 'EN_PREPARATION' ? 'badge-orange' : 'badge-blue'}`}>{r.statut || 'EN_ATTENTE'}</span>
    )},
    { key: 'total', label: 'Total', render: r => r.montantTotal ? `${r.montantTotal} DT` : '—' },
  ];

  const ticketColumns = [
    { key: 'etudiantId', label: 'Étudiant', accessor: 'etudiantId' },
    { key: 'type', label: 'Type', render: r => <span className="badge badge-purple">{r.type || '—'}</span> },
    { key: 'date', label: 'Date', accessor: 'date' },
    { key: 'utilise', label: 'Utilisé', render: r => <span className={`badge ${r.utilise ? 'badge-green' : 'badge-blue'}`}>{r.utilise ? 'Oui' : 'Non'}</span> },
  ];

  const histColumns = [
    { key: 'etudiantId', label: 'Étudiant', accessor: 'etudiantId' },
    { key: 'menu', label: 'Menu', accessor: 'menuNom' },
    { key: 'date', label: 'Date', accessor: 'date' },
    { key: 'montant', label: 'Montant', render: r => r.montant ? `${r.montant} DT` : '—' },
  ];

  return (
    <div className="content">
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 24 }}>
        <div>
          <h2 style={{ fontFamily: 'Syne', fontWeight: 800 }}>🍽️ MS Restaurant</h2>
          <p style={{ color: 'var(--text2)', fontSize: '0.875rem', marginTop: 4 }}>Menus, commandes, tickets repas et statistiques · MongoDB · NestJS</p>
        </div>
        <div style={{ display: 'flex', gap: 8 }}>
          {tab === 'menus' && <button className="btn btn-primary" onClick={() => { setForm({}); setEditId(null); setModal('menu'); }}>+ Nouveau menu</button>}
          {tab === 'commandes' && <button className="btn btn-primary" onClick={() => { setForm({}); setEditId(null); setModal('cmd'); }}>+ Commande</button>}
          {tab === 'tickets' && <button className="btn btn-primary" onClick={() => { setForm({}); setModal('tkt'); }}>+ Ticket</button>}
        </div>
      </div>

      {stats && (
        <div className="stats-grid" style={{ marginBottom: 24 }}>
          <div className="stat-card orange"><div className="stat-label">Commandes / jour</div><div className="stat-value">{stats.commandesAujourdhui ?? '—'}</div></div>
          <div className="stat-card green"><div className="stat-label">Recette / jour</div><div className="stat-value">{stats.recetteAujourdhui ?? '—'} DT</div></div>
          <div className="stat-card blue"><div className="stat-label">Tickets actifs</div><div className="stat-value">{tickets.filter(t => !t.utilise).length}</div></div>
          <div className="stat-card purple"><div className="stat-label">Menu populaire</div><div className="stat-value" style={{ fontSize: '1rem' }}>{stats.menuPopulaire ?? '—'}</div></div>
        </div>
      )}

      <div className="tabs">
        {['menus','commandes','tickets','historique'].map(t => (
          <button key={t} className={`tab ${tab === t ? 'active' : ''}`} onClick={() => setTab(t)}>
            {{'menus':'🍴','commandes':'📋','tickets':'🎟️','historique':'📜'}[t]} {t.charAt(0).toUpperCase() + t.slice(1)}
          </button>
        ))}
      </div>

      <div className="card">
        {tab === 'menus' && <CrudTable columns={menuColumns} data={menus} loading={loading}
          onEdit={r => { setForm(r); setEditId(r.id || r._id); setModal('menuEdit'); }}
          onDelete={async r => { await restaurantService.deleteMenu(r.id || r._id); load(); }} emptyIcon="🍴" emptyText="Aucun menu" />}
        {tab === 'commandes' && <CrudTable columns={commandeColumns} data={commandes} loading={loading}
          onEdit={r => { setForm(r); setEditId(r.id || r._id); setModal('cmdEdit'); }}
          onDelete={async r => { await restaurantService.deleteCommande(r.id || r._id); load(); }} emptyIcon="📋" emptyText="Aucune commande" />}
        {tab === 'tickets' && <CrudTable columns={ticketColumns} data={tickets} loading={loading}
          onDelete={async r => { await restaurantService.deleteTicket(r.id || r._id); load(); }} emptyIcon="🎟️" emptyText="Aucun ticket" />}
        {tab === 'historique' && <CrudTable columns={histColumns} data={historique} loading={loading} emptyIcon="📜" emptyText="Aucun historique" />}
      </div>

      {(modal === 'menu' || modal === 'menuEdit') && (
        <Modal title={modal === 'menu' ? 'Nouveau menu' : 'Modifier menu'} onClose={close}>
          {error && <div className="alert alert-error">⚠ {error}</div>}
          <div className="form-group"><label className="form-label">Nom</label><input className="form-input" value={form.nom || ''} onChange={set('nom')} /></div>
          <div className="form-row">
            <div className="form-group"><label className="form-label">Prix (DT)</label><input className="form-input" type="number" step="0.5" value={form.prix || ''} onChange={set('prix')} /></div>
            <div className="form-group"><label className="form-label">Type</label><select className="form-input" value={form.type || ''} onChange={set('type')}><option value="">—</option><option value="DEJEUNER">Déjeuner</option><option value="DINER">Dîner</option><option value="PETIT_DEJEUNER">Petit déjeuner</option></select></div>
          </div>
          <div className="form-group"><label className="form-label">Description</label><textarea className="form-input" rows={2} value={form.description || ''} onChange={set('description')} /></div>
          <div className="form-group"><label className="form-label">Date</label><input className="form-input" type="date" value={form.date || ''} onChange={set('date')} /></div>
          <div className="form-actions"><button className="btn btn-ghost" onClick={close}>Annuler</button><button className="btn btn-primary" onClick={saveMenu}>Enregistrer</button></div>
        </Modal>
      )}

      {(modal === 'cmd' || modal === 'cmdEdit') && (
        <Modal title={modal === 'cmd' ? 'Nouvelle commande' : 'Modifier commande'} onClose={close}>
          {error && <div className="alert alert-error">⚠ {error}</div>}
          <div className="form-group"><label className="form-label">Étudiant ID</label><input className="form-input" value={form.etudiantId || ''} onChange={set('etudiantId')} /></div>
          <div className="form-group"><label className="form-label">Menu</label><select className="form-input" value={form.menuId || ''} onChange={set('menuId')}><option value="">—</option>{menus.map(m => <option key={m.id || m._id} value={m.id || m._id}>{m.nom} — {m.prix} DT</option>)}</select></div>
          <div className="form-group"><label className="form-label">Statut</label><select className="form-input" value={form.statut || 'EN_ATTENTE'} onChange={set('statut')}><option value="EN_ATTENTE">En attente</option><option value="EN_PREPARATION">En préparation</option><option value="PRET">Prêt</option><option value="LIVRE">Livré</option></select></div>
          <div className="form-actions"><button className="btn btn-ghost" onClick={close}>Annuler</button><button className="btn btn-primary" onClick={saveCommande}>Enregistrer</button></div>
        </Modal>
      )}

      {modal === 'tkt' && (
        <Modal title="Nouveau ticket repas" onClose={close}>
          {error && <div className="alert alert-error">⚠ {error}</div>}
          <div className="form-group"><label className="form-label">Étudiant ID</label><input className="form-input" value={form.etudiantId || ''} onChange={set('etudiantId')} /></div>
          <div className="form-group"><label className="form-label">Type</label><select className="form-input" value={form.type || 'NORMAL'} onChange={set('type')}><option value="NORMAL">Normal</option><option value="SUBVENTIONNE">Subventionné</option><option value="GRATUIT">Gratuit</option></select></div>
          <div className="form-group"><label className="form-label">Date d'expiration</label><input className="form-input" type="date" value={form.dateExpiration || ''} onChange={set('dateExpiration')} /></div>
          <div className="form-actions"><button className="btn btn-ghost" onClick={close}>Annuler</button><button className="btn btn-primary" onClick={saveTicket}>Créer</button></div>
        </Modal>
      )}
    </div>
  );
}
