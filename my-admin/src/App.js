import { Admin, Resource } from 'react-admin';
import {CatalogueRootList} from "./components/CatalogueRootList";
import {BrowserRouter, Route} from "react-router-dom";

import dataProvider from "./dataProvider";
import simpleRestProvider from 'ra-data-simple-rest';
import {DocumentTypeCreate, DocumentTypeEdit, DocumentTypeList} from "./components/DocumentTypeList";
import authProvider from "./authProvider";
import MyLoginPage from "./MyLoginPage";
import {CatalogueList, CatalogueListCreate, CatalogueListEdit} from "./components/CatalogueList";
import {UserCreate, UserEdit, UserList} from "./components/UserList";
import {DocumentCreate, DocumentEdit, DocumentList} from "./components/DocumentList";



const App = () => (
    <BrowserRouter>
        <Admin dataProvider={dataProvider} authProvider={authProvider}>
            <Resource name="catalogue/root" list={CatalogueRootList} />,
            <Resource name="type" list={DocumentTypeList} create={DocumentTypeCreate} edit={DocumentTypeEdit}/>,
            <Resource name="catalogue" list={CatalogueList} create={CatalogueListCreate} edit={CatalogueListEdit} />,
            <Resource name="user" list={UserList} create={UserCreate} edit={UserEdit} />,
            <Resource name="documents" list={DocumentList} create={DocumentCreate} edit={DocumentEdit} />,
        </Admin>
    </BrowserRouter>
);

export default App;

// import React, { useEffect, useState } from "react"
// import {Datagrid, List, TextField} from "react-admin";
//
// const App = () => {
//     // let config = {
//     //     headers: {
//     //         "Content-Type": "application/json",
//     //         'Access-Control-Allow-Origin': '*',
//     //         "Access-Control-Allow-Methods": 'DELETE, POST, GET, OPTIONS',
//     //         "Access-Control-Allow-Headers": "Content-Type, Authorization, X-Requested-With"
//     //     }
//     // }
//
//     const [users, setUsers] = useState()
//
//     async function fetchUserData() {
//         const response = await fetch("http://localhost:8080/catalogue/root");
//         const jsonData = await response.json();
//
//
//         // return jsonData
//         setUsers([jsonData])
//     }
//     useEffect(() => {
//         fetchUserData()
//     }, [])
//     console.log(users);
//     // fetchUserData()
//
//     // async function fetchUserData() {
//     //     const url = 'http://localhost:8080/catalogue/root';
//     //     const username = 'login';
//     //     const password = 'password';
//     //
//     //
//     //     fetch(url, {
//     //         headers: {
//     //             Authorization: 'Bearer ' + btoa(username + ':' + password),
//     //         },
//     //         method: 'GET',
//     //         mode: 'cors'
//     //     })
//     //         .then(response => response.json())
//     //         .then(data => console.log(data))
//     //         .catch(error => console.log(error));
//     //     console.log(users);
//     // }
//
//     //const response = await fetch("http://example.com/movies.json"); const jsonData = await response.json();
//
//
//
//     const User = ({cur_user}) => (
//         <div>
//             <li key={1}>{cur_user?.id}</li>
//             <li key={2}>{cur_user?.name}</li>
//             <li key={3}>{cur_user?.parentId || 'Nothing'}</li>
//             <li key={4}>{cur_user?.typeOfFile}</li>
//             <li key={5}>{cur_user?.userCreatedById || 'None'}</li>
//             <li key={6}>{cur_user?.createdTime}</li>
//         </div>
//     );
//
// // @ts-ignore
//     return (
//         <div>
//             <h1>Hello</h1>
//             {users?.length > 0 && (
//                 <ul>
//                     {users?.map(user => <User cur_user={user} />)}
//                 </ul>
//
//             )}
//         </div>
//         // <div>
//         //
//         //     {users?.length > 0 && (
//         //         <ul>
//         //             {users?.map(user => <User cur_user={user}> )}
//         //         </ul>
//         //     )
//         // </div>
//             );
// }
//
// export default App;