package sym.heigvd.ch.sym_labo_protocole.compressed;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.*;

import sym.heigvd.ch.sym_labo_protocole.utils.CommunicationEventListener;

import static java.util.zip.Deflater.BEST_COMPRESSION;

/**
 * Java asynchronous activity used to send a compressed request to the server.
 *
 * @author Tano Iannetta, Lara Chauffoureaux, Wojciech Myszkorowski
 */
public class CompressSendRequest extends AsyncTask<String, String, String> {

    private CommunicationEventListener listener;                    // The listener to notify
    private static final String USER_AGENT = "Mozilla/5.0";         // The user-agent header
    private static final String CONTENT_TYPE = "application/json";  // The content-type header
    private static final String END_OF_RESP = "PHP_SELF";           // String to find end of response

    @Override
    public String doInBackground(String[] arguments) {
        return sendRequest(arguments[0], arguments[1]);
    }

    @Override
    public void onPostExecute(String result) {
        super.onPostExecute(result);
        listener.handleServerResponse(result);
    }

    /**
     * Method sending a post request to a given url.
     *
     * @param request The data to send
     * @param url     The URL where to send the request
     * @return The server's response body if everything was ok, the error code otherwise
     */
    private String sendRequest(String request, String url) {

        String answer = "";

        try {
            // Connection opening
            URL url_object = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url_object.openConnection();

            // Headers setting
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Content-Type", CONTENT_TYPE);
            connection.setRequestProperty("X-Network", "CSD");
            connection.setRequestProperty("X-Content-Encoding", "deflate");
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();

            // Write of the data & compression
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new DeflaterOutputStream(os, new Deflater(BEST_COMPRESSION, true))));

            out.write(request);
            out.flush();
            out.close();

            // Recuperate the answer
            int responseCode = connection.getResponseCode();

            // If everything is ok
            if (responseCode == HttpURLConnection.HTTP_OK) {

                // Reading body of the answer
                // Decompress the bytes
                InputStream in = connection.getInputStream();
                in = new InflaterInputStream(in, new Inflater(true));
                BufferedReader incoming = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = incoming.readLine()) != null) {
                    answer += line;
                }
                incoming.close();
            } else {
                // Printing the response message
                answer = connection.getResponseMessage();
            }
        } catch (Exception e) {

            // For debug purpose
            Log.e("CompressSendRequest", "error in HTTP things : " + e.getMessage(), e);
            answer = e.getMessage();
        }

        return answer;
    }

    public void setCommunicationEventListener(CommunicationEventListener l) {

        this.listener = l;
    }
}
