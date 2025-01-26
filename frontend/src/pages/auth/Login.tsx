import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../../client/auth';

interface LoginProps {
    setIsLoggedIn: React.Dispatch<React.SetStateAction<boolean>>;
}

const Login: React.FC<LoginProps> = ({ setIsLoggedIn }) => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleLogin = async () => {
        try {
            await login(email, password);
            setIsLoggedIn(true);
            navigate('/account');
        } catch (error) {
            console.error(error);
            alert('Login failed.');
        }
    };

    return (
        <div className="flex flex-col items-center justify-center h-full">
            <form className="flex flex-col items-center justify-center h-full gap-6" onSubmit={(e) => { e.preventDefault(); handleLogin(); }}>
                <input className="p-4 bg-violet-300 text-black rounded-3xl"
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                <input className="p-4 bg-violet-300 text-black rounded-3xl"
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <button className="hover:bg-gradient-to-b from-violet-50 to-violet-300 hover:text-black" type="submit">Login</button>
            </form>
        </div>
    );
};

export default Login;
