import React, { useEffect, useState } from 'react';
import api from '../../client/api';

const Transfers: React.FC = () => {
    const [transfers, setTransfers] = useState<any[]>([]);

    useEffect(() => {
        const fetchTransfers = async () => {
            try {
                const response = await api.get('/transfer/list');
                setTransfers(response.data);
            } catch (error) {
                console.error(error);
                alert('Failed to fetch transfers.');
            }
        };
        fetchTransfers();
    }, []);

    return (
        <div>
            <h2>Transfer History</h2>
            {transfers.length === 0 ? (
                <p>No transfers found.</p>
            ) : (
                <ul>
                    {transfers.map((transfer, index) => (
                        <li key={index}>
                            {transfer.senderIban} → {transfer.receiverIban}: ${transfer.amount}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default Transfers;
