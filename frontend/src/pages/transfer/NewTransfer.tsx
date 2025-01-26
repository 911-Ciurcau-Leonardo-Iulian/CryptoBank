import React, {useEffect, useState} from 'react';
import api from '../../client/api';
import {sha256} from 'crypto-hash';
import EncryptRSA from 'encrypt-rsa';

interface BankAccount {
    iban: string;
    amount: number;
}

const NewTransfer: React.FC = () => {
    const [accounts, setAccounts] = useState<BankAccount[]>([]);
    const [senderIban, setSenderIban] = useState('');
    const [receiverIban, setReceiverIban] = useState('');
    const [amount, setAmount] = useState<number>(0);
    const encryptRsa = new EncryptRSA();

    useEffect(() => {
        const fetchAccounts = async () => {
            try {
                const response = await api.get('/bank-account/list');
                setAccounts(response.data);
                if (response.data.length > 0) setSenderIban(response.data[0].iban); // Default to the first account
            } catch (error) {
                console.error(error);
                alert('Failed to fetch accounts.');
            }
        };

        fetchAccounts();
    }, []);

    const handleTransfer = async () => {
        try {
            if (!senderIban || !receiverIban || amount <= 0) {
                alert('Please provide valid transfer details.');
                return;
            }

            const transferRequest = {
                senderIban,
                receiverIban,
                amount,
            };

            const privateKey = localStorage.getItem('privateKey'); // User's private key from login
            if (!privateKey) {
                alert('Private key not found. Please log in again.');
                return;
            }

            // Step 1: Remove all whitespaces from the JSON string
            const transferRequestString = JSON.stringify(transferRequest).replace(/\s+/g, '');

            // Step 2: Hash the string using SHA-256
            const hash = await sha256(transferRequestString);

            // Step 3: Encrypt the hash using RSA Private Key
            const signature = signWithPrivateKey(hash, privateKey);

            // Send the transfer request with the signature
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

    const signWithPrivateKey = (hash: string, privateKey: string): string => {
        try {
            // Correctly use `sign` to sign the hash with the private key
            return encryptRsa.encrypt({text: hash, privateKey: '-----BEGIN PRIVATE KEY-----\n'+privateKey+'\n-----END PRIVATE KEY-----'});
        } catch (error) {
            console.error('Error generating RSA signature:', error);
            throw new Error('Failed to generate the RSA signature.');
        }
    };

    return (
        <div>
            <h2>New Transfer</h2>
            <form
                onSubmit={(e) => {
                    e.preventDefault();
                    handleTransfer();
                }}
            >
                {/* Sender IBAN Dropdown */}
                <div>
                    <label>Sender IBAN:</label>
                    <select value={senderIban} onChange={(e) => setSenderIban(e.target.value)}>
                        {accounts.map((account) => (
                            <option key={account.iban} value={account.iban}>
                                {account.iban} (Balance: ${account.amount})
                            </option>
                        ))}
                    </select>
                </div>

                {/* Receiver IBAN Input */}
                <div>
                    <label>Receiver IBAN:</label>
                    <input
                        type="text"
                        placeholder="Receiver IBAN"
                        value={receiverIban}
                        onChange={(e) => setReceiverIban(e.target.value)}
                    />
                </div>

                {/* Amount Input */}
                <div>
                    <label>Amount:</label>
                    <input
                        type="number"
                        placeholder="Amount"
                        value={amount}
                        onChange={(e) => setAmount(Number(e.target.value))}
                    />
                </div>

                <button type="submit">Transfer</button>
            </form>
        </div>
    );
};

export default NewTransfer;
