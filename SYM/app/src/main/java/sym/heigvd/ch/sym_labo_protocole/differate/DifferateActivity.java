package sym.heigvd.ch.sym_labo_protocole.differate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sym.heigvd.ch.sym_labo_protocole.R;

/** Mettre les requetes dans une liste, (attendre ou quitter l appli)
 * avoir un timer qui vide la liste toutes les minutes / 2 minutes
 * ecrit ensuite la réponse dans les logs
 */
public class DifferateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_differate);
    }
}
