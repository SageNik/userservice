# userservice
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


4.     Search for users

This endpoint should accept a dto which represents a filter that would allow to search for users:

     {
“username”:”xxx”,  
“firstname”:”yyyy”, 
“lastname”:”zzzz”,
“address”:”vvvvvv”
     }

All fields are optional. Endpoint should support pagination and return a list of users that matched specified search criteria.

Implementation notes:
    
Java 11+
Spring Boot, WebFlux
PostgreSQL (+ postgres-r2dbc reactive driver) 
ElasticSearch with Debezium
AWS for emails and SMS 
Docker
Minikube 


Use ElasticSearch to implement `Search for users` endpoint. To synchronize data between Postgres `Users` table and corresponding ElasticSearch index use Debezium. Use AWS for notifications. 


# Start the application in Minikube
Minikube must be installed. Go to the folder kubernetes and run a command:
kubectl create -f .



# Start the application in Docker
docker-compose -f docker-compose.yaml up

# Start Elasticsearch connector

curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @es-sink.json

# Start MySQL connector

curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @source.json
