package sym.heigvd.ch.sym_labo_protocole.differate;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import sym.heigvd.ch.sym_labo_protocole.utils.CommunicationEventListener;

/**
 * Send a request to the server
 * @author Tano Iannetta
 */
public class DifferateSendRequest  extends AsyncTask<String,String,String> {
    private CommunicationEventListener listener;

    public static final String METHOD = "POST";
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String CONTENT_TYPE = "text/plain";

    @Override
    protected String doInBackground(String... arguments) {
        sendRequest(arguments[0], arguments[1]);
        return "data sent";
    }

    @Override
    public void onPostExecute(String result) {
        super.onPostExecute(result);
        listener.handleServerResponse(result);
    }
    private void sendRequest(String request, String url) {
        String response = null;
        try {
            // connection
            URL url_object = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url_object.openConnection();

            // headers
            connection.setRequestMethod(METHOD);
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Content-Type", CONTENT_TYPE);
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
            Log.e("DifferateSendRequest", "error: " + e.getMessage(), e);
            response = e.getMessage();
        }

        //return response;
        // response in logs
        Log.e("Differate Response", response);
    }

    public void setCommunicationEventListener(CommunicationEventListener l)
    {
        this.listener = l;
    }
}
