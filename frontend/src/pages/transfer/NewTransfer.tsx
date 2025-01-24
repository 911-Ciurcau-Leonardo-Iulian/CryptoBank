import React, { useState } from 'react';
import api from '../../client/api';

const NewTransfer: React.FC = () => {
    const [receiverIban, setReceiverIban] = useState('');
    const [amount, setAmount] = useState<number>(0);

    const handleTransfer = async () => {
        try {
            const transferRequest = { senderIban: '', receiverIban, amount };
            const privateKey = localStorage.getItem('privateKey');

            if (!privateKey) throw new Error('Private key not found.');

            // Simplified signing logic
            const hash = btoa(JSON.stringify(transferRequest));
            const signature = btoa(hash + privateKey);

            await api.post('/transfer/transfer-amount', {
                transferRequest,
                signature,
            });

            alert('Transfer successful!');
        } catch (error) {
            console.error(error);
            alert('Transfer failed.');
        }
    };

    return (
        <div>
            <h2>New Transfer</h2>
            <form onSubmit={(e) => { e.preventDefault(); handleTransfer(); }}>
                <input
                    type="text"
                    placeholder="Receiver IBAN"
                    value={receiverIban}
                    onChange={(e) => setReceiverIban(e.target.value)}
                />
                <input
                    type="number"
                    placeholder="Amount"
                    value={amount}
                    onChange={(e) => setAmount(Number(e.target.value))}
                />
                <button type="submit">Transfer</button>
            </form>
        </div>
    );
};

export default NewTransfer;
