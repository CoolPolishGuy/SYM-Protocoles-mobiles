package sym.heigvd.ch.sym_labo_protocole.async;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import sym.heigvd.ch.sym_labo_protocole.MainActivity;
import sym.heigvd.ch.sym_labo_protocole.R;
import sym.heigvd.ch.sym_labo_protocole.object.ObjectActivity;
import sym.heigvd.ch.sym_labo_protocole.utils.CommunicationEventListener;

public class AsyncActivity extends AppCompatActivity {

    private EditText receivedData;
    private EditText toSendData;
    private Button send;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async);

        // Recuperate UI things
        this.receivedData = (EditText) findViewById(R.id.receivedData);
        this.toSendData = (EditText) findViewById(R.id.toSendData);
        this.send = (Button) findViewById(R.id.send);
        this.spinner = (Spinner) findViewById(R.id.spinner);

        toSendData.setText("{ \"nom\": \"hacker\"}");

        // Sender settings
        final AsyncSendRequest sender = new AsyncSendRequest();
        sender.setCommunicationEventListener(new CommunicationEventListener() {
            public boolean handleServerResponse(final String response) {

                // Code de traitement de la réponse – dans le UI-Thread
                AsyncActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Update view
                        receivedData.setText(response);
                    }
                });

                return true;
            }
        });

        // Action associated to "Async" button
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String spinnerText = String.valueOf(spinner.getSelectedItem());
                String url = "http://sym.iict.ch/rest/json";
                String contentType = "application/json";

                switch (spinnerText) {
                    case "JSON" :
                        url = "http://sym.iict.ch/rest/json";
                        contentType = "application/json";
                        break;
                    case "XML" :
                        // TODO
                        break;
                    case "Plain text" :
                        // TODO
                        break;
                }

                sender.execute(toSendData.getText().toString(), url, contentType);
            }
        });
    }
}
