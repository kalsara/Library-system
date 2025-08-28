### API Endpoints

#### Borrower Management

**Register a new borrower**
```
POST /api/v1/borrowers
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@email.com"
}

Response: 201 Created
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@email.com",
  "createdDate": "2023-12-01T10:00:00",
  "lastModifiedDate": "2023-12-01T10:00:00"
}
```

**Get borrower by ID**
```
GET /api/v1/borrowers/{id}

Response: 200 OK
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@email.com",
  "createdDate": "2023-12-01T10:00:00",
  "lastModifiedDate": "2023-12-01T10:00:00"
}
```

#### Book Management

**Register a new book**
```
POST /api/v1/books
Content-Type: application/json

{
  "isbn": "9780134685991",
  "title": "Effective Java",
  "author": "Joshua Bloch"
}

Response: 201 Created
{
  "id": 1,
  "isbn": "9780134685991",
  "title": "Effective Java",
  "author": "Joshua Bloch",
  "isAvailable": true,
  "createdDate": "2023-12-01T10:00:00",
  "lastModifiedDate": "2023-12-01T10:00:00"
}
```

**Get all books**
```
GET /api/v1/books

Response: 200 OK
[
  {
    "id": 1,
    "isbn": "9780134685991",
    "title": "Effective Java",
    "author": "Joshua Bloch",
    "isAvailable": true,
    "createdDate": "2023-12-01T10:00:00",
    "lastModifiedDate": "2023-12-01T10:00:00"
  }
]
```

**Get book by ID**
```
GET /api/v1/books/{id}

Response: 200 OK
{
  "id": 1,
  "isbn": "9780134685991",
  "title": "Effective Java",
  "author": "Joshua Bloch",
  "isAvailable": true,
  "createdDate": "2023-12-01T10:00:00",
  "lastModifiedDate": "2023-12-01T10:00:00"
}
```

#### Borrowing Operations

**Borrow a book**
```
POST /api/v1/borrowing/borrow?borrowerId=1&bookId=1

Response: 201 Created
{
  "id": 1,
  "borrowerId": 1,
  "borrowerName": "John Doe",
  "bookId": 1,
  "bookTitle": "Effective Java",
  "bookAuthor": "Joshua Bloch",
  "borrowedDate": "2023-12-01T10:00:00",
  "returnedDate": null,
  "isActive": true
}
```

**Return a book**
```
POST /api/v1/borrowing/return?borrowerId=1&bookId=1

Response: 200 OK
{
  "id": 1,
  "borrowerId": 1,
  "borrowerName": "John Doe",
  "bookId": 1,
  "bookTitle": "Effective Java",
  "bookAuthor": "Joshua Bloch",
  "borrowedDate": "2023-12-01T10:00:00",
  "returnedDate": "2023-12-01T14:00:00",
  "isActive": false
}
```

### Error Responses

**400 Bad Request - Validation Error**
```json
{
  "status": 400,
  "message": "Validation failed",
  "timestamp": "2023-12-01T10:00:00",
  "errors": {
    "email": "Email should be valid",
    "name": "Name is required"
  }
}
```

**404 Not Found**
```json
{
  "status": 404,
  "message": "Borrower not found with id: 1",
  "timestamp": "2023-12-01T10:00:00"
}
```

**409 Conflict - Book Already Borrowed**
```json
{
  "status": 409,
  "message": "Book is already borrowed by another member",
  "timestamp": "2023-12-01T10:00:00"
}

### Database Choice Justification

**MySQL** was chosen as the production database for the following reasons:

1. **ACID Compliance**: Ensures data integrity for financial and borrowing transactions
2. **Concurrent Access**: Excellent handling of multiple simultaneous borrowing operations
3. **Scalability**: Can handle growth from small library to large enterprise system
4. **Community Support**: Large community and extensive documentation
5. **Simplicity**: mostly reads, and lightweight workloads
6. **Cost Effective**: Open-source with no licensing fees

**H2** is used for development and testing for rapid prototyping and easy setup.

### Key Assumptions

1. **Single Library System**: The system manages one library location
2. **Unique Email per Borrower**: Email addresses are unique identifiers for borrowers
3. **Book Copies**: Multiple physical copies of the same book (same ISBN) have different IDs
4. **Single Borrower per Book**: Only one borrower can borrow a specific book copy at a time
5. **No Due Dates**: Simplified system without return due dates or late fees
6. **No Authentication**: Basic system without user authentication/authorization
7. **ISBN Validation**: Accepts both ISBN-10 and ISBN-13 formats
8. **Concurrent Safety**: Database transactions ensure thread safety for borrowing operations
