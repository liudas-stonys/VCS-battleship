package lt.liudas_stonys.laivu_musis.services;

import lt.liudas_stonys.laivu_musis.entities.Coordinate;
import lt.liudas_stonys.laivu_musis.entities.Event;
import lt.liudas_stonys.laivu_musis.entities.Game;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class WebService {

    public static final String SERVER_URL = "http://miskoverslas.lt/laivu_musis/";

    private LocalGameService localGameService = new LocalGameService();

    public Game performHttpRequest(String url)throws IOException, ParseException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        HttpResponse response = client.execute(request);
        String responseAsString = convertInputStreamToString(response.getEntity().getContent());

        if (response.getStatusLine().getStatusCode() == 200) {
            return convertJsonResponseToGame(responseAsString);
        }
        System.out.println("Error response: " + responseAsString);
        return null;
    }

    private Game convertJsonResponseToGame(String response) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonGame = (JSONObject) parser.parse(response);

        String gameId = (String) jsonGame.get("id");
        String status = (String) jsonGame.get("status");
        String nextTurnForUserId = (String) jsonGame.get("nextTurnForUserId");
        String winnerUserId = (String) jsonGame.get("winnerUserId");

        JSONArray jsonColumns = (JSONArray) jsonGame.get("columns");
        List<String> columns = new ArrayList<>();
        for (Object jsonObject : jsonColumns) {
            columns.add((String) jsonObject);
        }
        JSONArray jsonRows = (JSONArray) jsonGame.get("rows");
        List<Integer> rows = new ArrayList<>();
        for (Object jsonObject : jsonRows) {
            rows.add(Math.toIntExact((Long) jsonObject));
        }

        List<Event> events = new ArrayList<>();
        JSONArray jsonEvents = (JSONArray) jsonGame.get("events");

        for (Object jsonEventObject : jsonEvents) {
            JSONObject eventObj = (JSONObject) jsonEventObject;

            Long date = (Long) eventObj.get("date");

            JSONObject coordinateObj = (JSONObject) eventObj.get("coordinate");
            String column = (String) coordinateObj.get("column");
            Long row = (Long) coordinateObj.get("row");
            Coordinate coordinate = localGameService.createCoordinate(column, Math.toIntExact(row));
//            Coordinate coordinate = localGameService.parseCoordinate(column + row);

            String userId = (String) eventObj.get("userId");
            Boolean hit = (Boolean) eventObj.get("hit");

            events.add(new Event(date, coordinate, userId, hit));
        }

        return new Game(columns, rows, gameId, nextTurnForUserId, status, winnerUserId, events);
    }

    public String convertInputStreamToString(InputStream inputStream) throws IOException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, "UTF-8");
        return writer.toString();
    }
}
