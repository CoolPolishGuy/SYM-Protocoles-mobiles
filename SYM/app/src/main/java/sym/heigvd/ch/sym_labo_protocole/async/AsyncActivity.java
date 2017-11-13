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

/**
 * This activity allow the user to send requests asynchronously, the UI stay available during
 * the transmissions.
 * Three types of content can be send on different endpoints in the server. The content on the
 * server response is printed when received.
 *
 * @author Tano Iannetta, Lara Chauffoureaux, Wojciech Myszkorowki
 */
public class AsyncActivity extends AppCompatActivity {

    private EditText receivedData;  // Edit text with received data when available
    private EditText toSendData;    // Edit text containing the data of the user
    private Spinner spinner;        // Spinner for content type choice
    private Button send;            // Button send

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

                        // Code of response treatment, as to be in the UI thread
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

                // Sending of the request
                sender.execute(toSendData.getText().toString(), languageParameters[0], languageParameters[1]);
            }
        });
    }

    /**
     * Private function to recuperate the selected content when the button is clicked.
     *
     * @return An array of strings containing url in [0] and content-type in [1]
     */
    private String[] recuperateLanguage() {

        String url;
        String contentType;

        // Recuperation of the spinner value
        String spinnerText = String.valueOf(spinner.getSelectedItem());

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
