package unitn.dallatorre.client;

import java.io.IOException;
import java.io.Writer;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

public class ClientEvaluation {
	// Method used to print the required debug message and extract the most out of the objects
	//response is the response, which should be already buffered. WebTarget is the target used to launch the request
	//RequestSequenceNumber only indicates the request # by exercise requirements
	//restMethod indicates weather the request was PUT, POST, GET, ... I was unable to extract this from the response and the target
	//content type indicates the type of the payload sent
	//result is the boolean value that indicates the failure or the success of the test
	
	private static Writer writer;
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
	
	public static void printResponse(Response response, WebTarget webTarget, int requestSequenceNumber, String restMethod, String contentType, Boolean result) {
		String resString = ((result) ? "OK" : "ERROR");
		String OUTPUT = "Request #["+requestSequenceNumber+"]: ["+restMethod+"] ["+webTarget.getUri()+"] Accept: ["+response.getMediaType()+"] Content-type: ["+contentType+"] \n" + 
							"\t=> Result: ["+resString+"]\n" + 
							"\t=> HTTP Status: ["+response.getStatus()+"]\n";
		System.out.println(OUTPUT);
		if ( response.getStatus()!=204 ) {
			if( response.getMediaType().equals(MediaType.APPLICATION_XML_TYPE) ) {
				OUTPUT += XmlFormatter.format(response.readEntity(String.class));
				//If the payload is XML, pretty print it.
			}
			else if( response.getMediaType().equals(MediaType.APPLICATION_JSON_TYPE) ) {
				JSONObject json = new JSONObject(response.readEntity(String.class));
				OUTPUT += json.toString(2) ;
				//If the payload is JSON, pretty print it.
			}
			else {
				OUTPUT += response.readEntity(String.class);
			}
		}
		OUTPUT += "\n";
		try {
			writer.write(OUTPUT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}