package sym.heigvd.ch.sym_labo_protocole.async;

import sym.heigvd.ch.sym_labo_protocole.utils.CommunicationEventListener;

public interface IAsyncSendRequest {

    String sendRequest(String request, String url);

    void setCommunicationEventListener(CommunicationEventListener l);
}
