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
        <div>
            <h2>Login</h2>
            <form onSubmit={(e) => { e.preventDefault(); handleLogin(); }}>
                <input
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />
                <button type="submit">Login</button>
            </form>
        </div>
    );
};

export default Login;
