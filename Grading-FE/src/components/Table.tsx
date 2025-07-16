import React from 'react';

interface TableProps {
  headers: string[];
  rows: React.ReactNode[][];
  loading?: boolean;
  emptyText?: string;
}

const Table: React.FC<TableProps> = ({ headers, rows, loading, emptyText }) => {
  return (
    <div className="overflow-x-auto rounded shadow">
      <table className="min-w-full bg-white dark:bg-gray-900">
        <thead>
          <tr>
            {headers.map((header, idx) => (
              <th key={idx} className="px-4 py-2 text-left text-sm font-semibold text-gray-700 dark:text-gray-200 border-b">
                {header}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {loading ? (
            <tr><td colSpan={headers.length} className="p-4 text-center">Loading...</td></tr>
          ) : rows.length === 0 ? (
            <tr><td colSpan={headers.length} className="p-4 text-center text-gray-400">{emptyText || 'No data'}</td></tr>
          ) : (
            rows.map((row, i) => (
              <tr key={i} className="hover:bg-gray-100 dark:hover:bg-gray-800">
                {row.map((cell, j) => (
                  <td key={j} className="px-4 py-2 border-b dark:border-gray-700">{cell}</td>
                ))}
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
};

export default Table; 