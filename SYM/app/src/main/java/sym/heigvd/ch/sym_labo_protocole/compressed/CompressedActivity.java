package sym.heigvd.ch.sym_labo_protocole.compressed;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import sym.heigvd.ch.sym_labo_protocole.R;
import sym.heigvd.ch.sym_labo_protocole.utils.CommunicationEventListener;

/**
 * This activity allow the user to send requests with compressed content.
 * The server's response contain also compressed content, thus we need to decompress it
 * before displaying it to the user.
 *
 * @author Tano Iannetta, Lara Chauffoureaux, Wojciech Myszkorowki
 */
public class CompressedActivity extends AppCompatActivity {

    private EditText receivedData;  // Edit text with received data when available
    private EditText toSendData;    // Edit text containing the data of the user
    private Button send;            // Button send

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compressed);

        // Recuperate UI things
        this.receivedData = (EditText) findViewById(R.id.receivedData);
        this.toSendData = (EditText) findViewById(R.id.toSendData);
        this.send = (Button) findViewById(R.id.send);

        toSendData.setText(R.string.plain_default_fill);

        // "Compressed" button listener
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Sender settings
                final CompressSendRequest sender = new CompressSendRequest();
                sender.setCommunicationEventListener(new CommunicationEventListener() {
                    public boolean handleServerResponse(final String response) {

                        // Code of response treatment, as to be in the UI thread
                        CompressedActivity.this.runOnUiThread(new Runnable() {
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
                sender.execute(toSendData.getText().toString(), "http://sym.iict.ch/rest/txt");
            }
        });
    }
}
