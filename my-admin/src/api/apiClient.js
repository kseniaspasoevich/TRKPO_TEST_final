import {fetchUtils} from "react-admin";

const apiUrl = function() {
    return 'http://localhost:8080';
}()
// const httpClient = fetchUtils.fetchJson;

const httpClient = (url, options = {}) => {
    if (!options.headers) {
        options.headers = new Headers({ Accept: 'application/json' });
    }
    const token = localStorage.getItem('token');
    options.headers.set('Authorization', `Bearer ${token}`);
    return fetchUtils.fetchJson(url, options);
}

const apiClient = (path, params) => {
    if (path.startsWith("/")) {
        path = path.substring(1)
    }
    return httpClient(`${apiUrl}/${path}`, params);
}

export default apiClient;