# Brief explanation of this code

The project is a PC hospital management system which exports patient and pharmacy data to the rest API for processing and performing predictions


### How to run the project

Install mysql community 8.0 with support for backward compatibility settings.  
Create a database called medica_client.  
Import medica_client_update.sql to the database.  
Create a database called medica_client_sessions.  
Import medica_client_sessions_update.sql to the database.

Edit src/main/java/Hospital/Controllers/settings.java desSession and des to mysql user and password for the user.  
run the medica client application with java 8.

# The users in this system are:
* A receptionist

* A doctor

* A lab technician

* A hospital Administrator

* Nurses
