# DIGITAL DESIGN DOCUMENTS PROJECT

# Реализованные требования

1. приложение предоставляет rest api в формате json
2. приложение запаковываетя в исполняемый jar файл
3. предоставлено описание rest api в readme а так же через swagger.
4. Создание документов и версий документа.
5. Получение списка версий документа.
6. Сохранение каталогов.
7. Поиск объектов по родителю/названию/типу(каталог/документ). 
8. Paging.
9. Сохранение настроек(типы документов). 
10. Фильтрация документов по типу при поиске.
11. Проверка прав доступа при: получении/поиске объектов, сохранении объектов
12. Валидация объектов.
13. Учитывается сортировка по степени важности для документов при поиске.
14. Назначение прав доступа на каталоги.

# Запуск приложения

Перед запуском приложения необходимо изменить данные о базе данных в `application.properies` в app модуле в ресурсах. 

При первом запуске автоматически добавятся:

- Новый пользователь с параметрами "`login`"  "`password`" и ролью "`ADMIN`"
- Каталог "`root`"

# Тестирование

Все тесты для rest api находятся в `app` модуле, представляют собой интеграционные тесты выполненные с помощью `TestRestTemplate`.

# Описание схемы ролей и прав доступа.

**Admin -** имеет право просматривать и редактировать любой файл, так же только ADMIN может назначить другому пользователю роль ADMIN.

**User -** имеет право просматривать любой каталог и документ, если на данном документе/каталоге или любом из родительских каталогах нет ограничение на READ. 
Не имеет права по умолчанию редактировать/создавать документ/каталог в любом из каталогов, кроме случаев когда он является создателем данного документа/каталога и/или имеет права на READ_WRITE на данный документ/каталог либо на любой из родительских каталогов. 

Каждый документ/каталог имеет `set` пользователей на чтение, и `set` пользователей на чтение и запись. Если READ set пустой, значит данный документ/каталог не имеет ограничений на чтение, и просматривать его может любой (в случае если все родительские каталоги вплоть до root каталога так же имеют READ set пустым)
в противном случае файл имеет ограничения на чтение.

При каждом обращение к файлу проверяется соответствующие права доступа данного пользователя на файл:

Для успешного доступа на запись достаточно выполнения одного из следующих условий:

- Пользователь имеет роль ADMIN
- Пользователь является создателем данного документа/каталога
- Пользователь является создателем любого из родительских каталогов
- Пользователь присутствует в READWRITE set данного документа/каталога либо любого из родительских каталогов

Для успешного доступа на чтения достаточно выполнения одного из следующих условий:

- Пользователь удовлетворяет любому из условий "для записи"
- Пользователь присутствует в Read set данного документа/каталога либо любого из родительских каталогов
- Данный документ/каталог и каждый из родительских каталогов вплоть до root имеет пустой READ set

Изменять права доступа на файл может только пользователь удовлетворяющий условию READWRITE access либо имеющий роль ADMIN.

# Описание модулей

### APP

- точка входа
- тесты
- application.properties
- data.sql (данные необходимые при первом запуске приложения)

### DAO

- интерфейсы DAO.

### DTO

- DTO объекты

### JPA

- реализация интерфейсов DAO
- Entities

### SERVICE

- бизнес логика

### REST

- endpoints
- spring security
- swagger

# Описание сущностей.

### FileAbstract

Абстрактный класс, который расширяют сущности Catalogue и Document. Представляет общий набор полей присущий этим сущностям, в частности READ set и READ_WRITE set необходимые для управления правами доступа. Так же служит для полиморфных запросов. Содержит информацию о родительском каталоге, время создания, создателя и сэты.

### Catalogue

Расширяет FileAbstract, содержит имя и своих детей.

### Document

Расширяет FileAbstract, содержит указатель на тип документа, приоритет, указатель на самую свежую версию (ConcreteDocument), и список всех версий (включая самую свежую).

### ConcreteDocument

Представляет собой версию документа. Имеет в себе набор атрибутов изменяемых в документе (название, описание, файлы).  Так же содержит в себе версию (вычисляется автоматически при создании новой версии, начинается с 1 и увеличивается каждый раз на 1), пользователя создавшего новую версию, время создания версии, и указатель на документ которому пренадлежит.

### FilePath

Представляет собой информацию о файле, принадлежащем конкретной версии документа. Содержит в себя имя файла, путь, размер, время создания, ссылка на родительскую конкретную версию документа.

### User

Представляет собой сущность для хранения данных пользователя в базе данных, с базовыми параметрами - login, password, role, а так же с теми, что имплементируются от UserDetails.


# REST API DESCRIPTION

## ENDPOINTS

## `/catalogue`

> GET `/catalogue/{id}`

- returns catalogue by id if user has access to read it

RESPONSE EXAMPLE:

```json
{
"id": 63,
"parentId": 1,
"createdTime": "2021-05-15T18:54:11.625+00:00",
"userCreatedById": 1,
"name": "children_of_root",
"typeOfFile": "CATALOGUE"
}
```

> GET `/catalogue/root`

- returns root catalogue

RESPONSE EXAMPLE:

```json
{
"id": 1,
"parentId": null,
"createdTime": "2021-05-12T13:47:39.800+00:00",
"userCreatedById": null,
"name": "root",
"typeOfFile": "CATALOGUE"
}
```

> GET `/catalogue/open/{id}?type=...&name=...&documentType=...`

request with three non required parameters

- `type` - CATALOGUE/DOCUMENT (not case-sensitive)
- `name` - name of file, or substring of name of file (not case-sensitive)
- `documentType` - it shows only documents with specified type.

this request returns list of file and documents which are located in the catalogue.

RESPONSE EXAMPLE:

```json
[
  {
    "id": 63,
    "parentId": 1,
    "createdTime": "2021-05-15T18:54:11.625+00:00",
    "userCreatedById": 1,
    "name": "children_of_root",
    "typeOfFile": "CATALOGUE"
  },
  {
    "id": 24,
    "parentId": 1,
    "createdTime": "2021-05-13T20:43:10.005+00:00",
    "userCreatedById": 2,
    "name": "newDoc_mod2",
    "typeOfFile": "DOCUMENT",
    "documentType": "simplydoc",
    "priority": "LOW",
    "concreteDocument": null
  },
  {
    "id": 23,
    "parentId": 1,
    "createdTime": "2021-05-13T20:26:51.931+00:00",
    "userCreatedById": 2,
    "name": "newDoc_mod3",
    "typeOfFile": "DOCUMENT",
    "documentType": "simplydoc",
    "priority": "LOW",
    "concreteDocument": null
  }
]
```

> **POST** `/catalogue`

- saves new catalogue in specified catalogue if user has READWRITE access rights.

REQUEST BODY EXAMPLE:

```json
{
  "parentId": 63,
  "name": "children_of_63"
}
```

RESPONSE EXAMPLE:

```json
{
"id": 64,
"parentId": 63,
"createdTime": "2021-05-15T19:12:17.554+00:00",
"userCreatedById": 1,
"name": "children_of_63",
"typeOfFile": "CATALOGUE"
}
```

> **POST** `/catalogue/modify`

- modify catalogue if user has READWRITE access rights.

REQUEST BODY EXAMPLE:

```json
{
  "id": 64,
  "name": "children_of_63_mod"
}
```

RESPONSE EXAMPLE:

```json
{
"id": 64,
"parentId": 63,
"createdTime": "2021-05-15T19:12:17.554+00:00",
"userCreatedById": 1,
"name": "children_of_63_mod",
"typeOfFile": "CATALOGUE"
}
```

> **DELETE**`/catalogue/{id}`

- deletes catalogue by id if user has READWRITE access rights.

---

---

## `/documents`

> GET `/documents/{id}`

- returns last version of document by id if user has access to read it

RESPONSE EXAMPLE:

```json

 {
  "id": 12,
  "parentId": 1,
  "createdTime": "2021-05-12T14:31:15.888+00:00",
  "userCreatedById": 1,
  "name": "newDoc_mod2",
  "typeOfFile": "DOCUMENT",
  "documentType": "fax",
  "priority": "LOW",
  "concreteDocument": {
    "id": 19,
    "name": "newDoc_mod2",
    "description": "descr_mod2",
    "version": 3,
    "modifiedTime": "2021-05-13T20:01:56.020+00:00",
    "userModifiedBy": 2,
    "parentDocumentId": 12,
    "data": [
      {
        "id": 49,
        "name": "paaa",
        "size": 4,
        "path": "paaa",
        "parentConcreteDocumentId": null,
        "createdTime": "2021-05-13T20:01:56.022+00:00"
      },
      {
        "id": 50,
        "name": "ptthhhhh12",
        "size": 10,
        "path": "ptthhhhh12",
        "parentConcreteDocumentId": null,
        "createdTime": "2021-05-13T20:01:56.022+00:00"
      },
      {
        "id": 51,
        "name": "paa3",
        "size": 4,
        "path": "paa3",
        "parentConcreteDocumentId": null,
        "createdTime": "2021-05-13T20:01:56.022+00:00"
      }
    ]
  }
}
```

> GET `/documents/{id}/versions`

- returns all version of document by id if user has access to read it

RESPONSE EXAMPLE:

```json
[
  {
    "id": 11,
    "name": "newDoc",
    "description": "descr",
    "version": 1,
    "modifiedTime": "2021-05-12T14:31:15.888+00:00",
    "userModifiedBy": 1,
    "parentDocumentId": 12,
    "data": [
      {
        "id": 31,
        "name": "paaatthhhhh11",
        "size": 13,
        "path": "paaatthhhhh11",
        "parentConcreteDocumentId": null,
        "createdTime": "2021-05-12T14:31:15.903+00:00"
      },
      {
        "id": 32,
        "name": "paaatthhhhh12",
        "size": 13,
        "path": "paaatthhhhh12",
        "parentConcreteDocumentId": null,
        "createdTime": "2021-05-12T14:31:15.903+00:00"
      },
      {
        "id": 33,
        "name": "paaatthhhhh13",
        "size": 13,
        "path": "paaatthhhhh13",
        "parentConcreteDocumentId": null,
        "createdTime": "2021-05-12T14:31:15.903+00:00"
      }
    ]
  },
  {
    "id": 18,
    "name": "newDoc_mod1",
    "description": "descr_mod1",
    "version": 2,
    "modifiedTime": "2021-05-13T20:01:11.719+00:00",
    "userModifiedBy": 2,
    "parentDocumentId": 12,
    "data": [
      {
        "id": 46,
        "name": "paaatthhhhh11",
        "size": 13,
        "path": "paaatthhhhh11",
        "parentConcreteDocumentId": null,
        "createdTime": "2021-05-13T20:01:11.720+00:00"
      },
      {
        "id": 47,
        "name": "paaatthhhhh12",
        "size": 13,
        "path": "paaatthhhhh12",
        "parentConcreteDocumentId": null,
        "createdTime": "2021-05-13T20:01:11.720+00:00"
      },
      {
        "id": 48,
        "name": "paaatthhhhh13",
        "size": 13,
        "path": "paaatthhhhh13",
        "parentConcreteDocumentId": null,
        "createdTime": "2021-05-13T20:01:11.720+00:00"
      }
    ]
  },
  {
    "id": 19,
    "name": "newDoc_mod2",
    "description": "descr_mod2",
    "version": 3,
    "modifiedTime": "2021-05-13T20:01:56.020+00:00",
    "userModifiedBy": 2,
    "parentDocumentId": 12,
    "data": [
      {
        "id": 49,
        "name": "paaa",
        "size": 4,
        "path": "paaa",
        "parentConcreteDocumentId": null,
        "createdTime": "2021-05-13T20:01:56.022+00:00"
      },
      {
        "id": 50,
        "name": "ptthhhhh12",
        "size": 10,
        "path": "ptthhhhh12",
        "parentConcreteDocumentId": null,
        "createdTime": "2021-05-13T20:01:56.022+00:00"
      },
      {
        "id": 51,
        "name": "paa3",
        "size": 4,
        "path": "paa3",
        "parentConcreteDocumentId": null,
        "createdTime": "2021-05-13T20:01:56.022+00:00"
      }
    ]
  }
]
```

> **POST** `/documents`

- adds a new document and its first version.

REQUEST BODY EXAMPLE:

you have to specify

- parentId - parent catalogue id
- documentType - name of document type, not case-sensitive, if it isn't exists in document_type table, then there will be created a new record of your new document type, otherwise it will refer on existing document type
- priority (not required) - "LOW", "DEFAULT" "HIGH"
- concreteDocument - it is an initial version of document with parameters that can be changed in the future by creation of new versions
    - name
    - description
    - data (not required) - array of objects which represents inner files of that concrete document with parameters
        - path - file path
        - name (not required)
        - size (not required)

```json
{
  "parentId": 63,
  "documentType": "newfax",
  "priority": "HIGH",
  "concreteDocument": {
    "name": "newDoc_new",
    "description": "descr_new",
    "data": [
      {
        "path": "pa/a/a"
      },
      {
        "path": "pt/thhh/hh12"
      }
    ]
  }
}
```

RESPONSE EXAMPLE:

```json
{
  "id": 66,
  "parentId": 63,
  "createdTime": "2021-05-15T19:34:22.930+00:00",
  "userCreatedById": 1,
  "name": "newDoc_new",
  "typeOfFile": "DOCUMENT",
  "documentType": "newfax",
  "priority": "HIGH",
  "concreteDocument": {
    "id": 30,
    "name": "newDoc_new",
    "description": "descr_new",
    "version": 1,
    "modifiedTime": "2021-05-15T19:34:22.930+00:00",
    "userModifiedBy": 1,
    "parentDocumentId": 66,
    "data": [
      {
        "id": 82,
        "name": "pa/a/a",
        "size": 6,
        "path": "pa/a/a",
        "parentConcreteDocumentId": null,
        "createdTime": "2021-05-15T19:34:22.930+00:00"
      },
      {
        "id": 83,
        "name": "pt/thhh/hh12",
        "size": 12,
        "path": "pt/thhh/hh12",
        "parentConcreteDocumentId": null,
        "createdTime": "2021-05-15T19:34:22.930+00:00"
      }
    ]
  }
}
```

> **POST** `/documents/modify`

- adds new document version.

REQUEST BODY EXAMPLE:

```json
{
  "name": "mod_66",
  "description": "this is modified document",
  "parentDocumentId": 66,
  "data": [
    {
      "name": "new_file",
      "path": "file/1"
    },
    {
      "path": "file/2"
    },
    {
      "path": "file/3"
    }
  ]
}
```

RESPONSE EXAMPLE:

```json
{
  "id": 66,
  "parentId": 63,
  "createdTime": "2021-05-15T19:34:22.930+00:00",
  "userCreatedById": 1,
  "name": "mod_66",
  "typeOfFile": "DOCUMENT",
  "documentType": "newfax",
  "priority": "HIGH",
  "concreteDocument": {
    "id": 31,
    "name": "mod_66",
    "description": "this is modified document",
    "version": 2,
    "modifiedTime": "2021-05-15T20:00:09.993+00:00",
    "userModifiedBy": 1,
    "parentDocumentId": 66,
    "data": [
      {
        "id": 84,
        "name": "new_file",
        "size": 6,
        "path": "file/1",
        "parentConcreteDocumentId": null,
        "createdTime": "2021-05-15T20:00:09.993+00:00"
      },
      {
        "id": 85,
        "name": "file/2",
        "size": 6,
        "path": "file/2",
        "parentConcreteDocumentId": null,
        "createdTime": "2021-05-15T20:00:09.993+00:00"
      },
      {
        "id": 86,
        "name": "file/3",
        "size": 6,
        "path": "file/3",
        "parentConcreteDocumentId": null,
        "createdTime": "2021-05-15T20:00:09.993+00:00"
      }
    ]
  }
}
```

> **DELETE**`/documents/{id}`

- deletes document by id if user has READWRITE access rights.

---

---

## `/user`

> **POST**`/user/register`

- accessible for everyone. allows adding new user with the role "USER" in a database.

REQUEST BODY EXAMPLE:

```json
{
  "login": "login10",
  "password": "pass"
}
```

RESPONSE EXAMPLE:

```json
{
"id": 49,
"login": "login10",
"password": "$2y$10$.NfVS7FDg6EfYQh82JwHb.GgtZMvqyLUl.aRJl3JrXYR2uW9Lempi",
"role": "USER"
}
```

> **POST**`/user/grantaccess`

- accessible only for ADMINs. allows modifying role of any user.

REQUEST BODY EXAMPLE:

```json
{
  "id": 49,
  "role": "ADMIN"
}
```

RESPONSE EXAMPLE:

```json
{
"id": 49,
"login": "login10",
"password": "$2y$10$.NfVS7FDg6EfYQh82JwHb.GgtZMvqyLUl.aRJl3JrXYR2uW9Lempi",
"role": "USER"
}
```

> **GET**`/user/current`

- allows getting current user

RESPONSE EXAMPLE:

```json
{
"id": 49,
"login": "login10",
"password": "$2y$10$.NfVS7FDg6EfYQh82JwHb.GgtZMvqyLUl.aRJl3JrXYR2uW9Lempi",
"role": "USER"
}
```

---

---

## `/type`

> **GET** `/type`

- returns all document types

RESPONSE EXAMPLE:

```json
[
  {
    "id": 4,
    "name": "fax"
  },
  {
    "id": 5,
    "name": "simplydoc"
  },
  {
    "id": 6,
    "name": "newfax"
  },
  {
    "id": 7,
    "name": "mail"
  }
]
```

> **POST `/type`**

- adds new document typ

REQUEST BODY EXAMPLE:

```json
{
  "name" : "mail"
}
```

RESPONSE EXAMPLE:

```json
{
"id": 7,
"name": "mail"
}
```

---

---

## `/access`

> **GET** `/access/{id}`

- is to check which access rights current user has on certain file

RESPONSE EXAMPLE:

```json
{
"read": true,
"read_write": false
}
```

> **POST** `/access`

- is to modify access rights to concrete file for certain user
- accessible only for one, who has READ_WRITE access to this file

With parameters:

- access - **`READWRITE`** or **`READ`**
- modify - **`GRANT`** or **`DECLINE`**
- fileId - Id of catalogue or document
- userId - Id of user

REQUEST BODY EXAMPLE:

```json
{
  "access": "READWRITE",
  "modify": "GRANT",
  "fileId": 63,
  "userId": 60
}
```

RESPONSE EXAMPLE:

in the response we'll see list of users who has access to read or readwrite (depending on what we modified) 

```json
[
  {
    "id": 60,
    "login": "login3",
    "password": "$2a$10$./AHJhUWebG1KYs/NZlHCuxaW5jeD0KKDQaIhmEeqta.eCduDX0bW",
    "role": "USER"
  },
  {
    "id": 2,
    "login": "login2",
    "password": "$2y$10$sn.Z5ig7Phob/URwKJQq.OVtBJmWkYVQ0ykvkpE4F0C7OTzAGXvKu",
    "role": "USER"
  }
]
```

---

---

## `/globalsearch`

This endpoint is accessible only for ADMIN user, because this type of search ignores whole catalogue structure (so access levels too), and search directly in database by specified parameters. 

 

> GET`globalsearch/documents?page=..&pageSize=..&documentType=...&name=...`

request with four non required parameters

- `page` - number of page (starts with 0)
- `pageSize` - size of each page
- `documentType` - document type, case-insensitive
- `name` - name or part of name of document.

this request returns page of last versions of documents filtered by name, document type, and ordered by priority.

RESPONSE EXAMPLE:

```json
{
  "content": [
    {
      "id": 24,
      "parentId": 1,
      "createdTime": "2021-05-13T20:43:10.005+00:00",
      "userCreatedById": 2,
      "name": "newDoc_mod2",
      "typeOfFile": "DOCUMENT",
      "documentType": "simplydoc",
      "priority": "LOW",
      "concreteDocument": {
        "id": 23,
        "name": "newDoc_mod2",
        "description": "descr_mod2",
        "version": 1,
        "modifiedTime": "2021-05-13T20:43:10.005+00:00",
        "userModifiedBy": 2,
        "parentDocumentId": 24,
        "data": [
          {
            "id": 61,
            "name": "paaa",
            "size": 4,
            "path": "paaa",
            "parentConcreteDocumentId": null,
            "createdTime": "2021-05-13T20:43:10.009+00:00"
          },
          {
            "id": 62,
            "name": "ptthhhhh12",
            "size": 10,
            "path": "ptthhhhh12",
            "parentConcreteDocumentId": null,
            "createdTime": "2021-05-13T20:43:10.009+00:00"
          },
          {
            "id": 63,
            "name": "paa3",
            "size": 4,
            "path": "paa3",
            "parentConcreteDocumentId": null,
            "createdTime": "2021-05-13T20:43:10.009+00:00"
          }
        ]
      }
    },
    {
      "id": 26,
      "parentId": 1,
      "createdTime": "2021-05-13T20:43:27.962+00:00",
      "userCreatedById": 2,
      "name": "newDoc_mod2",
      "typeOfFile": "DOCUMENT",
      "documentType": "simplydoc",
      "priority": "LOW",
      "concreteDocument": {
        "id": 24,
        "name": "newDoc_mod2",
        "description": "descr_mod2",
        "version": 1,
        "modifiedTime": "2021-05-13T20:43:27.962+00:00",
        "userModifiedBy": 2,
        "parentDocumentId": 26,
        "data": [
          {
            "id": 64,
            "name": "paaa",
            "size": 4,
            "path": "paaa",
            "parentConcreteDocumentId": null,
            "createdTime": "2021-05-13T20:43:27.965+00:00"
          },
          {
            "id": 65,
            "name": "ptthhhhh12",
            "size": 10,
            "path": "ptthhhhh12",
            "parentConcreteDocumentId": null,
            "createdTime": "2021-05-13T20:43:27.965+00:00"
          },
          {
            "id": 66,
            "name": "paa3",
            "size": 4,
            "path": "paa3",
            "parentConcreteDocumentId": null,
            "createdTime": "2021-05-13T20:43:27.965+00:00"
          }
        ]
      }
    }
  ],
  "pageable": {
    "sort": {
      "unsorted": true,
      "sorted": false,
      "empty": true
    },
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 10,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalElements": 2,
  "totalPages": 1,
  "size": 10,
  "number": 0,
  "sort": {
    "unsorted": true,
    "sorted": false,
    "empty": true
  },
  "first": true,
  "numberOfElements": 2,
  "empty": false
}
```

> GET`globalsearch/catalogue?page=..&pageSize=..&name=...`

request with three non required parameters

- `page` - number of page (starts with 0)
- `pageSize` - size of each page
- `name` - name or part of name of catalogue (case-insensitive).

this request returns page of catalogues filtered by name ordered by name.

RESPONSE EXAMPLE:  (`/globalsearch/catalogue?name=root`)

```json
{
  "content": [
    {
      "id": 63,
      "parentId": 1,
      "createdTime": "2021-05-15T18:54:11.625+00:00",
      "userCreatedById": 1,
      "name": "children_of_root",
      "typeOfFile": "CATALOGUE"
    },
    {
      "id": 1,
      "parentId": null,
      "createdTime": "2021-05-12T13:47:39.800+00:00",
      "userCreatedById": null,
      "name": "root",
      "typeOfFile": "CATALOGUE"
    }
  ],
  "pageable": {
    "sort": {
      "unsorted": true,
      "sorted": false,
      "empty": true
    },
    "offset": 0,
    "pageNumber": 0,
    "pageSize": 10,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 2,
  "size": 10,
  "number": 0,
  "sort": {
    "unsorted": true,
    "sorted": false,
    "empty": true
  },
  "first": true,
  "numberOfElements": 2,
  "empty": false
}
```