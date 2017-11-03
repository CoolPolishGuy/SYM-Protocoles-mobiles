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
import sym.heigvd.ch.sym_labo_protocole.async.AsyncActivity;
import sym.heigvd.ch.sym_labo_protocole.utils.CommunicationEventListener;


/**
 * This activity send requests stored in a list every x seconds
 * This is used to simulate a differate send when the device isn't connected to internet
 * and therefore can't send http requests to the server.
 * @author Tano Iannetta
 * //todo ajout fonctionnalité lara pour les différents types
 */
public class DifferateActivity extends AppCompatActivity {

    // interface
    private EditText dataToSend;
    private Button send;
    private Button stopSend;

    private Timer timer;
    private List<String> differateRequests = new ArrayList<>();

    @Override
    protected void onPause() {
        super.onPause();
        if(timer != null)
        {
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

        // action associated to "send" button
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String req = String.valueOf(dataToSend.getText());
                if(req.length() != 0)
                {
                    // add to the list
                    differateRequests.add(req);
                }
            }
        });
        // action associated to "stop send" button
        stopSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timer != null)
                {
                    Log.e("Timer", "timer canceled");
                    timer.cancel();
                }
            }
        });
    }

    /**
     * Start a timer that send the requests in the list
     * every 10 seconds
     */
    private void launchTimer()
    {
        if(timer == null) // no timer already started
        {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {

                    if(!differateRequests.isEmpty())
                    {
                        // send requests
                        for(String request : differateRequests)
                        {
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
                            // send request
                            sender.execute(request, "http://sym.iict.ch/rest/txt");
                        }
                        differateRequests.clear();
                    }
                }
            },10000,10000); // after 10sec every 10sec
        }
    }
}
