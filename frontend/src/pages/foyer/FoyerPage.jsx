import React, { useState, useEffect } from 'react';
import CrudTable from '../../components/CrudTable';
import Modal from '../../components/Modal';
import { foyerService } from '../../services/api';

export default function FoyerPage() {
  const [tab, setTab] = useState('chambres');
  const [chambres, setChambres] = useState([]);
  const [logements, setLogements] = useState([]);
  const [reclamations, setReclamations] = useState([]);
  const [attributions, setAttributions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [modal, setModal] = useState(null);
  const [form, setForm] = useState({});
  const [editId, setEditId] = useState(null);
  const [error, setError] = useState('');

  const load = async () => {
    setLoading(true);
    try {
      const [ch, lo, re, at] = await Promise.all([
        foyerService.getChambres(), foyerService.getLogements(),
        foyerService.getReclamations(), foyerService.getAttributions(),
      ]);
      setChambres(ch.data); setLogements(lo.data); setReclamations(re.data); setAttributions(at.data);
    } catch { }
    finally { setLoading(false); }
  };
  useEffect(() => { load(); }, []);

  const set = k => e => setForm(f => ({ ...f, [k]: e.target.value }));
  const close = () => { setModal(null); setError(''); };

  const saveLogement = async () => {
    try {
      editId ? await foyerService.updateLogement(editId, form) : await foyerService.createLogement(form);
      close(); load();
    } catch (e) { setError(e.response?.data?.message || 'Erreur'); }
  };

  const saveChambre = async () => {
    try {
      editId ? await foyerService.updateChambre(editId, form) : await foyerService.createChambre(form);
      close(); load();
    } catch (e) { setError(e.response?.data?.message || 'Erreur'); }
  };

  const saveReclamation = async () => {
    try {
      editId ? await foyerService.updateReclamation(editId, form) : await foyerService.createReclamation(form);
      close(); load();
    } catch (e) { setError(e.response?.data?.message || 'Erreur'); }
  };

  const saveAttribution = async () => {
    try { await foyerService.createAttribution(form); close(); load(); }
    catch (e) { setError(e.response?.data?.message || 'Erreur'); }
  };

  const logementColumns = [
    { key: 'nom', label: 'Nom', accessor: 'nom' },
    { key: 'adresse', label: 'Adresse', accessor: 'adresse' },
    { key: 'capacite', label: 'Capacité', render: r => <span className="badge badge-blue">{r.capacite} lits</span> },
    { key: 'type', label: 'Type', render: r => <span className="badge badge-purple">{r.type || '—'}</span> },
  ];

  const chambreColumns = [
    { key: 'numero', label: 'N°', accessor: 'numero' },
    { key: 'type', label: 'Type', render: r => <span className="badge badge-orange">{r.type || '—'}</span> },
    { key: 'capacite', label: 'Capacité', accessor: 'capacite' },
    { key: 'dispo', label: 'Disponible', render: r => (
      <span className={`badge ${r.disponible ? 'badge-green' : 'badge-pink'}`}>{r.disponible ? 'Oui' : 'Non'}</span>
    )},
    { key: 'logement', label: 'Logement', render: r => {
      const lo = logements.find(l => l.id === r.logementId);
      return lo?.nom || r.logementId || '—';
    }},
  ];

  const reclaColumns = [
    { key: 'titre', label: 'Titre', accessor: 'titre' },
    { key: 'chambreId', label: 'Chambre', accessor: 'chambreId' },
    { key: 'statut', label: 'Statut', render: r => (
      <span className={`badge ${r.statut === 'RESOLU' ? 'badge-green' : r.statut === 'EN_COURS' ? 'badge-orange' : 'badge-pink'}`}>{r.statut || 'EN_ATTENTE'}</span>
    )},
    { key: 'desc', label: 'Description', render: r => <span style={{ color: 'var(--text2)', fontSize: '0.8rem' }}>{r.description?.slice(0, 60)}</span> },
  ];

  const attrColumns = [
    { key: 'etudiantId', label: 'Étudiant ID', accessor: 'etudiantId' },
    { key: 'chambreId', label: 'Chambre', accessor: 'chambreId' },
    { key: 'dateDebut', label: 'Début', accessor: 'dateDebut' },
    { key: 'dateFin', label: 'Fin', accessor: 'dateFin' },
  ];

  const btnLabel = { logements: 'Nouveau logement', chambres: 'Nouvelle chambre', reclamations: 'Nouvelle réclamation', attributions: 'Nouvelle attribution' };
  const openModal = { logements: () => { setForm({}); setEditId(null); setModal('log'); }, chambres: () => { setForm({}); setEditId(null); setModal('ch'); }, reclamations: () => { setForm({}); setEditId(null); setModal('rec'); }, attributions: () => { setForm({}); setModal('attr'); } };

  return (
    <div className="content">
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: 24 }}>
        <div>
          <h2 style={{ fontFamily: 'Syne', fontWeight: 800 }}>🏠 MS Foyer</h2>
          <p style={{ color: 'var(--text2)', fontSize: '0.875rem', marginTop: 4 }}>Logements, chambres, réclamations et attribution des lits</p>
        </div>
        <button className="btn btn-primary" onClick={openModal[tab]}>+ {btnLabel[tab]}</button>
      </div>

      <div className="stats-grid">
        <div className="stat-card orange"><div className="stat-label">Logements</div><div className="stat-value">{logements.length}</div></div>
        <div className="stat-card blue"><div className="stat-label">Chambres</div><div className="stat-value">{chambres.length}</div></div>
        <div className="stat-card pink"><div className="stat-label">Réclamations</div><div className="stat-value">{reclamations.length}</div></div>
        <div className="stat-card green"><div className="stat-label">Attributions</div><div className="stat-value">{attributions.length}</div></div>
      </div>

      <div className="tabs">
        {['logements','chambres','reclamations','attributions'].map(t => (
          <button key={t} className={`tab ${tab === t ? 'active' : ''}`} onClick={() => setTab(t)}>
            {{ logements: '🏢', chambres: '🛏', reclamations: '⚠️', attributions: '🔑' }[t]} {t.charAt(0).toUpperCase() + t.slice(1)}
          </button>
        ))}
      </div>

      <div className="card">
        {tab === 'logements' && <CrudTable columns={logementColumns} data={logements} loading={loading}
          onEdit={r => { setForm(r); setEditId(r.id); setModal('logEdit'); }} onDelete={async r => { await foyerService.deleteLogement(r.id); load(); }} emptyIcon="🏢" emptyText="Aucun logement" />}
        {tab === 'chambres' && <CrudTable columns={chambreColumns} data={chambres} loading={loading}
          onEdit={r => { setForm(r); setEditId(r.id); setModal('chEdit'); }} onDelete={async r => { await foyerService.deleteChambre(r.id); load(); }} emptyIcon="🛏" emptyText="Aucune chambre" />}
        {tab === 'reclamations' && <CrudTable columns={reclaColumns} data={reclamations} loading={loading}
          onEdit={r => { setForm(r); setEditId(r.id); setModal('recEdit'); }} onDelete={async r => { await foyerService.deleteReclamation(r.id); load(); }} emptyIcon="⚠️" emptyText="Aucune réclamation" />}
        {tab === 'attributions' && <CrudTable columns={attrColumns} data={attributions} loading={loading}
          onDelete={async r => { await foyerService.deleteAttribution(r.id); load(); }} emptyIcon="🔑" emptyText="Aucune attribution" />}
      </div>

      {/* Logement Modal */}
      {(modal === 'log' || modal === 'logEdit') && (
        <Modal title={modal === 'log' ? 'Nouveau logement' : 'Modifier logement'} onClose={close}>
          {error && <div className="alert alert-error">⚠ {error}</div>}
          <div className="form-group"><label className="form-label">Nom</label><input className="form-input" value={form.nom || ''} onChange={set('nom')} /></div>
          <div className="form-group"><label className="form-label">Adresse</label><input className="form-input" value={form.adresse || ''} onChange={set('adresse')} /></div>
          <div className="form-row">
            <div className="form-group"><label className="form-label">Capacité</label><input className="form-input" type="number" value={form.capacite || ''} onChange={set('capacite')} /></div>
            <div className="form-group"><label className="form-label">Type</label><select className="form-input" value={form.type || ''} onChange={set('type')}><option value="">—</option><option value="MASCULIN">Masculin</option><option value="FEMININ">Féminin</option><option value="MIXTE">Mixte</option></select></div>
          </div>
          <div className="form-actions"><button className="btn btn-ghost" onClick={close}>Annuler</button><button className="btn btn-primary" onClick={saveLogement}>Enregistrer</button></div>
        </Modal>
      )}

      {(modal === 'ch' || modal === 'chEdit') && (
        <Modal title={modal === 'ch' ? 'Nouvelle chambre' : 'Modifier chambre'} onClose={close}>
          {error && <div className="alert alert-error">⚠ {error}</div>}
          <div className="form-row">
            <div className="form-group"><label className="form-label">Numéro</label><input className="form-input" value={form.numero || ''} onChange={set('numero')} /></div>
            <div className="form-group"><label className="form-label">Capacité</label><input className="form-input" type="number" value={form.capacite || ''} onChange={set('capacite')} /></div>
          </div>
          <div className="form-group"><label className="form-label">Type</label><select className="form-input" value={form.type || ''} onChange={set('type')}><option value="">—</option><option value="SIMPLE">Simple</option><option value="DOUBLE">Double</option><option value="TRIPLE">Triple</option></select></div>
          <div className="form-group"><label className="form-label">Logement</label><select className="form-input" value={form.logementId || ''} onChange={set('logementId')}><option value="">— Sélectionner —</option>{logements.map(l => <option key={l.id} value={l.id}>{l.nom}</option>)}</select></div>
          <div className="form-actions"><button className="btn btn-ghost" onClick={close}>Annuler</button><button className="btn btn-primary" onClick={saveChambre}>Enregistrer</button></div>
        </Modal>
      )}

      {(modal === 'rec' || modal === 'recEdit') && (
        <Modal title={modal === 'rec' ? 'Nouvelle réclamation' : 'Modifier réclamation'} onClose={close}>
          {error && <div className="alert alert-error">⚠ {error}</div>}
          <div className="form-group"><label className="form-label">Titre</label><input className="form-input" value={form.titre || ''} onChange={set('titre')} /></div>
          <div className="form-group"><label className="form-label">Description</label><textarea className="form-input" rows={3} value={form.description || ''} onChange={set('description')} /></div>
          <div className="form-row">
            <div className="form-group"><label className="form-label">Chambre</label><select className="form-input" value={form.chambreId || ''} onChange={set('chambreId')}><option value="">—</option>{chambres.map(c => <option key={c.id} value={c.id}>Ch. {c.numero}</option>)}</select></div>
            <div className="form-group"><label className="form-label">Statut</label><select className="form-input" value={form.statut || 'EN_ATTENTE'} onChange={set('statut')}><option value="EN_ATTENTE">En attente</option><option value="EN_COURS">En cours</option><option value="RESOLU">Résolu</option></select></div>
          </div>
          <div className="form-actions"><button className="btn btn-ghost" onClick={close}>Annuler</button><button className="btn btn-primary" onClick={saveReclamation}>Enregistrer</button></div>
        </Modal>
      )}

      {modal === 'attr' && (
        <Modal title="Nouvelle attribution" onClose={close}>
          {error && <div className="alert alert-error">⚠ {error}</div>}
          <div className="form-group"><label className="form-label">Étudiant ID</label><input className="form-input" value={form.etudiantId || ''} onChange={set('etudiantId')} /></div>
          <div className="form-group"><label className="form-label">Chambre</label><select className="form-input" value={form.chambreId || ''} onChange={set('chambreId')}><option value="">—</option>{chambres.filter(c => c.disponible).map(c => <option key={c.id} value={c.id}>Ch. {c.numero}</option>)}</select></div>
          <div className="form-row">
            <div className="form-group"><label className="form-label">Date début</label><input className="form-input" type="date" value={form.dateDebut || ''} onChange={set('dateDebut')} /></div>
            <div className="form-group"><label className="form-label">Date fin</label><input className="form-input" type="date" value={form.dateFin || ''} onChange={set('dateFin')} /></div>
          </div>
          <div className="form-actions"><button className="btn btn-ghost" onClick={close}>Annuler</button><button className="btn btn-primary" onClick={saveAttribution}>Attribuer</button></div>
        </Modal>
      )}
    </div>
  );
}
