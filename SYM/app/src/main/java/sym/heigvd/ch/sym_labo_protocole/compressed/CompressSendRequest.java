package sym.heigvd.ch.sym_labo_protocole.compressed;

import android.os.AsyncTask;
import android.util.Log;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.*;

import sym.heigvd.ch.sym_labo_protocole.utils.CommunicationEventListener;

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
            //connection.setRequestProperty("X-Content-Encoding","deflate");
            connection.setDoOutput(true);

            // Write of the data



            byte[] input = request.getBytes("UTF-8");
            byte[] output = new byte[1024];
            Deflater d = new Deflater();
            d.setInput(input);
            d.finish();
            int compressedDataLength = d.deflate(output);
            d.end();



            OutputStream os = connection.getOutputStream();
            os.write(output);
            os.flush();
            os.close();
            /*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(request.length());
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);

            gzipOutputStream.write(request.getBytes());
            gzipOutputStream.close();



            byte[] compressed = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();

            OutputStream os = connection.getOutputStream();
            os.write(compressed);
            os.flush();
            os.close();*/


            // Recuperate the answer
            int responseCode = connection.getResponseCode();

            // If everything is ok
            if (responseCode == HttpURLConnection.HTTP_OK) {

                // Reading body of the answer

                // Decompress the bytes

                Inflater decompresser = new Inflater();
                //decompresser.setInput(output, 0, compressedDataLength);
                byte[] result = new byte[100];
                int resultLength = decompresser.inflate(result);
                decompresser.end();
                String outputString = new String(result, 0, resultLength, "UTF-8");


                /*Inflater decompresser = new Inflater();
                decompresser.setInput(output, 0, compressedDataLength);

                InputStream in = new InputStreamReader(connection.getInputStream());
                InflaterInputStream ini =  new InputStreamReader(in);
                ByteArrayOutputStream bout =new ByteArrayOutputStream(512);
                int inputLine;


                while ((inputLine = ini.read()) != -1) {
                    bout.write(inputLine);
                }

                in.close();*/
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
