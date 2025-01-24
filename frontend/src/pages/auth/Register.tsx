import React, { useState } from 'react';
import api from '../../client/api';

const Register: React.FC = () => {
    const [formData, setFormData] = useState({
        email: '',
        password: '',
        confirmedPassword: '',
        firstName: '',
        lastName: '',
        phoneNumber: '',
        address: '',
    });

    const handleRegister = async () => {
        try {
            await api.post('/user/register', formData);
            alert('Registration successful!');
        } catch (error) {
            console.error(error);
            alert('Registration failed.');
        }
    };

    return (
        <div>
            <h2>Register</h2>
            <form onSubmit={(e) => { e.preventDefault(); handleRegister(); }}>
                {Object.keys(formData).map((key) => (
                    <input
                        key={key}
                        type="text"
                        placeholder={key}
                        value={(formData as any)[key]}
                        onChange={(e) => setFormData({ ...formData, [key]: e.target.value })}
                    />
                ))}
                <button type="submit">Register</button>
            </form>
        </div>
    );
};

export default Register;
