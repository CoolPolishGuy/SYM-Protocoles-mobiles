package sym.heigvd.ch.sym_labo_protocole;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import sym.heigvd.ch.sym_labo_protocole.async.AsyncActivity;
import sym.heigvd.ch.sym_labo_protocole.differate.DifferateActivity;
import sym.heigvd.ch.sym_labo_protocole.object.ObjectActivity;

public class MainActivity extends AppCompatActivity {

    // For logging purposes
    private static final String TAG = MainActivity.class.getSimpleName();

    // Button of the main page
    private Button buttonAsync;
    private Button buttonDifferate;
    private Button buttonObject;
    private Button buttonCompressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recuperation of button reference in the view
        this.buttonAsync = (Button) findViewById(R.id.buttonAsync);
        this.buttonDifferate = (Button) findViewById(R.id.buttonDifferate);
        this.buttonObject = (Button) findViewById(R.id.buttonObject);
        this.buttonCompressed = (Button) findViewById(R.id.buttonCompressed);

        // Action associated to "Async" button
        buttonAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AsyncActivity.class);
                startActivity(intent);
            }
        });

        // Action associated to "Differate" button
        buttonDifferate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DifferateActivity.class);
                startActivity(intent);
            }
        });

        // Action associated to "Object" button
        buttonObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ObjectActivity.class);
                startActivity(intent);
            }
        });

        // Action associated to "Compressed" button
        buttonCompressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
