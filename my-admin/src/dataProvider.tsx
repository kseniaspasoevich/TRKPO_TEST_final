import {DeleteManyParams, GetListParams, GetManyReferenceParams} from 'react-admin';
import {stringify} from 'query-string';
import apiClient from "./api/apiClient";


export default {
    getList: (resource: string, params: GetListParams) => {
        const {page, perPage} = params.pagination;
        const {field, order} = params.sort;
        const query = {
            sort: JSON.stringify([field, order]),
            range: JSON.stringify([page - 1, perPage]),
            filter: JSON.stringify(params.filter),
        };
        const path = `${resource}?${stringify(query)}`;

        return apiClient(path).then(({headers, json}) => {
            const isArray = Array.isArray(json)
            const result = isArray ? json : [json]
            console.log(headers, json)
            console.log(headers.get('Content-Range'))
            headers.forEach(item => {
            });
            return {
                data: result,
                total: result?.length
                // parseInt(headers.get('Content-Range')!.split('/').pop()!, 10),

            };
        });
    },

    getOne: (resource: string, params: { id: any; }) =>
        apiClient(`${resource}/${params.id}`).then(({json}) => ({
            data: json,
        })),

    getMany: (resource: string, params: { ids: any; }) => {
        const query = {
            filter: JSON.stringify({id: params.ids}),
        };
        const url = `${resource}?${stringify(query)}`;
        return apiClient(url).then(({json}) => ({data: json}));
    },

    getManyReference: (resource: string, params: GetManyReferenceParams) => {
        const {page, perPage} = params.pagination;
        const {field, order} = params.sort;
        const query = {
            sort: JSON.stringify([field, order]),
            range: JSON.stringify([(page - 1) * perPage, page * perPage - 1]),
            filter: JSON.stringify({
                ...params.filter,
                [params.target]: params.id,
            }),
        };
        const url = `${resource}?${stringify(query)}`;

        return apiClient(url).then(({headers, json}) => ({
            data: json,
            total: parseInt(headers.get('content-range')!.split('/').pop()!, 10), // TODO: nullability
        }));
    },

    update: (resource: string, params: { id: any; data: any; }) =>
        apiClient(`${resource}/${params.id}`, {
            method: 'PUT',
            body: JSON.stringify(params.data),
        }).then(({json}) => ({data: json})),

    updateMany: (resource: string, params: { ids: any; data: any; }) => {
        const query = {
            filter: JSON.stringify({id: params.ids}),
        };
        return apiClient(`${resource}?${stringify(query)}`, {
            method: 'PUT',
            body: JSON.stringify(params.data),
        }).then(({json}) => ({data: json}));
    },

    create: (resource: string, params: { data: any; }) =>
        apiClient(`${resource}`, {
            method: 'POST',
            body: JSON.stringify(params.data),
        }).then(({json}) => ({
            data: {...json},
        })),

    delete: (resource: string, params: { id: any; }) =>
        apiClient(`${resource}/${params.id}`, {
            method: 'DELETE',
        }).then(({json}) => ({data: json})),

    deleteMany: (resource: string, params: DeleteManyParams) => {
        const query = {
            filter: JSON.stringify({id: params.ids}),
        };
        return apiClient(`${resource}?${stringify(query)}`, {
            method: 'DELETE',
        }).then(({json}) => ({data: json}));
    },
};