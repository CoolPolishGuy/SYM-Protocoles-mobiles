package sym.heigvd.ch.sym_labo_protocole.async;

import android.os.AsyncTask;

import sym.heigvd.ch.sym_labo_protocole.utils.CommunicationEventListener;
import sym.heigvd.ch.sym_labo_protocole.utils.RequestUtils;

/**
 * Java asynch activity used to send a request to the server.
 * This class is similar to the DifferateSendRequest and ObjectSendRequest, however we want to keep
 * those classes separate to have a more scalable implementation.
 *
 * @author Tano Iannetta, Lara Chauffoureaux, Wojciech Myszkorowski
 */
public class AsyncSendRequest extends AsyncTask<String, String, String> {

    private CommunicationEventListener listener;    // The listener to notify

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
