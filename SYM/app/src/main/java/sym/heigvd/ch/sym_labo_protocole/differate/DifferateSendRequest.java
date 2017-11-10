package sym.heigvd.ch.sym_labo_protocole.differate;

import android.os.AsyncTask;
import sym.heigvd.ch.sym_labo_protocole.utils.CommunicationEventListener;
import sym.heigvd.ch.sym_labo_protocole.utils.RequestUtils;

/**
 * Send a request to the server
 * This class is similar to the AsyncSendRequest, however we want to keep
 * those classes separate to have a more scalable implantation
 * @author Tano Iannetta, Lara Chauffoureaux and Wojciech Myszkorowski
 */
public class DifferateSendRequest  extends AsyncTask<String,String,String> {
    private CommunicationEventListener listener;

    @Override
    protected String doInBackground(String... arguments) {
        String response = RequestUtils.sendRequest(arguments[0], arguments[1], arguments[2]);
        return response;
    }

    @Override
    public void onPostExecute(String result) {
        super.onPostExecute(result);
        listener.handleServerResponse(result);
    }

    public void setCommunicationEventListener(CommunicationEventListener l)
    {
        this.listener = l;
    }
}
