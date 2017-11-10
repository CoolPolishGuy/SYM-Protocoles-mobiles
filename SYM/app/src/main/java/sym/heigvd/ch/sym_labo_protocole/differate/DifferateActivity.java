package sym.heigvd.ch.sym_labo_protocole.differate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import sym.heigvd.ch.sym_labo_protocole.R;
import sym.heigvd.ch.sym_labo_protocole.utils.CommunicationEventListener;

/**
 * This activity allow the user to send requests stored in a list every x seconds.
 * This is used to simulate a differate request when the device isn't connected to internet
 * and therefore can't send http requests to the server immediately.
 *
 * @author Tano Iannetta, Lara Chauffoureaux, Wojciech Myszkorowki
 */
public class DifferateActivity extends AppCompatActivity {

    private EditText dataToSend;    // Edit containing the data of the user
    private Button stopSend;        // Stop button
    private Button send;            // Send button
    private Timer timer;            // Timer regulating the list send

    // List containing the request to send since the last timeout
    private List<String> differateRequests = new ArrayList<>();

    @Override
    protected void onPause() {
        super.onPause();

        // If the application is stopped data aren't send anymore
        if (timer != null) {
            Log.e("Timer", "timer canceled");
            timer.cancel();
        }

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_differate);

        // Recuperate Ui things
        this.dataToSend = (EditText) findViewById(R.id.toSendData);
        this.send = (Button) findViewById(R.id.send);
        this.stopSend = (Button) findViewById(R.id.stopSent);

        launchTimer();

        // Listener associated to "send" button
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String req = String.valueOf(dataToSend.getText());

                // If content isn't empty, we add the request to the list
                if (req.length() != 0) {
                    differateRequests.add(req);
                }
            }
        });

        // Listener associated to "stop send" button
        stopSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timer != null) {
                    Log.e("Timer", "timer canceled");
                    timer.cancel();
                }
            }
        });
    }

    /**
     * Start a timer that send the requests in the list
     * every 10 seconds after a delay of 10 seconds
     */
    private void launchTimer() {
        // If no timer already started
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {

                    // If request list isn't empty we send it
                    if (!differateRequests.isEmpty()) {
                        // Send requests
                        for (String request : differateRequests) {
                            // Sender settings
                            final DifferateSendRequest sender = new DifferateSendRequest();
                            sender.setCommunicationEventListener(new CommunicationEventListener() {
                                @Override
                                public boolean handleServerResponse(final String response) {
                                    // process response in UI-Thread
                                    DifferateActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // log response
                                            Log.e("Response", response);
                                        }
                                    });
                                    return true;
                                }
                            });

                            // Send request
                            sender.execute(request, "http://sym.iict.ch/rest/txt", "text/plain");
                        }

                        // List emptied for the next batch
                        differateRequests.clear();
                    }
                }
            }, 10000, 10000); // After 10sec every 10sec
        }
    }
}
