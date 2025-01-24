import React, { useEffect, useState } from 'react';
import api from '../../client/api';

const AccountDetails: React.FC = () => {
    const [account, setAccount] = useState<{ iban: string; amount: number } | null>(null);

    useEffect(() => {
        const fetchAccount = async () => {
            try {
                const response = await api.get('/bank-account/list');
                setAccount(response.data[0]);
            } catch (error) {
                console.error(error);
                alert('Failed to fetch account details.');
            }
        };
        fetchAccount();
    }, []);

    if (!account) return <p>Loading account details...</p>;

    return (
        <div>
            <h2>Bank Account</h2>
            <p>IBAN: {account.iban}</p>
            <p>Balance: ${account.amount}</p>
        </div>
    );
};

export default AccountDetails;
