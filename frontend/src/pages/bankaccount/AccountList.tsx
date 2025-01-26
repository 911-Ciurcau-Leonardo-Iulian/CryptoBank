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
        <div className="flex flex-col mt-20 items-center w-screen rounded-lg">
            <div className="flex flex-row items-center gap-10 pb-6">
                <h2 className="text-xl font-extrabold text-green-100">Bank Accounts 💰</h2>
                <button className="hover:bg-gradient-to-b from-green-50 to-green-300 hover:text-black" onClick={handleCreateAccount}>Create New Account</button>
            </div>

            {accounts.length === 0 ? (
                <p>No accounts found.</p>
            ) : (
                <ul className="gap-6 flex flex-col justify-center items-center">
                    {accounts.map((account) => (
                        <li  className="rounded-3xl bg-violet-400 p-12 flex flex-col justify-center items-center" key={account.iban}>
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
