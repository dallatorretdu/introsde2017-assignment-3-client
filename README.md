Giulio Dallatorre giulio.dallatorre@studenti.unitn.it
Worked alone
Server visible at https://introsde-assignment-3-dallator.herokuapp.com/person?wsdl

The project is basically an adaptation of the older project 2 to work on a SOAP architecture, but many things and fuctions had to be redesigned to fit the new requirements.

The Long ids that were required as parameters in this project were all swapped for Integers, such to better conform my existing database schema and lean the conversion further. Since SOAP can return arrays very easily all the wrapper classes used in the second assignment are now gone, except ActivityWrapper, which contained useful filtering methods that I still needed. Even so the server will return an array of Activities, effectively hiding the wrapper to the client.
WSIMPORT has been used to create the client stubs, and it did not generate the wrapper class.
The client generated PersonImplService.java has been manually modified to conform to both my local deployment and the heroku one.
The client checks valid connectivity to the server by using the getHelloWorldAsString method which is carried over from the lab exercises.

The Server code basically does the same stuff as the assigment 2, but everything comes from the WebService @webmethod methods and every call will return the newely created, updated or read item, except for savePersonPreference which returns nothing, as for the requirements. The methods now refer the Activities as "preferences", this is only to conform 100% to the requirement list, which now calls the person's activities a preference.
2 endpoints have been created (/person) (/init) in order to separate the database initialization function from the required methods, the client does not have the initializer stubs and is not aware of it's presence.
The client, after assessing connectivity, will print the server's location trough the stub class, and will also print it's version. After that it will execute a suite of 12 tests made so that every implemented method is tested and that the database will be left unchanged. When a name is changed, it will be changed back to it's original value after the (printed) assertion has been done.

Just run ant execute.client to compile and run the client
The server is compiled by ant create.war