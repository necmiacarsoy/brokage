You can navigate to brokage folder and run instruction below to run the project.
./mvnw clean spring-boot:run

Initially loaded database with 5 TRY assets for 5 different customers and 1 admin user.

I implemented 5 endpoints. All endpoints can be used with basic authentication.
You can find the admin user information below.
Username: admin
Password: ourpassword

You can use createCustomer endpoint to create new users and use other 4 endpoint with these created users.
All users can only see their orders and assets except admin user. Admin user can view all information.
POST http://localhost:8080/v1/customers
requestBody: {"username":"user1",
"password":"12345"}

I used size parameter in request body for both size and usableSize at initial order creation phase.
Then I only changed usable size for calculations.
POST http://localhost:8080/v1/orders
requestBody: {"assetName":"aselsan",
"orderSide":"BUY",
"size":3,
"price":10}

DELETE http://localhost:8080/v1/orders/1

GET http://localhost:8080/v1/assets

GET http://localhost:8080/v1/orders?startDate=2025-10-01&endDate=2025-10-07
