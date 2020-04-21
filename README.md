# userservice
Gain knowledge in:

Spring Webflux 
OAUTH + JWT
Java 11
AWS SNS, SES


User microservice:

Responsibilities:

New user registration
Generating JWT token based on provided username/password
Refreshing JWT token
Search for users


New user registration: 
    
Post 
{
“username”:”xxx”,  
“password”:”yyy”, 
“firstname”:”yyyy”, 
“lastname”:”zzzz”,
“address”:”vvvvvv”
}


Username is either an email address or a phone number. In case if user has provided an email address as a username, then a verification email is sent. Verification email contains special link with a verification code, which, upon click, activates user. In case if user has provided a phone number, a text message with a verification code is sent. This verification code later can be used to call a dedicated endpoint which activates user. Verification code should have a configurable TTL. Expired verification codes should be collected.     
    
     2.  User verification endpoint
        
Endpoint dedicated to activate a user. It should support GET requests and contain a verification code as a part of URL. For instance:

  http://app.com/user/verify/{verificationCode}

In case if verificationCode exists and is not expired, user is activated. 

   3.   Obtain new token:
Endpoint dedicated to obtain new valid access JWT token and refresh JWT token. 
Both access and refresh tokens should have configurable TTL.


Implementation notes:
    
Java 11+
Spring Boot, WebFlux
PostgreSQL (+ postgres-r2dbc reactive driver) 
AWS for emails and SMS 




# Start the application


docker-compose -f docker-compose.yaml up

# Start Elasticsearch connector

curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @es-sink.json

# Start MySQL connector

curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @source.json