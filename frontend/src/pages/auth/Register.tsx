import React, { useState } from 'react';
import api from '../../client/api';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEye, faEyeSlash} from "@fortawesome/free-solid-svg-icons";

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

    // Separate state for the visibility of each password field
    const [passwordVisible, setPasswordVisible] = useState(false);
    const [confirmedPasswordVisible, setConfirmedPasswordVisible] = useState(false);

    const handleRegister = async () => {
        try {
            await api.post('/user/register', formData);
            alert('Registration successful!');
        } catch (error) {
            console.error(error);
            alert('Registration failed.');
        }
    };

    const togglePasswordVisibility = () => {
        setPasswordVisible(prev => !prev);
    };

    const toggleConfirmedPasswordVisibility = () => {
        setConfirmedPasswordVisible(prev => !prev);
    };

    return (
        <div className="mt-20 flex flex-col items-center justify-center h-full">
            <form className="grid grid-cols-2 items-center justify-center h-full gap-6" onSubmit={(e) => { e.preventDefault(); handleRegister(); }}>
                {Object.keys(formData).map((key) => (
                    key === 'password' ? (
                        <div key={key} className="relative">
                            <input
                                className="p-4 bg-violet-300 text-black rounded-3xl"
                                type={passwordVisible ? 'text' : 'password'}
                                placeholder={key}
                                value={(formData as any)[key]}
                                onChange={(e) => setFormData({ ...formData, [key]: e.target.value })}
                            />
                            <button
                                type="button"
                                className="absolute right-2 top-1/2 transform -translate-y-1/2 text-gray-500"
                                onClick={togglePasswordVisibility}
                            >
                                {!passwordVisible ? <FontAwesomeIcon className="text-white" icon={faEyeSlash} /> : <FontAwesomeIcon className="text-white" icon={faEye} />}
                            </button>
                        </div>
                    ) : key === 'confirmedPassword' ? (
                        <div key={key} className="relative">
                            <input
                                className="p-4 bg-violet-300 text-black rounded-3xl"
                                type={confirmedPasswordVisible ? 'text' : 'password'}
                                placeholder={key}
                                value={(formData as any)[key]}
                                onChange={(e) => setFormData({ ...formData, [key]: e.target.value })}
                            />
                            <button
                                type="button"
                                className="p-0 absolute right-2 top-1/2 transform -translate-y-1/2 text-gray-500"
                                onClick={toggleConfirmedPasswordVisibility}
                            >
                                {!confirmedPasswordVisible? <FontAwesomeIcon className="text-white" icon={faEyeSlash} /> : <FontAwesomeIcon className="text-white" icon={faEye} />}
                            </button>
                        </div>
                    ) : (
                        <input
                            className="p-4 bg-violet-300 text-black rounded-3xl"
                            key={key}
                            type="text"
                            placeholder={key}
                            value={(formData as any)[key]}
                            onChange={(e) => setFormData({ ...formData, [key]: e.target.value })}
                        />
                    )
                ))}
                <button className="hover:bg-gradient-to-b from-violet-50 to-violet-300 hover:text-black" type="submit">
                    Register
                </button>
            </form>
        </div>
    );
};

export default Register;
