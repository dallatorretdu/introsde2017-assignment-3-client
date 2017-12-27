package introsde.assignment3.soap;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import introsde.assignment3.soap.entities.Activity;
import introsde.assignment3.soap.entities.ActivityType;
import introsde.assignment3.soap.entities.Person;

public class TestUtils {

	private static Writer writer;
	
	public TestUtils() {
		super();
	}
	
	public static void printResponse(Object response, int requestSequenceNumber, String method, Boolean result) {
		String resString = ((result) ? "OK" : "ERROR");
		String OUTPUT = "\nRequest #["+requestSequenceNumber+"]: ["+method+"] \n" + 
							"\t=> Result: ["+resString+"]\n" + 
							"\t=> Response Content: ["+response.getClass()+"]\n";
		if(response.getClass().equals(java.util.ArrayList.class)) {
			OUTPUT += "\t\t=> Size: ["+ ((List)response).size() +"]\n"
					+ "\t\t\t=> Elements: ["+ ((List)response).get(0).getClass() +"]\n";
		}
		if(response.getClass().equals(introsde.assignment3.soap.entities.Person.class)) {
			OUTPUT += "\t\t=> ID: ["+ ((Person)response).getId() +"]\n"
					+ "\t\t=> First Name: ["+ ((Person)response).getFirstname() +"]\n"
					+ "\t\t=> Last Name: ["+ ((Person)response).getLastname() +"]\n"
					+ "\t\t=> Birthdate: ["+ ((Person)response).getBirthdate() +"]\n";
		}
		if(response.getClass().equals(introsde.assignment3.soap.entities.Activity.class)) {
			OUTPUT += "\t\t=> ID: ["+ ((Activity)response).getId() +"]\n"
					+ "\t\t=> Name: ["+ ((Activity)response).getName() +"]\n"
					+ "\t\t=> Place: ["+ ((Activity)response).getPlace() +"]\n"
					+ "\t\t=> Description: ["+ ((Activity)response).getDescription() +"]\n"
					+ "\t\t=> Start Date: ["+ ((Activity)response).getStartdate() +"]\n"
					+ "\t\t=> Preference: ["+ ((Activity)response).getPreference() +"]\n"
					+ "\t\t\t=> Type: ["+ ((Activity)response).getType().getId() +"]\n";
		}
		System.out.println(OUTPUT);
		try {
			writer.write(OUTPUT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void printMessage(String message) {
		String OUTPUT = "\t\t\t TEST MESSAGE: ["+message+"]\n";
		System.out.println(OUTPUT);
		try {
			writer.write(OUTPUT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static Person generateNewPerson() {
		Activity activity = generateActivity();
		Person person = new Person();												//Generate a new Person with that activity
		person.getActivitypreference().add(activity);
		person.setFirstname("Firstname");
		Random rand = new Random();
		person.setLastname("L+"+rand.nextInt(9999999));								//And a random lastname
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(new Date(System.currentTimeMillis()));
		try {
			person.setBirthdate(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
		} catch (DatatypeConfigurationException e) { }
		return person;
	}

	static Activity generateActivity() {
		ActivityType type = generateActivityType();
		Activity activity = new Activity();											//Generate a new Activity with that type
		activity.setDescription("Automatically Generated");
		activity.setName("Dota col Tonno - generated");
		activity.setPlace("A casa del Dezu - generated");
		activity.setType(type);
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(new Date(System.currentTimeMillis()));
		try {
			activity.setStartdate(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
		} catch (DatatypeConfigurationException e) { }
		return activity;
	}

	static ActivityType generateActivityType() {
		ActivityType type = new ActivityType();										//Generate a new Activity Type
		type.setId("Dota");
		return type;
	}
	
	public static void setWriter(Writer newWriter) {
		writer = newWriter;
	}
	public static Writer getWriter() {
		return writer;
	}
	public static void closeWriter() {
		try {
			writer.close();
		} catch (IOException e) {
		}
	}
	
	
}