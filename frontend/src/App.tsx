import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import AccountDetails from './pages/bankaccount/AccountDetails';
import NewTransfer from './pages/transfer/NewTransfer';
import Transfers from './pages/transfer/Transfers';

const App: React.FC = () => {
    return (
        <Router>
            <nav>
                <Link to="/login">Login</Link>
                <Link to="/register">Register</Link>
                <Link to="/account">Account</Link>
                <Link to="/transfer/new">New Transfer</Link>
                <Link to="/transfer/list">Transfer History</Link>
            </nav>
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/account" element={<AccountDetails />} />
                <Route path="/transfer/new" element={<NewTransfer />} />
                <Route path="/transfer/list" element={<Transfers />} />
            </Routes>
        </Router>
    );
};

export default App;
