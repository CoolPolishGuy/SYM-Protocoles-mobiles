package sym.heigvd.ch.sym_labo_protocole;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import sym.heigvd.ch.sym_labo_protocole.async.AsyncActivity;
import sym.heigvd.ch.sym_labo_protocole.compressed.CompressedActivity;
import sym.heigvd.ch.sym_labo_protocole.differate.DifferateActivity;
import sym.heigvd.ch.sym_labo_protocole.object.ObjectActivity;

/**
 * Main activity of the application, offer 4 buttons to the user to allow him to choose the
 * activity to open.
 *
 * @author Tano Iannetta, Lara Chauffoureaux, Wojciech Myszkorowski
 */
public class MainActivity extends AppCompatActivity {

    // For logging purposes
    private static final String TAG = MainActivity.class.getSimpleName();

    private Button buttonAsync;         // Async activity button
    private Button buttonDifferate;     // Differate activity button
    private Button buttonObject;        // Object activity button
    private Button buttonCompressed;    // Compressed activity button

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recuperate UI things
        this.buttonAsync = (Button) findViewById(R.id.buttonAsync);
        this.buttonDifferate = (Button) findViewById(R.id.buttonDifferate);
        this.buttonObject = (Button) findViewById(R.id.buttonObject);
        this.buttonCompressed = (Button) findViewById(R.id.buttonCompressed);

        // Listener associated to "Async" button
        buttonAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AsyncActivity.class);
                startActivity(intent);
            }
        });

        // Listener associated to "Differate" button
        buttonDifferate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DifferateActivity.class);
                startActivity(intent);
            }
        });

        // Listener associated to "Object" button
        buttonObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ObjectActivity.class);
                startActivity(intent);
            }
        });

        // Listener associated to "Compressed" button
        buttonCompressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CompressedActivity.class);
                startActivity(intent);
            }
        });
    }
}
