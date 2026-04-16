import React, { useState, useEffect } from 'react';
import CrudTable from '../../components/CrudTable';
import Modal from '../../components/Modal';
import { userService } from '../../services/api';

const EMPTY = { name: '', email: '', password: '', role: 'ETUDIANT' };

export default function UsersPage() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modal, setModal] = useState(null); // null | 'create' | 'edit'
  const [form, setForm] = useState(EMPTY);
  const [editId, setEditId] = useState(null);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const load = async () => {
    setLoading(true);
    try { const r = await userService.getAll(); setUsers(r.data); }
    catch { setUsers([]); }
    finally { setLoading(false); }
  };

  useEffect(() => { load(); }, []);

  const openCreate = () => { setForm(EMPTY); setEditId(null); setModal('create'); setError(''); };
  const openEdit = row => { setForm({ ...row, password: '' }); setEditId(row.id); setModal('edit'); setError(''); };
  const close = () => setModal(null);

  const save = async () => {
    try {
      if (modal === 'create') await userService.create(form);
      else await userService.update(editId, form);
      setSuccess(modal === 'create' ? 'Utilisateur créé !' : 'Utilisateur mis à jour !');
      setTimeout(() => setSuccess(''), 3000);
      close(); load();
    } catch (e) { setError(e.response?.data?.message || 'Erreur lors de l\'opération.'); }
  };

  const del = async row => {
    if (!window.confirm(`Supprimer ${row.name} ?`)) return;
    try { await userService.delete(row.id); load(); }
    catch { setError('Erreur suppression.'); }
  };

  const columns = [
    { key: 'name', label: 'Nom complet', accessor: 'name' },
    { key: 'email', label: 'Email', accessor: 'email' },
    { key: 'role', label: 'Rôle', render: r => (
      <span className={`badge ${r.role === 'admin' ? 'badge-pink' : r.role === 'enseignant' ? 'badge-purple' : 'badge-blue'}`}>
        {r.role}
      </span>
    )},
  ];

  const set = k => e => setForm(f => ({ ...f, [k]: e.target.value }));

  return (
    <div className="content">
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 24 }}>
        <div>
          <h2 style={{ fontFamily: 'Syne', fontWeight: 800 }}>👤 MS User</h2>
          <p style={{ color: 'var(--text2)', fontSize: '0.875rem', marginTop: 4 }}>Gestion des comptes, profils et rôles</p>
        </div>
        <button className="btn btn-primary" onClick={openCreate}>+ Nouvel utilisateur</button>
      </div>

      {success && <div className="alert alert-success">✓ {success}</div>}
      {error && <div className="alert alert-error">⚠ {error}</div>}

      <div className="card">
        <CrudTable columns={columns} data={users} loading={loading} onEdit={openEdit} onDelete={del} emptyIcon="👤" emptyText="Aucun utilisateur" />
      </div>

      {modal && (
        <Modal title={modal === 'create' ? 'Nouvel utilisateur' : 'Modifier utilisateur'} onClose={close}>
          {error && <div className="alert alert-error">⚠ {error}</div>}
          <div className="form-group">
            <label className="form-label">Nom complet</label>
            <input className="form-input" value={form.name || ''} onChange={set('name')} placeholder="Nom complet" />
          </div>
          <div className="form-group">
            <label className="form-label">Email</label>
            <input className="form-input" type="email" value={form.email || ''} onChange={set('email')} placeholder="email@campus.tn" />
          </div>
          <div className="form-group">
            <label className="form-label">Rôle</label>
            <select className="form-input" value={form.role || 'etudiant'} onChange={set('role')}>
              <option value="etudiant">Étudiant</option>
              <option value="enseignant">Enseignant</option>
              <option value="admin">Admin</option>
            </select>
          </div>
          {modal === 'create' && (
            <div className="form-group">
              <label className="form-label">Mot de passe</label>
              <input className="form-input" type="password" value={form.password} onChange={set('password')} placeholder="••••••••" />
            </div>
          )}
          <div className="form-actions">
            <button className="btn btn-ghost" onClick={close}>Annuler</button>
            <button className="btn btn-primary" onClick={save}>{modal === 'create' ? 'Créer' : 'Enregistrer'}</button>
          </div>
        </Modal>
      )}
    </div>
  );
}
