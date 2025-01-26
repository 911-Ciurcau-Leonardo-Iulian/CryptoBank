import React, { useEffect, useState } from 'react';
import api from '../../client/api';
import { sha256 } from 'crypto-hash';
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
            return encryptRsa.encrypt({
                text: hash,
                privateKey: '-----BEGIN PRIVATE KEY-----\n' + privateKey + '\n-----END PRIVATE KEY-----',
            });
        } catch (error) {
            console.error('Error generating RSA signature:', error);
            throw new Error('Failed to generate the RSA signature.');
        }
    };

    return (
        <div className="flex flex-col items-center justify-center p-6 bg-violet-400 rounded-lg mt-10">
            <h2 className="text-3xl font-semibold text-gray-800 mb-6">New Transfer</h2>
            <form
                onSubmit={(e) => {
                    e.preventDefault();
                    handleTransfer();
                }}
                className=""
            >
                {/* Sender IBAN Dropdown */}
                <div>
                    <label className="text-lg font-medium text-gray-700">Sender IBAN:</label>
                    <select
                        value={senderIban}
                        onChange={(e) => setSenderIban(e.target.value)}
                        className="w-full p-3 mt-2 border rounded-lg border-gray-300 focus:outline-none focus:ring-2 focus:ring-green-500"
                    >
                        {accounts.map((account) => (
                            <option className="text-black" key={account.iban} value={account.iban}>
                                {account.iban} (Balance: ${account.amount})
                            </option>
                        ))}
                    </select>
                </div>

                {/* Receiver IBAN Input */}
                <div>
                    <label className="text-lg font-medium text-gray-700">Receiver IBAN:</label>
                    <input
                        type="text"
                        placeholder="Receiver IBAN"
                        value={receiverIban}
                        onChange={(e) => setReceiverIban(e.target.value)}
                        className="w-full p-3 mt-2 border rounded-lg border-gray-300 focus:outline-none focus:ring-2 focus:ring-green-500"
                    />
                </div>

                {/* Amount Input */}
                <div>
                    <label className="text-lg font-medium text-gray-700">Amount:</label>
                    <input
                        type="number"
                        placeholder="Amount"
                        value={amount}
                        onChange={(e) => setAmount(Number(e.target.value))}
                        className="w-full p-3 mt-2 border rounded-lg border-gray-300 focus:outline-none focus:ring-2 focus:ring-green-500"
                    />
                </div>

                {/* Transfer Button */}
                <div>
                    <button
                        type="submit"
                        className="w-full py-3 mt-4 bg-gradient-to-b from-green-500 to-green-300 text-white font-semibold rounded-lg shadow-md hover:bg-green-400 transition duration-200"
                    >
                        Transfer
                    </button>
                </div>
            </form>
        </div>
    );
};

export default NewTransfer;
