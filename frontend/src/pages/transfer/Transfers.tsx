import React, {useEffect, useState} from 'react';
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
        <div className="flex flex-col items-center w-screen rounded-lg">
            <h2 className=" absolute top-0 pt-20 text-xl font-extrabold">Transfer History</h2>
            {transfers.length === 0 ? (
                <p>No transfers found.</p>
            ) : (
                <ul className="gap-6 flex flex-col justify-center w-1/2 mt-20 pt-20">
                    {transfers.map((transfer, index) => (
                        <li key={index} className="rounded-3xl bg-violet-400 p-12 flex flex-col justify-center items-center">
                            <span>{new Date(transfer.time).toLocaleString()}</span>
                            <span className="text-black ">Receiver: {transfer.receiverIban}</span>
                            <span>Sent ${transfer.amount}</span>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default Transfers;
