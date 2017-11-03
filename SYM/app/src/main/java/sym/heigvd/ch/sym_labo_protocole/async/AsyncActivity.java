package sym.heigvd.ch.sym_labo_protocole.async;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import sym.heigvd.ch.sym_labo_protocole.R;
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

        // Spinner listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                // Original text setting on spinner selection
                if (spinner.getSelectedItem().toString().equals(getString(R.string.XML))) {

                    toSendData.setText(R.string.xml_default_fill);

                } else if (spinner.getSelectedItem().toString().equals(getString(R.string.plain))) {

                    toSendData.setText(R.string.plain_default_fill);

                } else {

                    toSendData.setText(R.string.json_default_fill);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // "Asynch" button listener
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Recuperation of languages parameter (url + content)
                String[] languageParameters = recuperateLanguage();

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

                sender.execute(toSendData.getText().toString(), languageParameters[0], languageParameters[1]);
            }
        });
    }

    private String[] recuperateLanguage() {

        String spinnerText = String.valueOf(spinner.getSelectedItem());
        String url;
        String contentType;

        switch (spinnerText) {
            case "JSON":
                url = "http://sym.iict.ch/rest/json";
                contentType = "application/json";
                break;
            case "XML":
                url = "http://sym.iict.ch/rest/xml";
                contentType = "application/xml";
                break;
            case "Plain text":
                url = "http://sym.iict.ch/rest/txt";
                contentType = "text/plain";
                break;
            default:
                url = "http://sym.iict.ch/rest/json";
                contentType = "application/json";
                break;
        }

        String[] languageParameters = new String[2];
        languageParameters[0] = url;
        languageParameters[1] = contentType;

        return languageParameters;
    }
}
