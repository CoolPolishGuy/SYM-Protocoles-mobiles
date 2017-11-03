package sym.heigvd.ch.sym_labo_protocole.async;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import sym.heigvd.ch.sym_labo_protocole.utils.CommunicationEventListener;

public class AsyncSendRequest extends AsyncTask<String, String, String> {

    private CommunicationEventListener listener;
    private static final String USER_AGENT = "Mozilla/5.0";

    @Override
    public String doInBackground(String[] arguments) {

        return sendRequest(arguments[0], arguments[1], arguments[2]);
    }

    @Override
    public void onPostExecute(String result) {
        super.onPostExecute(result);
        listener.handleServerResponse(result);
    }

    private String sendRequest(String request, String url, String contentType) {

        String answer = null;

        try {
            // Connection opening
            URL url_object = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) url_object.openConnection();

            // Headers setting
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Content-Type", contentType);
            connection.setDoOutput(true);

            // Write of the data
            OutputStream os = connection.getOutputStream();
            os.write(request.getBytes());
            os.flush();
            os.close();

            // Recuperate the answer
            int responseCode = connection.getResponseCode();

            // If everything is ok
            if (responseCode == HttpURLConnection.HTTP_OK) {

                // Reading body of the answer
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    answer += inputLine;
                }

                in.close();
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
