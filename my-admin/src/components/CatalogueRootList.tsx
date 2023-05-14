import * as React from "react";
import {Datagrid, Edit, EditProps, List, ListProps, TextField} from "react-admin";

export const CatalogueRootList = (props: ListProps) => (
    <List {...props}>
        <Datagrid>
            <TextField source ="id" />
            <TextField source ="parentId" />
            <TextField source ="createdTime" />
            <TextField source ="userCreatedById" />
            <TextField source ="name" />
            <TextField source ="typeOfFile" />
        </Datagrid>
    </List>
)

