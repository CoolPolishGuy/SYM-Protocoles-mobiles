package sym.heigvd.ch.sym_labo_protocole.compressed;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.text.style.AlignmentSpan;
import android.util.Log;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.zip.*;

import sym.heigvd.ch.sym_labo_protocole.utils.CommunicationEventListener;

import static java.util.zip.Deflater.BEST_COMPRESSION;

/**
 * Created by wojtek on 10/27/17.
 */

public class CompressSendRequest extends AsyncTask<String, String, String> {

    private CommunicationEventListener listener;
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String CONTENT_TYPE = "application/json";

    @Override
    public String doInBackground(String[] arguments) {
        return sendRequest(arguments[0], arguments[1]);
    }

    @Override
    public void onPostExecute(String result) {
        super.onPostExecute(result);
        listener.handleServerResponse(result);
    }
    private String sendRequest(String request, String url) {

        String answer = null;

        try {
            // Connection opening
            URL url_object = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url_object.openConnection();

            // Headers setting
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Content-Type", CONTENT_TYPE);
            connection.setRequestProperty("X-Network","CSD");
            connection.setRequestProperty("X-Content-Encoding","deflate");

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
                in = new InflaterInputStream(in,new Inflater(true));
                BufferedReader incoming = new BufferedReader(new InputStreamReader(in));

                String line ;
                while ((line = incoming.readLine()) != null) {
                    answer+=line;
                }
                incoming.close();

            }
            else {
                // Printing the response message
                answer = connection.getResponseMessage();
            }
        } catch (Exception e) {

            // For debug purpose
            Log.e("AsyncSendRequest","error in HTTP things : " + e.getMessage(), e);
            answer = e.getMessage();
        }

        return answer;
    }

    public void setCommunicationEventListener(CommunicationEventListener l) {

        this.listener = l;
    }
}
