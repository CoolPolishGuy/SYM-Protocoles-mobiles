package sym.heigvd.ch.sym_labo_protocole.async;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import sym.heigvd.ch.sym_labo_protocole.utils.CommunicationEventListener;
import sym.heigvd.ch.sym_labo_protocole.utils.RequestUtils;

/**
 * Send a request to the server
 * This class is similar to the DifferateSendRequest, however we want to keep
 * those classes separate to have a more scalable implantation
 * @author Tano Iannetta, Lara Chauffoureaux and Wojciech Myszkorowski
 */
public class AsyncSendRequest extends AsyncTask<String, String, String> {

    private CommunicationEventListener listener;

    @Override
    public String doInBackground(String[] arguments) {

        return RequestUtils.sendRequest(arguments[0], arguments[1], arguments[2]);
    }

    @Override
    public void onPostExecute(String result) {

        super.onPostExecute(result);
        listener.handleServerResponse(result);
    }

    public void setCommunicationEventListener(CommunicationEventListener l) {

        this.listener = l;
    }
}
