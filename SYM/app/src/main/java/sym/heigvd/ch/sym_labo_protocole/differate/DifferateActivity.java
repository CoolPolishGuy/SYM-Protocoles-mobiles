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
 * TODO demander comment corriger : lancer timer -> revenir page principale -> relancer activite -> stopper timer
 */
public class DifferateActivity extends AppCompatActivity {

    private EditText dataToSend;
    private EditText receivedData;
    private Button send;
    private Button stopSend;

    private Timer timer;


    private List<String> differateRequests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_differate);

        // Recuperate Ui things
        this.receivedData = (EditText) findViewById(R.id.receivedData);
        this.dataToSend = (EditText) findViewById(R.id.toSendData);
        this.send = (Button) findViewById(R.id.send);
        this.stopSend = (Button) findViewById(R.id.stopSent);

        // prepare some requests to send
        differateRequests.add("differante send 1");
        differateRequests.add("differante send 2");
        differateRequests.add("differante send 3");
        differateRequests.add("differante send 4");

        // pre fill data to send
        dataToSend.setText(differateRequests.get(0) + " " +
                differateRequests.get(1) + " " +
                differateRequests.get(2) + " " +
                differateRequests.get(3));

        // action associated to "send" button
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo empecher plusieurs timers
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {

                        // Sender settings
                        final DifferateSendRequest sender = new DifferateSendRequest();
                        sender.setCommunicationEventListener(new CommunicationEventListener() {
                            @Override
                            public boolean handleServerResponse(final String response) {

                                // Code de traitement de la réponse – dans le UI-Thread
                                DifferateActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Update view
                                        receivedData.setText(response);
                                    }
                                });
                                return true;
                            }
                        });
                        sender.execute(String.valueOf(differateRequests), "http://sym.iict.ch/rest/txt");
                    }
                },2000,10000);
            }
        });
        // action associated to "stop send" button
        stopSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timer != null)
                {
                    Log.e("Timer", "timer stopped");
                    timer.cancel();
                }
            }
        });
    }
}
