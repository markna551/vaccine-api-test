import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
public class ApiTest {
    private final String url = "https://suchonsite-server.herokuapp.com";

    private HttpURLConnection getConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        return connection;
    }

    @Test
    public void testGetAllPeople() throws IOException {
        URL urlEndPoint = new URL(url + "/people/all");
        HttpURLConnection connection = getConnection(urlEndPoint);
        assertEquals(200,connection.getResponseCode());
        assertEquals("application/json; charset=utf-8",connection.getHeaderField("Content-Type"));
    }

    @Test
    public void testGetPeopleByDate() throws IOException {
        URL urlEndPoint = new URL(url + "/people/by_date/20-10-2021");
        HttpURLConnection connection = getConnection(urlEndPoint);
        assertEquals(200,connection.getResponseCode());
        assertEquals("application/json; charset=utf-8",connection.getHeaderField("Content-Type"));
    }

    @Test
    public void testGetPeopleByTodayDate() throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();
        URL urlEndPoint = new URL(url + "/people/by_date/" + dtf.format(now));
        HttpURLConnection connection = getConnection(urlEndPoint);
        assertEquals(200,connection.getResponseCode());

    }

    @Test
    public void testGetPeopleByNoDate() throws IOException {
        URL urlEndPoint = new URL(url + "/people/by_date/");
        HttpURLConnection connection = getConnection(urlEndPoint);
        assertEquals(404,connection.getResponseCode());

    }

    @Test
    public void testGetPeopleByInvalidMonth() throws IOException {
        URL urlEndPoint = new URL(url + "/people/by_date/20-13-2021");
        HttpURLConnection connection = getConnection(urlEndPoint);
        assertEquals(404,connection.getResponseCode());
    }

    @Test
    public void testGetPeopleByInvalidValue() throws IOException {
        URL urlEndPoint = new URL(url + "/people/by_date/ten-twelve-2021");
        HttpURLConnection connection = getConnection(urlEndPoint);
        assertEquals(404,connection.getResponseCode());
    }

    @Test
    public void testGetPeopleByInvalidValueNotDate() throws IOException {
        URL urlEndPoint = new URL(url + "/people/by_date/test");
        HttpURLConnection connection = getConnection(urlEndPoint);
        assertEquals(404,connection.getResponseCode());
    }

    @Test
    public void testGetPeopleByFutureYear() throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM");
        LocalDateTime now = LocalDateTime.now();
        int nextYear = now.getYear() + 1;
        String year = dtf.format(now) + "-" + nextYear;
        URL urlEndPoint = new URL(url + "/people/by_date/" + year);
        HttpURLConnection connection = getConnection(urlEndPoint);
        assertEquals(404,connection.getResponseCode());
    }


    @Test
    public void testGetPeopleCorrectDate() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        URL urlEndPoint = new URL(url + "/people/by_date/20-10-2021");
        HttpURLConnection connection = getConnection(urlEndPoint);
        InputStream in = connection.getInputStream();
        BufferedReader buf = new BufferedReader(new InputStreamReader(in));
        JSONObject data = null;
        String thisLine;
        while ((thisLine = buf.readLine()) != null ) {
            data = (JSONObject) parser.parse(thisLine);
        }
        assertEquals("20-10-2021",data.get("date"));
    }

}
