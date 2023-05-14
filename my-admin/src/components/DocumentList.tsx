import * as React from 'react';
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
    EditButton,
    ArrayInput,
    DateField,
    NumberField,
    SelectField,
    NumberInput,
    SimpleFormIterator,
    ArrayField
} from 'react-admin';

export const DocumentList = (props: ListProps) => (
    <List {...props}>
        <Datagrid>
            <TextField source="documentType" />
            <TextField source="priority" />
            <NumberField source="concreteDocument.id" />
            <TextField source="concreteDocument.name" />
            <TextField source="concreteDocument.description" />
            <TextField source="concreteDocument.version" />
            <DateField source="concreteDocument.modifiedTime" showTime />
            <TextField source="concreteDocument.userModifiedBy" />
            <NumberField source="concreteDocument.parentDocumentId" />
            <ArrayField source="concreteDocument.data" >
                <Datagrid >
                    <NumberField source="id" />
                    <TextField source="name" />
                    <NumberField source="size" />
                    <TextField source="path" />
                    <NumberField source="parentConcreteDocumentId" />
                    <DateField source="createdTime" showTime />
                </Datagrid>
            </ArrayField>
            <EditButton />
        </Datagrid>
    </List>
);

export const DocumentCreate = (props: CreateProps) => (
    <Create {...props}>
        <SimpleForm>
            <NumberInput source="id" />
            <NumberInput source="parentId" />
            <TextInput source="documentType" required />
            <TextInput source="priority" required />
            <NumberInput source="concreteDocument.id" />
            <TextInput source="concreteDocument.name" required />
            <TextInput source="concreteDocument.description" required />
            {/*<TextInput source="concreteDocument.version" />*/}
            {/*<TextInput source="concreteDocument.modifiedTime" />*/}
            <TextInput source="concreteDocument.userModifiedBy" />
            {/*<TextInput source="concreteDocument.parentDocumentId" />*/}
            <ArrayInput source="concreteDocument.data" required >
                <SimpleFormIterator inline>
                    {/*<NumberInput source="id" />*/}
                    <TextInput source="name" />
                    <NumberInput source="size" />
                    <TextInput source="path" />
                    {/*<NumberInput source="parentConcreteDocumentId" />*/}
                    {/*<DateField source="createdTime" showTime />*/}
                </SimpleFormIterator>
            </ArrayInput>
        </SimpleForm>
    </Create>
);

export const DocumentEdit = (props: EditProps) => (
    <Edit {...props}>
        <SimpleForm>
            <TextInput source="priority" />
            <TextInput source="concreteDocument.name" />
            <TextInput source="concreteDocument.description" />
            <ArrayInput source="concreteDocument.data" required >
                <SimpleFormIterator inline>
                    <TextInput source="name" />
                    <NumberInput source="size" />
                    <TextInput source="path" />
                    <NumberInput source="parentConcreteDocumentId" />
                    <DateField source="createdTime" showTime />
                </SimpleFormIterator>
            </ArrayInput>
        </SimpleForm>
    </Edit>
);