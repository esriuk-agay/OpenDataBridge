package esride.opendatabridge.agolclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import esride.opendatabridge.httptransport.HTTPRequest;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Class which implements the Generate Token Request.
 * See API details for a better parameter understanding here: http://resources.arcgis.com/en/help/arcgis-rest-api/index.html#/Generate_Token/02r3000000m5000000/
 * User: Markus Stecker, con terra GmbH
 * Date: 04.12.13
 * Time: 17:13
 */
public class AgolGenerateTokenRequest {

    private static Logger sLogger = Logger.getLogger(AgolGenerateTokenRequest.class);

    private static final String usernameParam = "username";
    private String usernameValue;

    private static final String passwdParam = "password";
    private String passwordValue;

    private static final String refererParam = "referer";
    private String refererValue;
    private static final String refererValueDefault = "http://www.esri.com";

    private static final String formatParam = "f";
    private static final String jsonFormatValueDefault = "json";

    private String url;

    public AgolGenerateTokenRequest(String pUsernameValue, String pPasswordValue, String pRefererValue, String pUrl) {
        if (sLogger.isDebugEnabled()) {
            sLogger.debug("Generate Token Request");
            sLogger.debug(usernameParam + ": " + pUsernameValue);
            sLogger.debug("password-Length: " + pPasswordValue.length());
            sLogger.debug(refererParam + ": " + pRefererValue);
            sLogger.debug("URL: " + pUrl);
        }

        if(pUsernameValue == null || pUsernameValue.trim().length() == 0){
            throw new IllegalArgumentException("missing username for token generation");
        }
        usernameValue = pUsernameValue;
        if(pPasswordValue == null || pPasswordValue.trim().length() == 0){
            throw new IllegalArgumentException("missing password for token generation");
        }
        passwordValue = pPasswordValue;

        if(pUrl == null || pUrl.trim().length() == 0){
            throw new IllegalArgumentException("missing url for token generation");
        }
        url = pUrl;
        if(pRefererValue == null){
            refererValue = refererValueDefault;
        }else{
            refererValue = pRefererValue;
        }
        refererValue = pRefererValue;
    }

    public AgolGenerateTokenResponse excReqWithJsonResp(HTTPRequest pHttpRequest, ObjectMapper pObjectMapper) throws IOException {
        HashMap<String, String> agolAttributes = new HashMap<String, String>();
        agolAttributes.put(formatParam, jsonFormatValueDefault);
        agolAttributes.put(usernameParam, usernameValue);
        agolAttributes.put(passwdParam, passwordValue);
        agolAttributes.put(refererParam, refererValue);

        InputStream entities = pHttpRequest.executePostRequest(url, agolAttributes, null);

        if (entities != null)
        {
            JsonNode rootNode = pObjectMapper.readTree(entities);
            JsonNode tokenNode = rootNode.get("token");
            if(tokenNode != null){
                String token = tokenNode.asText();
                Long tokenExpiresLong = rootNode.get("expires").asLong();
                Date resultdate = new Date(tokenExpiresLong);
                if (sLogger.isDebugEnabled()) {
                    sLogger.debug("Generate Token Response");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                    sLogger.debug("New token created. Expires at " + sdf.format(resultdate) + ".");
                }
                return new AgolGenerateTokenResponse(token, resultdate);
            }
            else{
                sLogger.error("No valid Token found in response: " + rootNode.toString());
                throw new IOException("No response from GenerateToken Request");

            }
        }else{
            throw new IOException("No response from GenerateToken Request");
        }
    }

}
