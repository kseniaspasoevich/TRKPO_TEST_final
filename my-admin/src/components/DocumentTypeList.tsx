import * as React from "react";
import {CreateProps, Datagrid, Edit, EditProps, List, ListProps, TextField, SimpleForm, Create, TextInput, SaveButton, EditButton} from "react-admin";

export const DocumentTypeList = (props: ListProps) => (
    <List {...props}>
        <Datagrid>
            <TextField source ="id" />
            <TextField source ="name" />
            <EditButton />
        </Datagrid>
    </List>
)

export const DocumentTypeCreate = (props: CreateProps) => (
    <Create {...props}>
        <SimpleForm>
            <TextInput source="name" required={true}/>
        </SimpleForm>
    </Create>
)

export const DocumentTypeEdit = (props: EditProps) => (
    <Edit {...props}>
        <SimpleForm>
            <TextInput source="name"/>
        </SimpleForm>
    </Edit>
)