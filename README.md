# TDD with Spring Boot

On this README file I detail endpoints created to this API REST, their functionality and details of the proyect

## Details

This project uses **H2SQL** and into the resources path was added an import.sql with data to start using this API.

In test path are all test made for this example following **TDD(Test Driven Development)**. All of they will pass with any problem.

## Endpoints

1. User registration. __Endpoint /user/registry Method: POST__
    This endpoint allows to registry a new user to database. It's a must send into the request body the next data:
    
    ```
    {
        name:"",
        last_name:"",
        username:"",
        password:""
    }
    ```
   
    __Note: It is important that the password was validated on the frontend__
    
    The creation date is asigned automatically by the API.

2. Get user information. __Endpoint /user/details/{id} Method: GET__
    From this endpoint itcan be get the data from a user. By default the user's password is not send, all this for security.
    
    User information is send by the API with next JSON format (Added data is just for example):
    ```
    {
        "user": {
            "id": 1,
            "name": "Joe",
            "last_name": "Doe",
            "username": "jdoe",
            "password": null,
            "createdAt": "2020-06-08"
        }
    }
    ```

3. Update a user. __Endpoint /user/update/{id} Method: PUT__
    This endpoint allows to update the data of a user. By default it is not possible to update the username nor password, it is only possible to chance the personal data.
    
    In the same way, it is necesary to send the new user's data througth request body.

4. Deleta a user. __Endpoint /user/delete/{id} Method: DELETE__
    Througth this endpoint it is possible to delete a user using its id.

5. Get all users. __Endpoint /user/all Method: GET__
    From this endpoint it is possible to retrive all users registrated in the database.
    