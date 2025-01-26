import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Link, Navigate } from 'react-router-dom';
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import AccountList from './pages/bankaccount/AccountList';
import NewTransfer from './pages/transfer/NewTransfer';
import TransferList from './pages/transfer/Transfers';

const App: React.FC = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem('jwt'));

    const handleLogout = () => {
        localStorage.clear();
        setIsLoggedIn(false);
    };

    return (
        <Router>
            <nav className="absolute top-0  pt-2 flex flex-row w-screen justify-center items-center gap-10">
                {!isLoggedIn && <Link className="rounded-3xl text-slate-50 hover:text-black hover:bg-gradient-to-b from-violet-100 to-violet-300 p-4" to="/login">Login</Link>}
                {!isLoggedIn && <Link className="rounded-3xl text-slate-50 hover:text-black hover:bg-gradient-to-b from-violet-100 to-violet-300 p-4" to="/register">Register</Link>}
                {isLoggedIn && <Link className="rounded-3xl text-slate-50 hover:text-black hover:bg-gradient-to-b from-violet-100 to-violet-300 p-4" to="/account">Accounts</Link>}
                {isLoggedIn && <Link className="rounded-3xl text-slate-50 hover:text-black hover:bg-gradient-to-b from-violet-100 to-violet-300 p-4" to="/transfer/new">New Transfer</Link>}
                {isLoggedIn && <Link className="rounded-3xl text-slate-50  hover:text-black hover:bg-gradient-to-b from-violet-100 to-violet-300 p-4" to="/transfer/list">Transfer History</Link>}
                {isLoggedIn && <button className="rounded-3xl text-slate-50 hover:text-black hover:bg-gradient-to-b from-red-100 to-red-300 p-4" onClick={handleLogout}>Logout</button>}
            </nav>
            <Routes>
                <Route path="/login" element={<Login setIsLoggedIn={setIsLoggedIn} />} />
                <Route path="/register" element={<Register />} />
                <Route path="/account" element={isLoggedIn ? <AccountList /> : <Navigate to="/login" />} />
                <Route path="/transfer/new" element={isLoggedIn ? <NewTransfer /> : <Navigate to="/login" />} />
                <Route path="/transfer/list" element={isLoggedIn ? <TransferList /> : <Navigate to="/login" />} />
            </Routes>
        </Router>
    );
};

export default App;
