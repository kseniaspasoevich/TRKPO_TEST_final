// const authProvider = {
//     // send username and password to the auth server and get back credentials
//     login: params => Promise.resolve(),
//     // when the dataProvider returns an error, check if this is an authentication error
//     checkError: error => Promise.resolve(),
//     // when the user navigates, make sure that their credentials are still valid
//     checkAuth: params => Promise.resolve(),
//     // remove local credentials and notify the auth server that the user logged out
//     logout: () => Promise.resolve(),
//     // get the user's profile
//     getIdentity: () => Promise.resolve(),
//     // get the user permissions (optional)
//     getPermissions: () => Promise.resolve(),
// };

// import { AuthProvider } from 'react-admin';
//
// const authProvider: AuthProvider = {
//     login: ({ username }) => {
//         localStorage.setItem('username', username);
//         // accept all username/password combinations
//         return Promise.resolve();
//     },
//     logout: () => {
//         localStorage.removeItem('username');
//         return Promise.resolve();
//     },
//     checkError: () => Promise.resolve(),
//     checkAuth: () =>
//         localStorage.getItem('username') ? Promise.resolve() : Promise.reject(),
//     getPermissions: () => Promise.reject('Unknown method'),
//     getIdentity: () =>
//         Promise.resolve({
//             id: 'user',
//             fullName: 'Jane Doe'
//             }),
// };
//
// export default authProvider;



import {AUTH_CHECK, AUTH_LOGIN} from 'react-admin';

export default (type, params) => {
    if (type === AUTH_LOGIN) {
        const { username, password } = params;
        const request = new Request('http://localhost:8080/user/login', {
            method: 'POST',
            body: username,
            headers: new Headers({ 'Content-Type': 'application/json' }),
        })
        return fetch(request)
            .then(response => {
                if (response.status < 200 || response.status >= 300) {
                    throw new Error(response.statusText);
                }
                return response.json();
            })
            .then(({ token }) => {
                localStorage.setItem('token', token);
            });
    }
    if (type === AUTH_CHECK) {
        return localStorage.getItem('token') ? Promise.resolve() : Promise.reject();
    }
    return Promise.resolve();
}