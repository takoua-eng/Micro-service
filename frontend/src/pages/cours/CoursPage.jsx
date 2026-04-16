import React, { useState, useEffect } from 'react';
import CrudTable from '../../components/CrudTable';
import Modal from '../../components/Modal';
import { coursService } from '../../services/api';

const EMPTY_COURS = { titre: '', code: '', description: '', credits: '', enseignantId: '' };
const EMPTY_ENS = { nom: '', prenom: '', email: '', specialite: '' };

export default function CoursPage() {
  const [tab, setTab] = useState('cours');
  const [cours, setCours] = useState([]);
  const [enseignants, setEnseignants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modal, setModal] = useState(null);
  const [form, setForm] = useState({});
  const [editId, setEditId] = useState(null);
  const [error, setError] = useState('');

  const load = async () => {
    setLoading(true);
    try {
      const [c, e] = await Promise.all([coursService.getAll(), coursService.getEnseignants()]);
      setCours(c.data); setEnseignants(e.data);
    } catch { setCours([]); setEnseignants([]); }
    finally { setLoading(false); }
  };
  useEffect(() => { load(); }, []);

  const set = k => e => setForm(f => ({ ...f, [k]: e.target.value }));

  // COURS CRUD
  const openCreateCours = () => { setForm(EMPTY_COURS); setEditId(null); setModal('cours'); setError(''); };
  const openEditCours = row => { setForm(row); setEditId(row.id); setModal('coursEdit'); setError(''); };
  const saveCours = async () => {
    try {
      editId ? await coursService.update(editId, form) : await coursService.create(form);
      close(); load();
    } catch (e) { setError(e.response?.data?.message || 'Erreur'); }
  };
  const delCours = async row => {
    if (!window.confirm(`Supprimer "${row.titre}" ?`)) return;
    await coursService.delete(row.id); load();
  };

  // ENSEIGNANT CRUD
  const openCreateEns = () => { setForm(EMPTY_ENS); setEditId(null); setModal('ens'); setError(''); };
  const openEditEns = row => { setForm(row); setEditId(row.id); setModal('ensEdit'); setError(''); };
  const saveEns = async () => {
    try {
      if (editId) { /* update endpoint may vary */ } else await coursService.createEnseignant(form);
      close(); load();
    } catch (e) { setError(e.response?.data?.message || 'Erreur'); }
  };
  const delEns = async row => {
    if (!window.confirm(`Supprimer ${row.prenom} ${row.nom} ?`)) return;
    await coursService.deleteEnseignant(row.id); load();
  };

  const close = () => { setModal(null); setError(''); };

  const coursColumns = [
    { key: 'code', label: 'Code', accessor: 'code' },
    { key: 'titre', label: 'Titre', accessor: 'titre' },
    { key: 'credits', label: 'Crédits', render: r => <span className="badge badge-blue">{r.credits} cr.</span> },
    { key: 'enseignant', label: 'Enseignant', render: r => {
      const ens = enseignants.find(e => e.id === r.enseignantId);
      return ens ? `${ens.prenom} ${ens.nom}` : r.enseignantId || '—';
    }},
    { key: 'desc', label: 'Description', render: r => <span style={{ color: 'var(--text2)', fontSize: '0.8rem' }}>{r.description?.slice(0,60)}…</span> },
  ];

  const ensColumns = [
    { key: 'name', label: 'Nom', render: r => `${r.prenom || ''} ${r.nom || ''}` },
    { key: 'email', label: 'Email', accessor: 'email' },
    { key: 'specialite', label: 'Spécialité', render: r => <span className="badge badge-purple">{r.specialite || '—'}</span> },
  ];

  return (
    <div className="content">
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 24 }}>
        <div>
          <h2 style={{ fontFamily: 'Syne', fontWeight: 800 }}>📚 MS Cours</h2>
          <p style={{ color: 'var(--text2)', fontSize: '0.875rem', marginTop: 4 }}>Cours, enseignants, supports et crédits académiques</p>
        </div>
        <button className="btn btn-primary" onClick={tab === 'cours' ? openCreateCours : openCreateEns}>
          + {tab === 'cours' ? 'Nouveau cours' : 'Nouvel enseignant'}
        </button>
      </div>

      <div className="tabs">
        {['cours', 'enseignants'].map(t => (
          <button key={t} className={`tab ${tab === t ? 'active' : ''}`} onClick={() => setTab(t)}>
            {t === 'cours' ? '📚 Cours' : '👨‍🏫 Enseignants'}
          </button>
        ))}
      </div>

      <div className="card">
        {tab === 'cours' && (
          <CrudTable columns={coursColumns} data={cours} loading={loading} onEdit={openEditCours} onDelete={delCours} emptyIcon="📚" emptyText="Aucun cours" />
        )}
        {tab === 'enseignants' && (
          <CrudTable columns={ensColumns} data={enseignants} loading={loading} onEdit={openEditEns} onDelete={delEns} emptyIcon="👨‍🏫" emptyText="Aucun enseignant" />
        )}
      </div>

      {/* Cours Modal */}
      {(modal === 'cours' || modal === 'coursEdit') && (
        <Modal title={modal === 'cours' ? 'Nouveau cours' : 'Modifier cours'} onClose={close}>
          {error && <div className="alert alert-error">⚠ {error}</div>}
          <div className="form-row">
            <div className="form-group">
              <label className="form-label">Code</label>
              <input className="form-input" value={form.code || ''} onChange={set('code')} placeholder="INF101" />
            </div>
            <div className="form-group">
              <label className="form-label">Crédits</label>
              <input className="form-input" type="number" value={form.credits || ''} onChange={set('credits')} placeholder="3" />
            </div>
          </div>
          <div className="form-group">
            <label className="form-label">Titre</label>
            <input className="form-input" value={form.titre || ''} onChange={set('titre')} placeholder="Titre du cours" />
          </div>
          <div className="form-group">
            <label className="form-label">Description</label>
            <textarea className="form-input" rows={3} value={form.description || ''} onChange={set('description')} placeholder="Description…" />
          </div>
          <div className="form-group">
            <label className="form-label">Enseignant</label>
            <select className="form-input" value={form.enseignantId || ''} onChange={set('enseignantId')}>
              <option value="">— Sélectionner —</option>
              {enseignants.map(e => (
                <option key={e.id} value={e.id}>{e.prenom} {e.nom}</option>
              ))}
            </select>
          </div>
          <div className="form-actions">
            <button className="btn btn-ghost" onClick={close}>Annuler</button>
            <button className="btn btn-primary" onClick={saveCours}>Enregistrer</button>
          </div>
        </Modal>
      )}

      {/* Enseignant Modal */}
      {(modal === 'ens' || modal === 'ensEdit') && (
        <Modal title={modal === 'ens' ? 'Nouvel enseignant' : 'Modifier enseignant'} onClose={close}>
          {error && <div className="alert alert-error">⚠ {error}</div>}
          <div className="form-row">
            <div className="form-group">
              <label className="form-label">Prénom</label>
              <input className="form-input" value={form.prenom || ''} onChange={set('prenom')} placeholder="Prénom" />
            </div>
            <div className="form-group">
              <label className="form-label">Nom</label>
              <input className="form-input" value={form.nom || ''} onChange={set('nom')} placeholder="Nom" />
            </div>
          </div>
          <div className="form-group">
            <label className="form-label">Email</label>
            <input className="form-input" type="email" value={form.email || ''} onChange={set('email')} placeholder="email@campus.tn" />
          </div>
          <div className="form-group">
            <label className="form-label">Spécialité</label>
            <input className="form-input" value={form.specialite || ''} onChange={set('specialite')} placeholder="Informatique, Mathématiques…" />
          </div>
          <div className="form-actions">
            <button className="btn btn-ghost" onClick={close}>Annuler</button>
            <button className="btn btn-primary" onClick={saveEns}>Enregistrer</button>
          </div>
        </Modal>
      )}
    </div>
  );
}
