import api from "./api.ts";

export const login = async (email: string, password: string) => {
    const response = await api.post('/user/login', { email, password });
    localStorage.setItem('jwt', response.data.accessToken);
    localStorage.setItem('privateKey', response.data.privateKey);
    return response.data;
};

export const logout = () => {
    localStorage.removeItem('jwt');
    localStorage.removeItem('privateKey');
};
