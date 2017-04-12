package com.amplearch.beaconshop.Utils;

/**
 * Created by admin on 04/04/2017.
 */


public class CallSoap
{
    public final String SOAP_ACTION = "http://tempuri.org/login";

    public  final String OPERATION_NAME = "login";

    public  final String WSDL_TARGET_NAMESPACE = "http://tempuri.org/";

    public  final String SOAP_ADDRESS = "http://localhost:62330/SQLWebService.asmx";
    public CallSoap()
    {
    }
  /*  public String Call(String uname,String pwd)
    {
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
        PropertyInfo pi=new PropertyInfo();
        pi.setName("uname");
        pi.setValue(uname);
        pi.setType(String.class);
        request.addProperty(pi);
        pi=new PropertyInfo();
        pi.setName("pwd");
        pi.setValue(pwd);
        pi.setType(String.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response=null;
        try
        {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        }
        catch (Exception exception)
        {
            response=exception.toString();
        }
        return response.toString();
    }*/
}