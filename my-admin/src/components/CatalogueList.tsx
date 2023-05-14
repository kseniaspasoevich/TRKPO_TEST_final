import * as React from 'react';
import { CreateProps, Datagrid, Edit, EditProps, List, ListProps, TextField, SimpleForm, Create, TextInput, EditButton, ArrayInput, DateField, NumberField, SelectField, NumberInput } from 'react-admin';

export const CatalogueList = (props: ListProps) => (
    <List {...props}>
        <Datagrid>
            <NumberField source="id" />
            <NumberField source="parentId" />
            <DateField source="createdTime" showTime />
            <TextField source="userCreatedById" />
            <TextField source="name" />
            <TextField source="typeOfFile" />
            <EditButton />
        </Datagrid>
    </List>
);

export const CatalogueListCreate = (props: CreateProps) => (
    <Create {...props}>
        <SimpleForm>
            <TextInput source="name" required />
            <NumberInput source="parentId" />
            {/*<DateField source="createdTime" showTime />*/}
            <NumberInput source="userCreatedById" />
            <SelectField source="typeOfFile" choices={[{ id: 'CATALOGUE', name: 'CATALOGUE' }]} />
        </SimpleForm>
    </Create>
);

export const CatalogueListEdit = (props: EditProps) => (
    <Edit {...props}>
        <SimpleForm>
            <TextInput source="name" />
        </SimpleForm>
    </Edit>
);
