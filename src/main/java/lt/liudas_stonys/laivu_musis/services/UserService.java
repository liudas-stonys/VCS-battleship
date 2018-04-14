package lt.liudas_stonys.laivu_musis.services;

import lt.liudas_stonys.laivu_musis.entities.User;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class UserService extends WebService {

    public static final String CREATE_USER_METHOD = "create_user?";

    public User createUser(String name, String email) throws IOException, ParseException {
        StringBuilder url = new StringBuilder(SERVER_URL);
        url.append(CREATE_USER_METHOD);
        url.append("name=").append(name).append("&");
        url.append("email=").append(email);

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url.toString());

        HttpResponse response = client.execute(request);

        String responseAsString = convertInputStreamToString(response.getEntity().getContent());

        return convertJsonToUser(responseAsString);
    }

    private User convertJsonToUser(String response) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonUser = (JSONObject) parser.parse(response);

        String userId = (String) jsonUser.get("id");
        String userName = (String) jsonUser.get("name");
        String userEmail = (String) jsonUser.get("email");
        return new User(userId, userName, userEmail);
    }
}
