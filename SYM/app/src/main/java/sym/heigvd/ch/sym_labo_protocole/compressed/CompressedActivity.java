package sym.heigvd.ch.sym_labo_protocole.compressed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import sym.heigvd.ch.sym_labo_protocole.R;
import sym.heigvd.ch.sym_labo_protocole.async.AsyncSendRequest;
import sym.heigvd.ch.sym_labo_protocole.utils.CommunicationEventListener;

public class CompressedActivity extends AppCompatActivity {

    private EditText receivedData;
    private EditText toSendData;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async);

        // Recuperate UI things
        this.receivedData = (EditText) findViewById(R.id.receivedData);
        this.toSendData = (EditText) findViewById(R.id.toSendData);
        this.send = (Button) findViewById(R.id.send);

        // "Compressed" button listener
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            // Sender settings
            final AsyncSendRequest sender = new AsyncSendRequest();
            sender.setCommunicationEventListener(new CommunicationEventListener() {
                public boolean handleServerResponse(final String response) {

                    // Code de traitement de la réponse – dans le UI-Thread
                    CompressedActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Update view
                            receivedData.setText(response);
                        }
                    });
                    // TODO Et là on retourne quoi?
                    return true;
                }
            });

            sender.execute(toSendData.getText().toString(), "http://sym.iict.ch/rest/json");
            }
        });
    }
}
