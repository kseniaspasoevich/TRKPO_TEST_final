// in src/MyLoginPage.js
import * as React from 'react';
import { useState } from 'react';
import { useLogin, useNotify, Notification } from 'react-admin';

const MyLoginPage = ({ theme }) => {
    const [login, setLogin] = useState('');
    const [password, setPassword] = useState('');
    const userLogin = useLogin();
    const notify = useNotify();

    const handleSubmit = e => {
        e.preventDefault();
        userLogin({ login, password }).catch(() =>
            notify('Invalid login or password')
        );
    };

    return (
        <form onSubmit={handleSubmit}>
        <input
            name="login"
    type="login"
    value={login}
    onChange={e => setLogin(e.target.value)}
    />
    <input
    name="password"
    type="password"
    value={password}
    onChange={e => setPassword(e.target.value)}
    />
    </form>
);
};

export default MyLoginPage;