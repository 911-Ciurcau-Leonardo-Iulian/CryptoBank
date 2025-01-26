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
        <div className="mt-20 flex flex-col items-center justify-center h-full" >
            <form className="grid grid-cols-2 items-center justify-center h-full gap-6" onSubmit={(e) => { e.preventDefault(); handleRegister(); }}>
                {Object.keys(formData).map((key) => (
                    <input className="p-4 bg-violet-300 text-black rounded-3xl"
                        key={key}
                        type="text"
                        placeholder={key}
                        value={(formData as any)[key]}
                        onChange={(e) => setFormData({ ...formData, [key]: e.target.value })}
                    />
                ))}
                <button  className="hover:bg-gradient-to-b from-violet-50 to-violet-300 hover:text-black" type="submit">Register</button>
            </form>
        </div>
    );
};

export default Register;
