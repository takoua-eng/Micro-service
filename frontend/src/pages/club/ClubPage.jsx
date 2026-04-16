import React, { useState, useEffect } from 'react';
import CrudTable from '../../components/CrudTable';
import Modal from '../../components/Modal';
import { clubService } from '../../services/api';

export default function ClubPage() {
  const [tab, setTab] = useState('clubs');
  const [clubs, setClubs] = useState([]);
  const [selectedClub, setSelectedClub] = useState(null);
  const [evenements, setEvenements] = useState([]);
  const [membres, setMembres] = useState([]);
  const [reunions, setReunions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modal, setModal] = useState(null);
  const [form, setForm] = useState({});
  const [editId, setEditId] = useState(null);
  const [error, setError] = useState('');

  const loadClubs = async () => {
    setLoading(true);
    try { const r = await clubService.getAll(); setClubs(r.data); }
    catch { setClubs([]); }
    finally { setLoading(false); }
  };

  const loadDetails = async (club) => {
    if (!club) return;
    try {
      const [e, m, r] = await Promise.all([
        clubService.getEvenements(club.id),
        clubService.getMembres(club.id),
        clubService.getReunions(club.id),
      ]);
      setEvenements(e.data); setMembres(m.data); setReunions(r.data);
    } catch { setEvenements([]); setMembres([]); setReunions([]); }
  };

  useEffect(() => { loadClubs(); }, []);
  useEffect(() => { if (selectedClub) loadDetails(selectedClub); }, [selectedClub]);

  const set = k => e => setForm(f => ({ ...f, [k]: e.target.value }));
  const close = () => { setModal(null); setError(''); };

  // Club CRUD
  const saveClub = async () => {
    try {
      editId ? await clubService.update(editId, form) : await clubService.create(form);
      close(); loadClubs();
    } catch (e) { setError(e.response?.data?.message || 'Erreur'); }
  };
  const delClub = async row => {
    if (!window.confirm(`Supprimer "${row.nom}" ?`)) return;
    await clubService.delete(row.id); if (selectedClub?.id === row.id) setSelectedClub(null); loadClubs();
  };

  // Sub-entity helpers
  const addEvenement = async () => {
    try { await clubService.addEvenement(selectedClub.id, form); close(); loadDetails(selectedClub); }
    catch (e) { setError(e.response?.data?.message || 'Erreur'); }
  };
  const delEvenement = async row => {
    await clubService.deleteEvenement(selectedClub.id, row.id); loadDetails(selectedClub);
  };
  const addMembre = async () => {
    try { await clubService.addMembre(selectedClub.id, form); close(); loadDetails(selectedClub); }
    catch (e) { setError(e.response?.data?.message || 'Erreur'); }
  };
  const delMembre = async row => {
    await clubService.deleteMembre(selectedClub.id, row.id); loadDetails(selectedClub);
  };
  const addReunion = async () => {
    try { await clubService.addReunion(selectedClub.id, form); close(); loadDetails(selectedClub); }
    catch (e) { setError(e.response?.data?.message || 'Erreur'); }
  };

  const clubColumns = [
    { key: 'nom', label: 'Nom', accessor: 'nom' },
    { key: 'description', label: 'Description', render: r => <span style={{ color: 'var(--text2)', fontSize: '0.8rem' }}>{r.description?.slice(0,50)}…</span> },
    { key: 'categorie', label: 'Catégorie', render: r => <span className="badge badge-green">{r.categorie || '—'}</span> },
    { key: 'dateCreation', label: 'Créé le', render: r => <span style={{ color: 'var(--text2)', fontSize: '0.9rem' }}>{r.dateCreation ? new Date(r.dateCreation).toLocaleDateString() : '—'}</span> },
    { key: 'actions2', label: 'Détails', render: r => (
      <button className="btn btn-ghost btn-sm" onClick={() => { setSelectedClub(r); setTab('details'); }}>Voir →</button>
    )},
  ];

  return (
    <div className="content">
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 24 }}>
        <div>
          <h2 style={{ fontFamily: 'Syne', fontWeight: 800 }}>🎯 MS Club</h2>
          <p style={{ color: 'var(--text2)', fontSize: '0.875rem', marginTop: 4 }}>Clubs étudiants, événements, membres et réunions</p>
        </div>
        {tab === 'clubs' && (
          <button className="btn btn-primary" onClick={() => { setForm({ nom: '', description: '', categorie: '', dateCreation: '' }); setEditId(null); setModal('club'); setError(''); }}>+ Nouveau club</button>
        )}
        {tab === 'details' && selectedClub && (
          <div style={{ display: 'flex', gap: 8 }}>
            <button className="btn btn-ghost btn-sm" onClick={() => { setForm({ titre: '', date: '', lieu: '' }); setModal('ev'); }}>+ Événement</button>
            <button className="btn btn-ghost btn-sm" onClick={() => { setForm({ userId: '', role: 'MEMBRE' }); setModal('mb'); }}>+ Membre</button>
            <button className="btn btn-ghost btn-sm" onClick={() => { setForm({ sujet: '', date: '' }); setModal('re'); }}>+ Réunion</button>
          </div>
        )}
      </div>

      <div className="tabs">
        <button className={`tab ${tab === 'clubs' ? 'active' : ''}`} onClick={() => setTab('clubs')}>🎯 Clubs</button>
        {selectedClub && (
          <button className={`tab ${tab === 'details' ? 'active' : ''}`} onClick={() => setTab('details')}>
            📋 {selectedClub.nom}
          </button>
        )}
      </div>

      {tab === 'clubs' && (
        <div className="card">
          <CrudTable columns={clubColumns} data={clubs} loading={loading}
            onEdit={row => { setForm(row); setEditId(row.id); setModal('clubEdit'); setError(''); }}
            onDelete={delClub} emptyIcon="🎯" emptyText="Aucun club" />
        </div>
      )}

      {tab === 'details' && selectedClub && (
        <div style={{ display: 'flex', flexDirection: 'column', gap: 20 }}>
          <div className="card">
            <div className="card-header"><span className="card-title">🎉 Événements</span></div>
            <CrudTable
              columns={[
                { key: 'titre', label: 'Titre', accessor: 'titre' },
                { key: 'date', label: 'Date', accessor: 'date' },
                { key: 'lieu', label: 'Lieu', accessor: 'lieu' },
              ]}
              data={evenements} loading={false} onDelete={delEvenement} emptyIcon="🎉" emptyText="Aucun événement" />
          </div>
          <div className="card">
            <div className="card-header"><span className="card-title">👥 Membres</span></div>
            <CrudTable
              columns={[
                { key: 'userId', label: 'User ID', accessor: 'userId' },
                { key: 'role', label: 'Rôle', render: r => <span className="badge badge-green">{r.role}</span> },
              ]}
              data={membres} loading={false} onDelete={delMembre} emptyIcon="👥" emptyText="Aucun membre" />
          </div>
          <div className="card">
            <div className="card-header"><span className="card-title">📅 Réunions</span></div>
            <CrudTable
              columns={[
                { key: 'sujet', label: 'Sujet', accessor: 'sujet' },
                { key: 'date', label: 'Date', accessor: 'date' },
              ]}
              data={reunions} loading={false} emptyIcon="📅" emptyText="Aucune réunion" />
          </div>
        </div>
      )}

      {/* Club Modal */}
      {(modal === 'club' || modal === 'clubEdit') && (
        <Modal title={modal === 'club' ? 'Nouveau club' : 'Modifier club'} onClose={close}>
          {error && <div className="alert alert-error">⚠ {error}</div>}
          <div className="form-group">
            <label className="form-label">Nom</label>
            <input className="form-input" value={form.nom || ''} onChange={set('nom')} placeholder="Nom du club" />
          </div>
          <div className="form-group">
            <label className="form-label">Description</label>
            <textarea className="form-input" rows={3} value={form.description || ''} onChange={set('description')} />
          </div>
          <div className="form-group">
            <label className="form-label">Catégorie</label>
            <input className="form-input" value={form.categorie || ''} onChange={set('categorie')} placeholder="Sportif, Culturel…" />
          </div>
          <div className="form-group">
            <label className="form-label">Date de création</label>
            <input className="form-input" type="date" value={form.dateCreation || ''} onChange={set('dateCreation')} />
          </div>
          <div className="form-actions">
            <button className="btn btn-ghost" onClick={close}>Annuler</button>
            <button className="btn btn-primary" onClick={saveClub}>Enregistrer</button>
          </div>
        </Modal>
      )}

      {modal === 'ev' && (
        <Modal title="Nouvel événement" onClose={close}>
          {error && <div className="alert alert-error">⚠ {error}</div>}
          <div className="form-group"><label className="form-label">Titre</label><input className="form-input" value={form.titre || ''} onChange={set('titre')} /></div>
          <div className="form-group"><label className="form-label">Date</label><input className="form-input" type="date" value={form.date || ''} onChange={set('date')} /></div>
          <div className="form-group"><label className="form-label">Lieu</label><input className="form-input" value={form.lieu || ''} onChange={set('lieu')} /></div>
          <div className="form-actions">
            <button className="btn btn-ghost" onClick={close}>Annuler</button>
            <button className="btn btn-primary" onClick={addEvenement}>Ajouter</button>
          </div>
        </Modal>
      )}

      {modal === 'mb' && (
        <Modal title="Ajouter membre" onClose={close}>
          {error && <div className="alert alert-error">⚠ {error}</div>}
          <div className="form-group"><label className="form-label">User ID</label><input className="form-input" value={form.userId || ''} onChange={set('userId')} /></div>
          <div className="form-group">
            <label className="form-label">Rôle</label>
            <select className="form-input" value={form.role || 'MEMBRE'} onChange={set('role')}>
              <option value="MEMBRE">Membre</option><option value="PRESIDENT">Président</option><option value="VICE_PRESIDENT">Vice-président</option>
            </select>
          </div>
          <div className="form-actions">
            <button className="btn btn-ghost" onClick={close}>Annuler</button>
            <button className="btn btn-primary" onClick={addMembre}>Ajouter</button>
          </div>
        </Modal>
      )}

      {modal === 're' && (
        <Modal title="Nouvelle réunion" onClose={close}>
          {error && <div className="alert alert-error">⚠ {error}</div>}
          <div className="form-group"><label className="form-label">Sujet</label><input className="form-input" value={form.sujet || ''} onChange={set('sujet')} /></div>
          <div className="form-group"><label className="form-label">Date</label><input className="form-input" type="datetime-local" value={form.date || ''} onChange={set('date')} /></div>
          <div className="form-actions">
            <button className="btn btn-ghost" onClick={close}>Annuler</button>
            <button className="btn btn-primary" onClick={addReunion}>Ajouter</button>
          </div>
        </Modal>
      )}
    </div>
  );
}
