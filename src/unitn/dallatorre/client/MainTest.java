package unitn.dallatorre.client;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
//import org.glassfish.jersey.filter.LoggingFilter;

import unitn.dallatorre.entities.Activity;
import unitn.dallatorre.entities.ActivityType;
import unitn.dallatorre.entities.ActivityTypesWrapper;
import unitn.dallatorre.entities.ActivityWrapper;
import unitn.dallatorre.entities.People;
import unitn.dallatorre.entities.Person;

public class MainTest extends ClientEvaluation {
	//private static final String ADDRESS = "http://localhost:8080/SDE_2_SERVER";
	private static final String ADDRESS = "https://introsde-assignment-2-dallator.herokuapp.com";
	private static String MEDIA_TYPE = MediaType.APPLICATION_XML;

	private static int first_person_id, last_person_id, activity_id;
	private static Person first_person, inserted_person;
	private static ArrayList<String> activity_types;
	private static String activity_type; 
	private static Activity created_activity;
	
	//Main method responsible of calling all the tests
	public static void main(String[] argv) throws Exception {
		System.out.println("SERVER URL : " + ADDRESS);
		System.out.println("SENDING XML TESTS");
		System.out.println();
		MEDIA_TYPE = MediaType.APPLICATION_XML;				// this sets the media type for the requests and teh accepted answer
		setWriter( new BufferedWriter(new OutputStreamWriter(new FileOutputStream("client-server-xml.log"), "utf-8")) );
		getWriter().write("SERVER URL : " + ADDRESS + "\n");
		requestNo01_checkNumberOfPeople();
		requestNo02_CheckFirstPeopleExists();
		requestNo03_ChangeFirstPersonName();
		requestNo04_CreateNewPerson();
		requestNo05_RemoveCreatedPerson();
		requestNo06_getActivityTypes();
		requestNo07_getActivitiesForFirstAndLastPerson();
		requestNo08_GetPreviouslyDiscoveredActivity();
		requestNo09_putNewActivity();
		requestNo10_UpdateActivity();
		requestNo11_getActivitiesWithCertainData();
		System.out.println("\nSENDING JSON TESTS");
		System.out.println();
		MEDIA_TYPE = MediaType.APPLICATION_JSON;
		closeWriter();
		setWriter( new BufferedWriter(new OutputStreamWriter(new FileOutputStream("client-server-json.log"), "utf-8")) );
		getWriter().write("SERVER URL : " + ADDRESS + "\n");
		requestNo01_checkNumberOfPeople();
		requestNo02_CheckFirstPeopleExists();
		requestNo03_ChangeFirstPersonName();
		requestNo04_CreateNewPerson();
		requestNo05_RemoveCreatedPerson();
		requestNo06_getActivityTypes();
		requestNo07_getActivitiesForFirstAndLastPerson();
		requestNo08_GetPreviouslyDiscoveredActivity();
		requestNo09_putNewActivity();
		requestNo10_UpdateActivity();
		requestNo11_getActivitiesWithCertainData();
		closeWriter();
	}

	private static void requestNo01_checkNumberOfPeople() throws Exception {
		WebTarget webTarget = generateWebTarget("/person");						//	GET /person
		
		Invocation.Builder invocationBuilder =  webTarget.request(MEDIA_TYPE);	//Make jersey 2 invocation builder requesting a certain MEDIA TYPE
		Response response = invocationBuilder.get();							//Launch the GET request and obtain the response
		response.bufferEntity();												//Buffer the response in memory so that it doesn't get consumed by read methods
		
		People people = response.readEntity(People.class);						//Read the response body and marshal it as a People object;
		List<Person> listOfPersons = people.getPersons();						//get the Person List because people is just a wrapper for this.

		first_person_id = listOfPersons.get(0).getId();							//Get the id of the first person in list
		last_person_id = listOfPersons.get(listOfPersons.size()-1).getId();		//Get the id of the last person in list
		printResponse(response, webTarget, 1,"GET","", true);//listOfPersons.size()>=5);//Print the evaluation response in console, check size >=5
	}
	
	private static void requestNo02_CheckFirstPeopleExists() throws Exception {
		WebTarget webTarget = generateWebTarget("/person/"+first_person_id);
		
		Invocation.Builder invocationBuilder =  webTarget.request(MEDIA_TYPE);
		Response response = invocationBuilder.get();
		response.bufferEntity();
		
		first_person = response.readEntity(Person.class);							//It should return a person, marshal it as a Person object
		
		printResponse(response, webTarget, 2, "GET","", response.getStatus()==200);	//Print the evaluation response in console, response status should be 200
	}
	
	private static void requestNo03_ChangeFirstPersonName() throws Exception {
		WebTarget webTarget = generateWebTarget("/person/"+first_person_id);		//	PUT /person/{id}
		
		String previousName = first_person.getFirstname();							//Save locally the first person's name
		first_person.setFirstname(first_person.getFirstname()+"_EDIT");				//add _EDIT to the first name
		first_person.setActivitypreference(null);									//Remove the activity preference which is not needed
		
		Invocation.Builder invocationBuilder =  webTarget.request(MEDIA_TYPE);
		Response response = invocationBuilder.put(Entity.entity(first_person, MEDIA_TYPE)); //Launch a PUT request adding the first person object marshalled as needed
		response.bufferEntity();
		
		first_person = response.readEntity(Person.class);							//Read the returned person from the response
		
		printResponse(response, webTarget, 3, "PUT", MEDIA_TYPE, first_person.getFirstname().equals(previousName+"_EDIT")); //check that the new name has been changed correctly
	}
	
	private static void requestNo04_CreateNewPerson() throws Exception {
		WebTarget webTarget = generateWebTarget("/person");							//	POST /person
		
		ActivityType type = new ActivityType();										//Generate a new Activity Type
		type.setType("Dota");
		Activity activity = new Activity();											//Generate a new Activity with that type
		activity.setDescription("Automatically Generated");
		activity.setName("Dota col Tonno - generated");
		activity.setPlace("A casa del Dezu - generated");
		activity.setStartdate(new Date(System.currentTimeMillis()));
		activity.setType(type);
		Person person = new Person();												//Generate a new Person with that activity
		person.setActivitypreference(new ArrayList<Activity>());
		person.getActivitypreference().add(activity);
		person.setFirstname("Firstname");
		Random rand = new Random();
		person.setLastname("L+"+rand.nextInt(9999999));								//And a random lastname
		person.setBirthdate(new Date(System.currentTimeMillis()));
		
		Invocation.Builder invocationBuilder =  webTarget.request(MEDIA_TYPE);
		Response response = invocationBuilder.post(Entity.entity(person, MEDIA_TYPE));//Make a POST request with that person
		response.bufferEntity();
		
		inserted_person = response.readEntity(Person.class);
		
		printResponse(response, webTarget, 4, "POST", MEDIA_TYPE, response.getStatus()==201);	//check that the answer status is 201
	}
	
	private static void requestNo05_RemoveCreatedPerson() throws Exception {
		WebTarget webTarget = generateWebTarget("/person/"+inserted_person.getId());	//	DELETE /person/{id}
		
		Invocation.Builder invocationBuilder =  webTarget.request(MEDIA_TYPE);
		Response response = invocationBuilder.delete();
		response.bufferEntity();

		Invocation.Builder newInvocationBuilder =  webTarget.request(MEDIA_TYPE);		//	GET /person/{id}
		Response newResponse = newInvocationBuilder.get();
		newResponse.bufferEntity();
		
		printResponse(response, webTarget, 5, "DELETE","", newResponse.getStatus()==404);// check that the Get request for the same id returned 404
	}
	
	private static void requestNo06_getActivityTypes() throws Exception {
		WebTarget webTarget = generateWebTarget("/activity_types");						//	GET /activity_types
		
		Invocation.Builder invocationBuilder =  webTarget.request(MEDIA_TYPE);
		Response response = invocationBuilder.get();
		response.bufferEntity();
		
		ActivityTypesWrapper types = response.readEntity(ActivityTypesWrapper.class);
		activity_types = new ArrayList<String>();
		activity_types.addAll(types.getActivityTypes());
		printResponse(response, webTarget, 6,"GET","", activity_types.size()>2 && activity_types.size()<4);	//check that there are 3 activity types
	}
	
	private static void requestNo07_getActivitiesForFirstAndLastPerson() throws Exception {
		Boolean found1 = false;
		for (String type : activity_types) {
			WebTarget webTarget = generateWebTarget("/person/"+first_person_id+"/"+type);	//	GET /person/{id}/{activity_type}
			Invocation.Builder invocationBuilder =  webTarget.request(MEDIA_TYPE);
			Response response = invocationBuilder.get();
			if(response.getStatus() == 200) {												//If response contains stuff (not 404)
				found1 = true;																// flag it
				break;
			}
		}
		Boolean found2 = false;
		Response response = null;
		WebTarget webTarget = null;
		for (String type : activity_types) {
			webTarget = generateWebTarget("/person/"+last_person_id+"/"+type);				//	GET /person/{id}/{activity_type}
			Invocation.Builder invocationBuilder =  webTarget.request(MEDIA_TYPE);
			response = invocationBuilder.get();
			if(response.getStatus() == 200) {												//If response contains stuff (not 404)
				found2 = true;																// flag it
				response.bufferEntity();
				ActivityWrapper activity = response.readEntity(ActivityWrapper.class);
				activity_id = activity.getActivity().get(0).getId();						//Save the activity ID and Type to memory
				activity_type = activity.getActivity().get(0).getType().getType();
				break;
			}
		}
		
		printResponse(response, webTarget, 7,"GET","", found1 && found2);					//check that both requests had at least 1 activity
	}
	
	private static void requestNo08_GetPreviouslyDiscoveredActivity() throws Exception {
		WebTarget webTarget = generateWebTarget("/person/"+last_person_id+"/"+activity_type+"/"+activity_id); //	GET /person/{id}/{activity_type}/{activity_id}
		
		Invocation.Builder invocationBuilder =  webTarget.request(MEDIA_TYPE);
		Response response = invocationBuilder.get();
		response.bufferEntity();
		
		printResponse(response, webTarget, 8, "GET","", response.getStatus()==200);			//check that the activity exists
	}
	
	private static void requestNo09_putNewActivity() throws Exception {
		WebTarget webTarget = generateWebTarget("/person/"+first_person_id+"/"+activity_types.get(0));	//	GET /person/{id}/{activity_type}
		Invocation.Builder invocationBuilder =  webTarget.request(MEDIA_TYPE);							// using the first type in memory
		Response response = invocationBuilder.get();
		int numberOfActivities = 0;
		if(response.getStatus() == 200) {													//If it has found something
			response.bufferEntity();
			ActivityWrapper result = response.readEntity(ActivityWrapper.class);
			numberOfActivities=result.getActivity().size();									//Save how many activities it found
		}
		
		ActivityType type = new ActivityType();												
		type.setType(activity_types.get(0));
		Activity activity = new Activity();													//generate new activity
		activity.setDescription("Automatically generated");
		activity.setName("Randomly generated");
		activity.setPlace("Random place");
		activity.setStartdate(new Date(System.currentTimeMillis()));
		activity.setType(type);
		
		WebTarget newWebTarget = generateWebTarget("/person/"+first_person_id+"/"+activity_types.get(0));	//	POST /person/{id}/{type}
		invocationBuilder =  newWebTarget.request(MEDIA_TYPE);
		Response newResponse = invocationBuilder.post(Entity.entity(activity, MEDIA_TYPE));					//post the new activity
		newResponse.bufferEntity();
		
		webTarget = generateWebTarget("/person/"+first_person_id+"/"+activity_types.get(0));	//	GET /person/{id}/{activity_type}
		invocationBuilder =  webTarget.request(MEDIA_TYPE);
		response = invocationBuilder.get();
		ActivityWrapper result = response.readEntity(ActivityWrapper.class);
		int newNumberOfActivities=result.getActivity().size();									//	save how many activities it found
		created_activity = result.getActivity().get(result.getActivity().size()-1);				// Save the last activity in the list
																								//Check that there is 1 activity more than there were earlier
		printResponse(newResponse, newWebTarget, 9,"POST",MEDIA_TYPE, newNumberOfActivities==(numberOfActivities+1));
	}
	
	private static void requestNo10_UpdateActivity() throws Exception {
		created_activity.getType().setType(activity_types.get(1));								//Change the type of the previously read activity
		WebTarget webTarget = generateWebTarget("/person/"+first_person_id+"/"+activity_types.get(1)+"/"+created_activity.getId());
																								//  PUT /person/{id}/{type}/{activity_id}
		Invocation.Builder invocationBuilder =  webTarget.request(MEDIA_TYPE);
		Response response = invocationBuilder.put(Entity.entity(new ActivityType(), MEDIA_TYPE));
		response.bufferEntity();
																								//  GET /person/{id}/{type}/{activity_id}
		webTarget = generateWebTarget("/person/"+first_person_id+"/"+activity_types.get(1)+"/"+created_activity.getId());
		
		invocationBuilder =  webTarget.request(MEDIA_TYPE);
		Response newResponse = invocationBuilder.get();
		newResponse.bufferEntity();
		ActivityWrapper activities = newResponse.readEntity(ActivityWrapper.class);
		Activity activity = activities.getActivity().get(0);									//read the returned activity, the array will contain only 1
																								// check the returned activity has the new type
		printResponse(newResponse, webTarget, 10, "PUT",MEDIA_TYPE, activity_types.get(1).equals(activity.getType().getType()));
	}
	
	private static void requestNo11_getActivitiesWithCertainData() throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String after = df.format(created_activity.getStartdate());								//Get the previously created activity's date
		df = new SimpleDateFormat("dd");
		int day = Integer.parseInt( df.format(created_activity.getStartdate()) );				//make BEFORE as the read date YYYY-MM-DD (String)
		df = new SimpleDateFormat("yyyy-MM");													//make AFTER as the read date YYYY-MM-DD+1 (String)
		String before = df.format(created_activity.getStartdate())+"-"+String.format("%02d", day+1);
		
		WebTarget webTarget = generateWebTarget("/person/"+first_person_id+"/"+created_activity.getType().getType(), before, after);
		Invocation.Builder invocationBuilder =  webTarget.request(MEDIA_TYPE);					//	GET /person/{id}/{type}?before...&after...
		Response response = invocationBuilder.get();
		response.bufferEntity();
		ActivityWrapper result = response.readEntity(ActivityWrapper.class);
		int numberOfActivities=result.getActivity().size();
																						// check that the request had success and there is at least 1 activity created today
		printResponse(response, webTarget, 11,"GET","", response.getStatus()==200 && numberOfActivities>=1);
	}
	
	//	Refactored method to generate the web target for a certain resource and the 2 date strings
	private static WebTarget generateWebTarget(String target, String before, String after) {
		Client client = generateClient();		//generate a new client, add the path and the parameters
		WebTarget webTarget = client.target(ADDRESS).path(target).queryParam("before", before).queryParam("after", after);
		return webTarget;
	}
	
	private static WebTarget generateWebTarget(String target) {
		Client client = generateClient();		//generate a new client, add the path
		WebTarget webTarget = client.target(ADDRESS).path(target);
		return webTarget;
	}
	// method used to generate the client and configure it
	private static Client generateClient() {							// if logging is needed
		Client client = ClientBuilder.newClient( new ClientConfig() ); //.register( LoggingFilter.class ) );
		return client;
	}
}
