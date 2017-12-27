package introsde.assignment3.soap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import introsde.assignment3.soap.entities.*;

public class MainClientTest extends TestUtils {
	
	private static PersonImplService service;
	private static PersonWebService ws;
	private static Integer no;
	private static Integer firstPersonID;
	private static int numberOfPeople;
	private static Person firstPerson;
	private static Person generatedPerson;
	private static Activity readActivity;
	
	public static void main(String[] argv) throws Exception {
		service = new PersonImplService();
        ws = service.getPersonImplPort();
        assertEquals("Hello World JAX-WS Marcelo", ws.getHelloWorldAsString("Marcelo"));
        System.out.println("Service Active at " + ws);
        
        setWriter( new BufferedWriter(new OutputStreamWriter(new FileOutputStream("client-server.log"), "utf-8")) );
        printMessage(ws.toString());
        no=1;
        requestNo01_checkNumberOfPeople();
        no++;
		requestNo02_CheckFirstPersonExists();
		no++;
		requestNo03_UpdateFirstPersonName();
		no++;
		requestNo04_CreateNewPerson();
		no++;
		requestNo05_readPreferences();
		no++;
		requestNo06_readSinglePreference();
		no++;
		requestNo07_updatePreference();
		no++;
		requestNo08_evaluatePreference();
		no++;
		requestNo09_SavePreference();
		no++;
		requestNo10_getBestPreference();
		no++;
		requestNo11_RemoveCreatedPerson();
		no++;
		requestNo12_ReadAllPreferences();
		
		closeWriter();
	}

	private static void requestNo01_checkNumberOfPeople() {
		List<Person> response = ws.getPersonList();
		printResponse(response, no,"getPersonList()", response.size()>=5);
		String output = "\n\tPERSONS:\n";
		for(Person person : response) {
			output += "\t\t=> " + person.getFirstname() + " " + person.getLastname() + "\n";
		}
		printMessage(output);
		numberOfPeople = response.size();
		firstPersonID = response.get(0).getId();
	}
	
	private static void requestNo02_CheckFirstPersonExists() {
		Person response = ws.getPerson(firstPersonID);	
		printResponse(response, no,"getPerson(firstPersonID)", response.getId()==firstPersonID);
		
		firstPerson = response;
	}
	
	private static void requestNo03_UpdateFirstPersonName() {
		String firstname = firstPerson.getFirstname();
		firstPerson.setFirstname("UPDATED NAME DURING TESTS");
		
		Person response = ws.updatePerson(firstPerson);
		printResponse(response, no,"updatePerson(firstPerson)", response.getFirstname().equals(firstPerson.getFirstname()));
		
		firstPerson.setFirstname(firstname);
		ws.updatePerson(firstPerson);
		printMessage("Changes Reverted");
	}
	
	private static void requestNo04_CreateNewPerson() {
		generatedPerson = generateNewPerson();
		generatedPerson = ws.createPerson(generatedPerson);
		int pplCount = ws.getPersonList().size();
		
		printResponse(generatedPerson, no,"createPerson(generatedPerson)", pplCount>numberOfPeople);
	}
	
	private static void requestNo05_readPreferences() {
		List<Activity> result = ws.readPersonPreferences(generatedPerson.getId(), generatedPerson.getActivitypreference().get(0).getType().getId());
		
		printResponse(result, no,"readPersonPreferences(generatedPerson.getId(), generatedPerson.getActivitypreference().get(0).getType().getId())", result.size()>0);
	}
	
	private static void requestNo06_readSinglePreference() {
		Activity result = ws.readPersonPreference(generatedPerson.getId(), generatedPerson.getActivitypreference().get(0).getId());
		
		printResponse(result, no,"readPersonPreference(generatedPerson.getId(), generatedPerson.getActivitypreference().get(0).getId())", result.getId() == generatedPerson.getActivitypreference().get(0).getId());
		readActivity = result;
	}
	
	private static void requestNo07_updatePreference() {
		readActivity.setName("New Name After Editing");
		readActivity = ws.updatePersonPreference(generatedPerson.getId(), readActivity);
		
		printResponse(readActivity, no,"updatePersonPreference(generatedPerson.getId(), readActivity)", readActivity.getName().equals("New Name After Editing"));
	}
	
	private static void requestNo08_evaluatePreference() {
		readActivity = ws.evaluatePersonPreference(generatedPerson.getId(), readActivity, 9);

		printResponse(readActivity, no,"evaluatePersonPreference(generatedPerson.getId(), readActivity, 9)", readActivity.getPreference()==9);
	}
	
	private static void requestNo09_SavePreference() {
		Activity activity = generateActivity();
		activity.setName("Activity added");
		int number = ws.readPersonPreferences(generatedPerson.getId(), activity.getType().getId()).size();
		
		ws.savePersonPreference(generatedPerson.getId(), activity);
		int numberAfterAdd = ws.readPersonPreferences(generatedPerson.getId(), activity.getType().getId()).size();
		printResponse(numberAfterAdd, no,"savePersonPreference(generatedPerson.getId(), activity)", numberAfterAdd>number);
	}
	
	private static void requestNo10_getBestPreference() {
		List<Activity> result = ws.getBestPersonPreferences(generatedPerson.getId());

		printResponse(result, no,"getBestPersonPreferences(generatedPerson.getId())", result.size()>0);
		String output = "\n\tCONTENT:\n";
		for(Activity a : result) {
			output += "\t\t=> [" + a.getName() + " " + a.getDescription() + "] Rank: " + a.getPreference() + "\n";
		}
		printMessage(output);
	}
	
	private static void requestNo11_RemoveCreatedPerson() {
		ws.deletePerson(generatedPerson);
		int pplCount = ws.getPersonList().size();
		
		printResponse(String.class, no,"deletePerson(generatedPerson)", pplCount==numberOfPeople);
	}
	
	private static void requestNo12_ReadAllPreferences() {
		List<Activity> result = ws.readPreferences();
		
		printResponse(result, no,"readPreferences()", result.size()>0);
		
		String output = "\n\tCONTENT:\n";
		for(Activity a : result) {
			output += "\t\t=> [" + a.getName() + " -- " + a.getDescription() + "]\n";
		}
		printMessage(output);
	}
}
