import React, { useEffect, useState } from 'react';
import api from '../../client/api';

interface BankAccount {
    iban: string;
    amount: number;
    creationTime: number;
}

const AccountList: React.FC = () => {
    const [accounts, setAccounts] = useState<BankAccount[]>([]);
    const [isLoading, setIsLoading] = useState(true);

    // Fetch accounts on component load
    useEffect(() => {
        fetchAccounts();
    }, []);

    const fetchAccounts = async () => {
        try {
            const response = await api.get('/bank-account/list');
            setAccounts(response.data);
        } catch (error) {
            console.error(error);
            alert('Failed to fetch accounts.');
        } finally {
            setIsLoading(false);
        }
    };

    // Create a new account
    const handleCreateAccount = async () => {
        try {
            const response = await api.post('/bank-account/create');
            alert(`New account created with IBAN: ${response.data.iban}`);
            fetchAccounts(); // Refresh accounts after creation
        } catch (error) {
            console.error(error);
            alert('Failed to create a new account.');
        }
    };

    if (isLoading) return <p>Loading accounts...</p>;

    return (
        <div>
            <h2>Your Bank Accounts</h2>

            {/* Create Account Section */}
            <button onClick={handleCreateAccount}>Create New Account</button>

            {/* List Accounts */}
            {accounts.length === 0 ? (
                <p>No accounts found.</p>
            ) : (
                <ul>
                    {accounts.map((account) => (
                        <li key={account.iban}>
                            <p>IBAN: {account.iban}</p>
                            <p>Balance: ${account.amount}</p>
                            <p>Created At: {new Date(account.creationTime).toLocaleString()}</p>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default AccountList;
