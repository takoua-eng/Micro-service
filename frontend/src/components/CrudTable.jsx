import React, { useState } from 'react';

export default function CrudTable({ columns, data, onEdit, onDelete, loading, emptyIcon = '📭', emptyText = 'Aucune donnée' }) {
  const [search, setSearch] = useState('');

  const filtered = search
    ? data.filter(row =>
        columns.some(col => {
          const val = col.accessor ? row[col.accessor] : '';
          return String(val || '').toLowerCase().includes(search.toLowerCase());
        })
      )
    : data;

  return (
    <>
      <div className="toolbar">
        <input
          className="search-input"
          placeholder="Rechercher…"
          value={search}
          onChange={e => setSearch(e.target.value)}
        />
      </div>

      {loading ? (
        <div className="loading"><div className="spinner" /> Chargement…</div>
      ) : filtered.length === 0 ? (
        <div className="empty">
          <div className="empty-icon">{emptyIcon}</div>
          <p>{search ? 'Aucun résultat pour cette recherche.' : emptyText}</p>
        </div>
      ) : (
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                {columns.map(col => <th key={col.key}>{col.label}</th>)}
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((row, i) => (
                <tr key={row.id || i}>
                  {columns.map(col => (
                    <td key={col.key}>
                      {col.render ? col.render(row) : row[col.accessor] ?? '—'}
                    </td>
                  ))}
                  <td>
                    <div style={{ display: 'flex', gap: 6 }}>
                      {onEdit && (
                        <button className="btn btn-ghost btn-sm" onClick={() => onEdit(row)}>✏️ Éditer</button>
                      )}
                      {onDelete && (
                        <button className="btn btn-danger btn-sm" onClick={() => onDelete(row)}>🗑</button>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </>
  );
}
