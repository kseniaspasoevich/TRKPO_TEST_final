import * as React from "react";
import {
    CreateProps,
    Datagrid,
    Edit,
    EditProps,
    List,
    ListProps,
    TextField,
    SimpleForm,
    Create,
    TextInput,
    SaveButton,
    EditButton,
    SelectInput,
    SelectField
} from "react-admin";

export const UserList = (props: ListProps) => (
    <List {...props}>
        <Datagrid>
            <TextField source ="id" />
            <TextField source ="login" />
            <TextField source ="password" />
            <SelectField source="role" choices={[
                { id: 'ADMIN', name: 'ADMIN' },
                { id: 'USER', name: 'USER' },
            ]} />
            <EditButton />
        </Datagrid>
    </List>
)

export const UserCreate = (props: CreateProps) => (
    <Create {...props}>
        <SimpleForm>
            <TextInput source ="login" required/>
            <TextInput source ="password" required/>
            <SelectInput source="role" choices={[
                { id: 'ADMIN', name: 'ADMIN' },
                { id: 'USER', name: 'USER' },
            ]} />
        </SimpleForm>
    </Create>
)

export const UserEdit = (props: EditProps) => (
    <Edit {...props}>
        <SimpleForm>
            <SelectInput source="role" choices={[
                { id: 'ADMIN', name: 'ADMIN' },
                { id: 'USER', name: 'USER' },
            ]} />
        </SimpleForm>
    </Edit>
)
