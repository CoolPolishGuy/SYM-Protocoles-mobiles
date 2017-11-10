package sym.heigvd.ch.sym_labo_protocole.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Utility class to send an http request
 *
 * @author Tano Iannetta, Lara Chauffoureaux, Wojciech Myszkorovski
 */
public class RequestUtils {

    // HTTP parameters and headers
    private static final String METHOD = "POST";
    private static final String USER_AGENT = "Mozilla/5.0";

    /**
     * Static method sending a post request to a given url.
     *
     * @param request     The data to send
     * @param url         The URL where to send the request
     * @param contentType The content-type of the data
     * @return The server's response body if everything was ok, the error code otherwise
     */
    public static String sendRequest(String request, String url, String contentType) {

        String response = "";

        try {
            // Connection opening
            URL url_object = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url_object.openConnection();

            // Setting of the headers and method
            connection.setRequestMethod(METHOD);
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Content-Type", contentType);
            connection.setDoOutput(true);

            // Setting of the data and sending to the server
            OutputStream os = connection.getOutputStream();
            os.write(request.getBytes());
            os.flush();
            os.close();

            // Response code recuperation
            int responseCode = connection.getResponseCode();

            // If everything went well
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Reading response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;

                while ((line = in.readLine()) != null) {
                    response += line;
                }
                in.close();
            } else {
                // We return the error code
                response = connection.getResponseMessage();
            }

        } catch (Exception e) {
            Log.e(RequestUtils.class.getName(), "error: " + e.getMessage(), e);
            response = e.getMessage();
        }

        return response;
    }
}
