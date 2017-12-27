
package introsde.assignment3.soap;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "PersonImplService", targetNamespace = "http://soap.assignment3.introsde/", wsdlLocation = "http://localhost:6901/soap/person?wsdl")
public class PersonImplService
    extends Service
{

    private final static URL PERSONIMPLSERVICE_WSDL_LOCATION;
    private final static WebServiceException PERSONIMPLSERVICE_EXCEPTION;
    private final static QName PERSONIMPLSERVICE_QNAME = new QName("http://soap.assignment3.introsde/", "PersonImplService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:6901/soap/person?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        PERSONIMPLSERVICE_WSDL_LOCATION = url;
        PERSONIMPLSERVICE_EXCEPTION = e;
    }

    public PersonImplService() {
        super(__getWsdlLocation(), PERSONIMPLSERVICE_QNAME);
    }

    public PersonImplService(WebServiceFeature... features) {
        super(__getWsdlLocation(), PERSONIMPLSERVICE_QNAME, features);
    }

    public PersonImplService(URL wsdlLocation) {
        super(wsdlLocation, PERSONIMPLSERVICE_QNAME);
    }

    public PersonImplService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, PERSONIMPLSERVICE_QNAME, features);
    }

    public PersonImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public PersonImplService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns PersonWebService
     */
    @WebEndpoint(name = "PersonImplPort")
    public PersonWebService getPersonImplPort() {
        return super.getPort(new QName("http://soap.assignment3.introsde/", "PersonImplPort"), PersonWebService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns PersonWebService
     */
    @WebEndpoint(name = "PersonImplPort")
    public PersonWebService getPersonImplPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://soap.assignment3.introsde/", "PersonImplPort"), PersonWebService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (PERSONIMPLSERVICE_EXCEPTION!= null) {
            throw PERSONIMPLSERVICE_EXCEPTION;
        }
        return PERSONIMPLSERVICE_WSDL_LOCATION;
    }

}