package Facebook_Java;

import com.visural.common.StringUtil;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AuthenticationProperties 
{
    // get these from your FB Dev App
    private static final String api_key = "161665943942708";
    private static final String secret = "70b0bb7b810ef0b9bcb2844ab633af80";
    private static final String client_id = "161665943942708";  

    // set this to your servlet URL for the authentication servlet/filter
    private static final String redirect_uri = //"http://localhost:8080/FacebookWebApplication/authentication";
            "http://shoopi.appspot.com/temp";
    
    // set this to the list of extended permissions you want
    private static final String[] perms = new String[] {"publish_stream", "read_stream"};
    
    

    public static String getAPIKey() {
        return api_key;
    }

    public static String getSecret() {
        return secret;
    }
    
    public static String getLoginRedirectURL() 
    {
        String result = "https://graph.facebook.com/oauth/authorize?client_id=" +
            client_id + "&display=page&redirect_uri=" +
            redirect_uri + "&scope=" + StringUtil.delimitObjectsToString(",", perms);
        return result;
    }
    
    public static String getAuthURL(String authCode) {
        String result = "https://graph.facebook.com/oauth/access_token?client_id=" +
            client_id + "&redirect_uri=" +
            redirect_uri + "&client_secret=" + secret + "&code=" + encode(authCode);
        return result;
    }
    
    /*The same needs to be done when building the uri in the authFacebookLogin in the 
    ‘UserService’ for the access token.*/
    
    public static String encode(String authCode) 
    {
        String encodedAuthCode = null;
        try {
            encodedAuthCode = URLEncoder.encode(authCode, "UTF-8");
        } 
        catch (UnsupportedEncodingException e) {
        // This should never happen, we have specified UTF-8 correctly, Log error
        }
        return encodedAuthCode;
    }
}
