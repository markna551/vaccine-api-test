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

    /**
     *
     * Get connection form end point and use GET request.
     * @param url is url of end point
     * @return connection of http URL connection
     *
     * @throws IOException from URL and connection.
     */
    private HttpURLConnection getConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        return connection;
    }


    /**
     * TestID: 1
     * check URL can GET data of all people from end point.
     *
     * status is 200
     *
     * @throws IOException from URL and connection.
     */
    @Test
    public void testGetAllPeople() throws IOException {
        URL urlEndPoint = new URL(url + "/people/all");
        HttpURLConnection connection = getConnection(urlEndPoint);
        assertEquals(200,connection.getResponseCode());
        assertEquals("application/json; charset=utf-8",connection.getHeaderField("Content-Type"));
    }

    /**
     * TestID: 2
     * check URL can GET data of people by date from end point.
     *
     * status is 200
     *
     * @throws IOException from URL and connection.
     */
    @Test
    public void testGetPeopleByDate() throws IOException {
        URL urlEndPoint = new URL(url + "/people/by_date/20-10-2021");
        HttpURLConnection connection = getConnection(urlEndPoint);
        assertEquals(200,connection.getResponseCode());
        assertEquals("OK",connection.getResponseMessage());
        assertEquals("application/json; charset=utf-8",connection.getHeaderField("Content-Type"));
    }

    /**
     * TestID: 3
     * check URL can GET data of people by date from end point.
     * day for test is today
     * status is 200
     *
     * @throws IOException from URL and connection.
     */
    @Test
    public void testGetPeopleByTodayDate() throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();
        URL urlEndPoint = new URL(url + "/people/by_date/" + dtf.format(now));
        HttpURLConnection connection = getConnection(urlEndPoint);
        assertEquals(200,connection.getResponseCode());
        assertEquals("OK",connection.getResponseMessage());

    }

    /**
     * TestID: 4
     * check URL can GET data of people by date from end point.
     * day for test is previous day
     * status is 200
     *
     * @throws IOException from URL and connection.
     */
    @Test
    public void testGetPeopleByPreviousDate() throws IOException {
        URL urlEndPoint = new URL(url + "/people/by_date/20-9-2021");
        HttpURLConnection connection = getConnection(urlEndPoint);
        assertEquals(200,connection.getResponseCode());
        assertEquals("OK",connection.getResponseMessage());

    }

    /**
     * TestID: 5
     * check URL can GET data of people by date from end point.
     * day for test is empty date
     * status is 202
     *
     * @throws IOException from URL and connection.
     */
    @Test
    public void testGetPeopleByNoDate() throws IOException {
        URL urlEndPoint = new URL(url + "/people/by_date/");
        HttpURLConnection connection = getConnection(urlEndPoint);
        assertEquals(406,connection.getResponseCode());
        assertEquals("Not Acceptable",connection.getResponseMessage());

    }

    /**
     * TestID: 6
     * check URL can GET data of people by date from end point.
     * day for test is invalid because set month to 13.
     * status is 404
     *
     * @throws IOException from URL and connection.
     */
    @Test
    public void testGetPeopleByInvalidMonth() throws IOException {
        URL urlEndPoint = new URL(url + "/people/by_date/20-13-2021");
        HttpURLConnection connection = getConnection(urlEndPoint);
        assertEquals(404,connection.getResponseCode());
    }

    /**
     * TestID: 7
     * check URL can GET data of people by date from end point.
     * day for test is invalid because set day and month to String.
     * status is 404
     *
     * @throws IOException from URL and connection.
     */
    @Test
    public void testGetPeopleByInvalidValue() throws IOException {
        URL urlEndPoint = new URL(url + "/people/by_date/ten-twelve-2021");
        HttpURLConnection connection = getConnection(urlEndPoint);
        assertEquals(404,connection.getResponseCode());
    }

    /**
     * TestID: 8
     * check URL can GET data of people by date from end point.
     * day for test is invalid because set date to String.
     * status is 404
     *
     * @throws IOException from URL and connection.
     */
    @Test
    public void testGetPeopleByInvalidValueNotDate() throws IOException {
        URL urlEndPoint = new URL(url + "/people/by_date/test");
        HttpURLConnection connection = getConnection(urlEndPoint);
        assertEquals(404,connection.getResponseCode());
    }

    /**
     * TestID: 9
     * check URL can GET data of people by date from end point.
     * day for test is invalid because set year to future
     * and message should return No Content.
     * status is 204
     *
     * @throws IOException from URL and connection.
     */
    @Test
    public void testGetPeopleByFutureYear() throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM");
        LocalDateTime now = LocalDateTime.now();
        int nextYear = now.getYear() + 1;
        String year = dtf.format(now) + "-" + nextYear;
        URL urlEndPoint = new URL(url + "/people/by_date/" + year);
        HttpURLConnection connection = getConnection(urlEndPoint);
        assertEquals(204,connection.getResponseCode());
        assertEquals("No Content",connection.getResponseMessage());
    }

    /**
     * TestID: 10
     * Check data that get form end point is correct.
     *
     * status is 200
     *
     * @throws IOException from URL and connection.
     * @throws ParseException from JSONParser
     */
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
        assertEquals(200,connection.getResponseCode());
    }


}
