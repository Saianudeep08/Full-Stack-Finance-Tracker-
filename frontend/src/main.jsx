import React, { useEffect, useMemo, useState } from 'react';
import { createRoot } from 'react-dom/client';
import { DollarSign, LogOut, Plus, Trash2 } from 'lucide-react';
import { api } from './services/api';
import { auth, firebaseEnabled, loginWithGoogle, logout } from './firebase';
import './styles.css';

const blank = { title: '', category: 'General', amount: '', type: 'EXPENSE', transactionDate: new Date().toISOString().slice(0, 10), notes: '' };

function App() {
  const [user, setUser] = useState(firebaseEnabled ? null : { displayName: 'Demo User' });
  const [token, setToken] = useState(null);
  const [transactions, setTransactions] = useState([]);
  const [summary, setSummary] = useState({ income: 0, expenses: 0, balance: 0 });
  const [form, setForm] = useState(blank);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!auth) return;
    return auth.onAuthStateChanged(async current => {
      setUser(current);
      setToken(current ? await current.getIdToken() : null);
    });
  }, []);

  const load = async () => {
    if (!user) return;
    const [items, totals] = await Promise.all([api.listTransactions(token), api.summary(token)]);
    setTransactions(items);
    setSummary(totals);
  };

  useEffect(() => { load().catch(err => setError(err.message)); }, [user, token]);

  const byCategory = useMemo(() => transactions.reduce((acc, item) => {
    const value = item.type === 'EXPENSE' ? Number(item.amount) : 0;
    acc[item.category] = (acc[item.category] || 0) + value;
    return acc;
  }, {}), [transactions]);

  async function submit(event) {
    event.preventDefault();
    setError('');
    await api.createTransaction({ ...form, amount: Number(form.amount) }, token);
    setForm(blank);
    await load();
  }

  async function remove(id) {
    await api.deleteTransaction(id, token);
    await load();
  }

  if (!user) {
    return <main className="login"><h1>Finance Tracker</h1><p>Sign in to manage your budget securely.</p><button onClick={loginWithGoogle}>Continue with Google</button></main>;
  }

  return <main className="app">
    <header><div><h1><DollarSign /> Finance Tracker</h1><p>Track income, expenses, and spending categories.</p></div>{firebaseEnabled && <button className="secondary" onClick={logout}><LogOut size={16}/> Sign out</button>}</header>
    {error && <div className="error">{error}</div>}
    <section className="cards"><Card label="Income" value={summary.income}/><Card label="Expenses" value={summary.expenses}/><Card label="Balance" value={summary.balance}/></section>
    <section className="grid">
      <form onSubmit={submit} className="panel">
        <h2><Plus size={18}/> Add transaction</h2>
        <input placeholder="Title" value={form.title} onChange={e => setForm({...form, title: e.target.value})} required />
        <div className="row"><input placeholder="Category" value={form.category} onChange={e => setForm({...form, category: e.target.value})} required /><input type="number" min="0.01" step="0.01" placeholder="Amount" value={form.amount} onChange={e => setForm({...form, amount: e.target.value})} required /></div>
        <div className="row"><select value={form.type} onChange={e => setForm({...form, type: e.target.value})}><option>EXPENSE</option><option>INCOME</option></select><input type="date" value={form.transactionDate} onChange={e => setForm({...form, transactionDate: e.target.value})} required /></div>
        <textarea placeholder="Notes" value={form.notes} onChange={e => setForm({...form, notes: e.target.value})} />
        <button>Add transaction</button>
      </form>
      <div className="panel"><h2>Expense categories</h2>{Object.entries(byCategory).map(([name, value]) => <p className="category" key={name}><span>{name}</span><strong>${value.toFixed(2)}</strong></p>)}</div>
    </section>
    <section className="panel"><h2>Recent transactions</h2><div className="list">{transactions.map(item => <article key={item.id} className="transaction"><div><strong>{item.title}</strong><p>{item.category} • {item.transactionDate}</p></div><span className={item.type.toLowerCase()}>{item.type === 'INCOME' ? '+' : '-'}${Number(item.amount).toFixed(2)}</span><button className="icon" onClick={() => remove(item.id)}><Trash2 size={16}/></button></article>)}</div></section>
  </main>;
}

function Card({ label, value }) { return <div className="card"><span>{label}</span><strong>${Number(value).toFixed(2)}</strong></div>; }

createRoot(document.getElementById('root')).render(<App />);
