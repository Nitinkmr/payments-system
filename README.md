
# Dynamic Payment Gateway Routing – Spring Boot

Spring Boot service that routes payments across multiple gateways (Razorpay, PayU, Cashfree) using weighted routing and gateway health checks.

**Base URL**

```text
https://spring-boot-production-0a05.up.railway.app
1. Initiate a Transaction
Endpoint

Method: POST

URL: https://spring-boot-production-0a05.up.railway.app/transactions/initiate

Headers: Content-Type: application/json

Request Body

json
{
  "orderId": "ORD123",
  "amount": 499.0,
  "paymentInstrument": {
    "type": "card",
    "cardNumber": "411111******1111",
    "expiry": "12/29"
  }
}
Expected

HTTP 200

JSON with: transactionId, orderId, amount, gateway, status (PENDING), attemptNumber

2. Success Callback
Endpoint

Method: POST

URL: https://spring-boot-production-0a05.up.railway.app/transactions/callback

Headers: Content-Type: application/json

Request Body

json
{
  "orderId": "ORD123",
  "status": "success",
  "gateway": "razorpay",
  "reason": "Payment authorized"
}
Marks the latest transaction for that orderId and gateway as SUCCESS.

3. Failure Callback
Use the same callback endpoint to mark a failure and influence gateway health.

Request Body

json
{
  "orderId": "ORD123",
  "status": "failure",
  "gateway": "razorpay",
  "reason": "Mock failure"
}
Multiple failures for the same gateway can mark it unhealthy and exclude it from routing for a cooldown period.

4. Multiple Attempts for the Same Order
Call /transactions/initiate multiple times with the same orderId (e.g., ORD200) and verify that attemptNumber in the response increments (1, 2, 3, …).

TODO

