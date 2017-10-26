package sym.heigvd.ch.sym_labo_protocole.async;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import sym.heigvd.ch.sym_labo_protocole.utils.CommunicationEventListener;

public class AsyncSendRequest implements IAsyncSendRequest {

    private CommunicationEventListener listener;
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String CONTENT_TYPE = "application/json";

    @Override
    public String sendRequest(String request, String url) {

        String answer = null;

        try {
            // Connection opening
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Headers setting
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Content-Type", CONTENT_TYPE);
            con.setDoOutput(true);

            // Write of the data
            OutputStream os = con.getOutputStream();
            os.write(request.getBytes());
            os.flush();
            os.close();

            // Recuperate the answer
            int responseCode = con.getResponseCode();

            // If everything is ok
            if (responseCode == HttpURLConnection.HTTP_OK) {

                // Reading body of the answer
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    answer += inputLine;
                }

                in.close();
            }
            else {
                // Printing the response message
                answer = con.getResponseMessage();
            }
        } catch (Exception e) {

            // For debug purpose
            System.err.println("error in HTTP things : \n" + e.getMessage());
            answer = e.getMessage();
        }

        listener.handleServerResponse(answer);

        // TODO on retourne quoi?
        return null;
    }

    @Override
    public void setCommunicationEventListener(CommunicationEventListener l) {

        this.listener = l;
    }
}
