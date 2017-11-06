package sym.heigvd.ch.sym_labo_protocole.object;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import com.google.gson.Gson;

import sym.heigvd.ch.sym_labo_protocole.R;
import sym.heigvd.ch.sym_labo_protocole.utils.CommunicationEventListener;

public class ObjectActivity extends AppCompatActivity {

    private EditText firstname;
    private EditText name;
    private EditText age;
    private RadioButton male;
    private RadioButton female;
    private Spinner serializazion;
    private Button send;
    private EditText receivedData;

    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object);

        // Recuperate UI things
        this.firstname = (EditText) findViewById(R.id.firstName);
        this.name = (EditText) findViewById(R.id.name);
        this.age = (EditText) findViewById(R.id.age);
        this.male = (RadioButton) findViewById(R.id.male);
        this.female = (RadioButton) findViewById(R.id.female);
        this.serializazion = (Spinner) findViewById(R.id.spinner);
        this.send = (Button) findViewById(R.id.send);
        this.receivedData = (EditText) findViewById(R.id.receivedData);

        // "Object" button listener
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Sender settings
                final ObjectSendRequest sender = new ObjectSendRequest();
                sender.setCommunicationEventListener(new CommunicationEventListener() {
                    public boolean handleServerResponse(final String response) {

                        // Code de traitement de la réponse – dans le UI-Thread
                        ObjectActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                // Deserialization dont work...
                                // Student recievedtudent = gson.fromJson(response, Student.class);
                                // receivedData.setText(recievedtudent.toString());

                                // Update view
                                receivedData.setText(response);
                            }
                        });

                        return true;
                    }
                });

                // Recuperation of the fields of the new object
                String recuperatedFirstname = firstname.getText().toString();
                String recuperatedName = name.getText().toString();
                int recuperatedAge = Integer.parseInt(age.getText().toString());
                int recuperatedGender = -1;

                if(male.isChecked()) {
                    recuperatedGender = 0;
                }

                if(female.isChecked()) {
                    recuperatedGender = 1;
                }

                // Check if fields are correct
                Student student = Student.verifyFields(recuperatedFirstname, recuperatedName,
                    recuperatedAge, recuperatedGender);

                if(student == null) {

                    receivedData.setText(R.string.incorrectValue);
                }
                else {

                    // GSONification + sending to the server
                    String url = "http://sym.iict.ch/rest/json";
                    String contentType = "application/json";

                    sender.execute(gson.toJson(student), url, contentType);
                }
            }
        });
    }
}
