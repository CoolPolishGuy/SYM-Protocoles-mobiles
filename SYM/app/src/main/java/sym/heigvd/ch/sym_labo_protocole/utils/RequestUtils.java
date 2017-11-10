package sym.heigvd.ch.sym_labo_protocole.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Utility class to send request
 * @author Tano Iannetta, Lara Chauffoureaux and Wojciech Myszkorovski
 */
public class RequestUtils{

    /**
     * HTTP parameters
     */
    private static final String METHOD = "POST";
    private static final String USER_AGENT = "Mozilla/5.0";

    /**
     * Send a request
     * @return response of the request
     */
    public static String sendRequest(String request, String url, String contentType) {

        String response = "";

        try {
            // connection
            URL url_object = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url_object.openConnection();

            // headers
            connection.setRequestMethod(METHOD);
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Content-Type", contentType);
            connection.setDoOutput(true);

            // send
            OutputStream os = connection.getOutputStream();
            os.write(request.getBytes());
            os.flush();
            os.close();

            // response
            int responseCode = connection.getResponseCode();

            // ok
            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                // reading answer
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;

                while ((line = in.readLine()) != null)
                {
                    response += line;
                }
                in.close();
            }
            else
            {
                // response message
                response = connection.getResponseMessage();
            }

        }catch (Exception e)
        {
            Log.e(RequestUtils.class.getName(), "error: " + e.getMessage(), e);
            response = e.getMessage();
        }

        return response;
    }
}
