const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

async function request(path, options = {}, token) {
  const response = await fetch(`${API_URL}${path}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...options.headers,
    },
  });
  if (!response.ok) throw new Error(await response.text() || 'Request failed');
  if (response.status === 204) return null;
  return response.json();
}

export const api = {
  listTransactions: (token) => request('/transactions', {}, token),
  summary: (token) => request('/transactions/summary', {}, token),
  createTransaction: (transaction, token) => request('/transactions', { method: 'POST', body: JSON.stringify(transaction) }, token),
  updateTransaction: (id, transaction, token) => request(`/transactions/${id}`, { method: 'PUT', body: JSON.stringify(transaction) }, token),
  deleteTransaction: (id, token) => request(`/transactions/${id}`, { method: 'DELETE' }, token),
};
