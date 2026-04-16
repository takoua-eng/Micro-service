import React, { useState, useEffect } from 'react';
import CrudTable from '../../components/CrudTable';
import Modal from '../../components/Modal';
import { bibliothequeService } from '../../services/api';

export default function BibliothequePage() {
  const [tab, setTab] = useState('livres');
  const [livres, setLivres] = useState([]);
  const [emprunts, setEmprunts] = useState([]);
  const [reservations, setReservations] = useState([]);
  const [penalites, setPenalites] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modal, setModal] = useState(null);
  const [form, setForm] = useState({});
  const [editId, setEditId] = useState(null);
  const [error, setError] = useState('');

  const load = async () => {
    setLoading(true);
    try {
      const [l, e, r, p] = await Promise.all([
        bibliothequeService.getLivres(), bibliothequeService.getEmprunts(),
        bibliothequeService.getReservations(), bibliothequeService.getPenalites(),
      ]);
      setLivres(l.data); setEmprunts(e.data); setReservations(r.data); setPenalites(p.data);
    } catch { }
    finally { setLoading(false); }
  };
  useEffect(() => { load(); }, []);

  const set = k => e => setForm(f => ({ ...f, [k]: e.target.value }));
  const close = () => { setModal(null); setError(''); };

  const saveLivre = async () => {
    try {
      editId ? await bibliothequeService.updateLivre(editId, form) : await bibliothequeService.createLivre(form);
      close(); load();
    } catch (e) { setError(e.response?.data?.message || 'Erreur'); }
  };

  const saveEmprunt = async () => {
    try { await bibliothequeService.createEmprunt(form); close(); load(); }
    catch (e) { setError(e.response?.data?.message || 'Erreur'); }
  };

  const saveReservation = async () => {
    try { await bibliothequeService.createReservation(form); close(); load(); }
    catch (e) { setError(e.response?.data?.message || 'Erreur'); }
  };

  const livreColumns = [
    { key: 'isbn', label: 'ISBN', accessor: 'isbn' },
    { key: 'titre', label: 'Titre', accessor: 'titre' },
    { key: 'auteur', label: 'Auteur', accessor: 'auteur' },
    { key: 'categorie', label: 'Catégorie', render: r => <span className="badge badge-blue">{r.categorie || '—'}</span> },
    { key: 'dispo', label: 'Exemplaires', render: r => <span className="badge badge-green">{r.exemplairesDisponibles ?? r.exemplaires ?? '—'}</span> },
  ];

  const empruntColumns = [
    { key: 'etudiantId', label: 'Étudiant', accessor: 'etudiantId' },
    { key: 'livreId', label: 'Livre', render: r => { const l = livres.find(li => li.id === r.livreId); return l?.titre || r.livreId || '—'; } },
    { key: 'dateEmprunt', label: 'Emprunté le', accessor: 'dateEmprunt' },
    { key: 'dateRetour', label: 'Retour prévu', accessor: 'dateRetourPrevue' },
    { key: 'statut', label: 'Statut', render: r => <span className={`badge ${r.dateRetourEffective ? 'badge-green' : 'badge-orange'}`}>{r.dateRetourEffective ? 'Rendu' : 'En cours'}</span> },
    { key: 'ret', label: '', render: r => !r.dateRetourEffective && (
      <button className="btn btn-success btn-sm" onClick={async () => { await bibliothequeService.returnEmprunt(r.id); load(); }}>↩ Retour</button>
    )},
  ];

  const reservColumns = [
    { key: 'etudiantId', label: 'Étudiant', accessor: 'etudiantId' },
    { key: 'livreId', label: 'Livre', render: r => { const l = livres.find(li => li.id === r.livreId); return l?.titre || r.livreId || '—'; } },
    { key: 'date', label: 'Date', accessor: 'dateReservation' },
    { key: 'statut', label: 'Statut', render: r => <span className="badge badge-purple">{r.statut || 'EN_ATTENTE'}</span> },
  ];

  const penaliteColumns = [
    { key: 'etudiantId', label: 'Étudiant', accessor: 'etudiantId' },
    { key: 'montant', label: 'Montant', render: r => <span className="badge badge-pink">{r.montant} DT</span> },
    { key: 'raison', label: 'Raison', accessor: 'raison' },
    { key: 'date', label: 'Date', accessor: 'date' },
  ];

  return (
    <div className="content">
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 24 }}>
        <div>
          <h2 style={{ fontFamily: 'Syne', fontWeight: 800 }}>📖 MS Bibliothèque</h2>
          <p style={{ color: 'var(--text2)', fontSize: '0.875rem', marginTop: 4 }}>Livres, emprunts, réservations et pénalités</p>
        </div>
        <div style={{ display: 'flex', gap: 8 }}>
          {tab === 'livres' && <button className="btn btn-primary" onClick={() => { setForm({}); setEditId(null); setModal('livre'); }}>+ Nouveau livre</button>}
          {tab === 'emprunts' && <button className="btn btn-primary" onClick={() => { setForm({}); setModal('emprunt'); }}>+ Emprunt</button>}
          {tab === 'reservations' && <button className="btn btn-primary" onClick={() => { setForm({}); setModal('reserv'); }}>+ Réservation</button>}
        </div>
      </div>

      <div className="stats-grid">
        <div className="stat-card blue"><div className="stat-label">Livres</div><div className="stat-value">{livres.length}</div></div>
        <div className="stat-card orange"><div className="stat-label">Emprunts</div><div className="stat-value">{emprunts.length}</div></div>
        <div className="stat-card purple"><div className="stat-label">Réservations</div><div className="stat-value">{reservations.length}</div></div>
        <div className="stat-card pink"><div className="stat-label">Pénalités</div><div className="stat-value">{penalites.length}</div></div>
      </div>

      <div className="tabs">
        {['livres','emprunts','reservations','penalites'].map(t => (
          <button key={t} className={`tab ${tab === t ? 'active' : ''}`} onClick={() => setTab(t)}>
            {{'livres':'📚','emprunts':'📤','reservations':'🔖','penalites':'💸'}[t]} {t.charAt(0).toUpperCase() + t.slice(1)}
          </button>
        ))}
      </div>

      <div className="card">
        {tab === 'livres' && <CrudTable columns={livreColumns} data={livres} loading={loading}
          onEdit={r => { setForm(r); setEditId(r.id); setModal('livreEdit'); }}
          onDelete={async r => { await bibliothequeService.deleteLivre(r.id); load(); }} emptyIcon="📚" emptyText="Aucun livre" />}
        {tab === 'emprunts' && <CrudTable columns={empruntColumns} data={emprunts} loading={loading}
          onDelete={async r => { await bibliothequeService.deleteEmprunt(r.id); load(); }} emptyIcon="📤" emptyText="Aucun emprunt" />}
        {tab === 'reservations' && <CrudTable columns={reservColumns} data={reservations} loading={loading}
          onDelete={async r => { await bibliothequeService.deleteReservation(r.id); load(); }} emptyIcon="🔖" emptyText="Aucune réservation" />}
        {tab === 'penalites' && <CrudTable columns={penaliteColumns} data={penalites} loading={loading}
          onDelete={async r => { await bibliothequeService.deletePenalite(r.id); load(); }} emptyIcon="💸" emptyText="Aucune pénalité" />}
      </div>

      {(modal === 'livre' || modal === 'livreEdit') && (
        <Modal title={modal === 'livre' ? 'Nouveau livre' : 'Modifier livre'} onClose={close}>
          {error && <div className="alert alert-error">⚠ {error}</div>}
          <div className="form-group"><label className="form-label">ISBN</label><input className="form-input" value={form.isbn || ''} onChange={set('isbn')} /></div>
          <div className="form-group"><label className="form-label">Titre</label><input className="form-input" value={form.titre || ''} onChange={set('titre')} /></div>
          <div className="form-group"><label className="form-label">Auteur</label><input className="form-input" value={form.auteur || ''} onChange={set('auteur')} /></div>
          <div className="form-row">
            <div className="form-group"><label className="form-label">Catégorie</label><input className="form-input" value={form.categorie || ''} onChange={set('categorie')} /></div>
            <div className="form-group"><label className="form-label">Exemplaires</label><input className="form-input" type="number" value={form.exemplaires || ''} onChange={set('exemplaires')} /></div>
          </div>
          <div className="form-group"><label className="form-label">Année publication</label><input className="form-input" type="number" value={form.anneePublication || ''} onChange={set('anneePublication')} /></div>
          <div className="form-actions"><button className="btn btn-ghost" onClick={close}>Annuler</button><button className="btn btn-primary" onClick={saveLivre}>Enregistrer</button></div>
        </Modal>
      )}

      {modal === 'emprunt' && (
        <Modal title="Nouvel emprunt" onClose={close}>
          {error && <div className="alert alert-error">⚠ {error}</div>}
          <div className="form-group"><label className="form-label">Étudiant ID</label><input className="form-input" value={form.etudiantId || ''} onChange={set('etudiantId')} /></div>
          <div className="form-group"><label className="form-label">Livre</label><select className="form-input" value={form.livreId || ''} onChange={set('livreId')}><option value="">—</option>{livres.map(l => <option key={l.id} value={l.id}>{l.titre}</option>)}</select></div>
          <div className="form-group"><label className="form-label">Date retour prévue</label><input className="form-input" type="date" value={form.dateRetourPrevue || ''} onChange={set('dateRetourPrevue')} /></div>
          <div className="form-actions"><button className="btn btn-ghost" onClick={close}>Annuler</button><button className="btn btn-primary" onClick={saveEmprunt}>Emprunter</button></div>
        </Modal>
      )}

      {modal === 'reserv' && (
        <Modal title="Nouvelle réservation" onClose={close}>
          {error && <div className="alert alert-error">⚠ {error}</div>}
          <div className="form-group"><label className="form-label">Étudiant ID</label><input className="form-input" value={form.etudiantId || ''} onChange={set('etudiantId')} /></div>
          <div className="form-group"><label className="form-label">Livre</label><select className="form-input" value={form.livreId || ''} onChange={set('livreId')}><option value="">—</option>{livres.map(l => <option key={l.id} value={l.id}>{l.titre}</option>)}</select></div>
          <div className="form-actions"><button className="btn btn-ghost" onClick={close}>Annuler</button><button className="btn btn-primary" onClick={saveReservation}>Réserver</button></div>
        </Modal>
      )}
    </div>
  );
}
