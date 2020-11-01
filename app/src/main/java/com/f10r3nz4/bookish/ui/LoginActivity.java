package com.f10r3nz4.bookish.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.f10r3nz4.bookish.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {

    private EditText eMail, psswrd;
    private Button entra;
    private TextView registrati;
    private ProgressBar progressBar;
    private LinearLayout loginView;
    private FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eMail = findViewById(R.id.editEMail);
        psswrd = findViewById(R.id.editPassword);
        progressBar =findViewById(R.id.progressBar);
        loginView = findViewById(R.id.login_view);
        entra = findViewById(R.id.buttonEntra);
        registrati = findViewById(R.id.textRegistrati);
        fAuth = FirebaseAuth.getInstance();

        /**
         * Se l'utente è loggato, accede direttamente
         */
        if(fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        entra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String myEmail = eMail .getText().toString().trim();
                String myPassword = psswrd.getText().toString().trim();

                /**
                 * Creo un metodo a parte per la funzione login e lo
                 * chiamo solo se email e password sono stati inseriti
                 *
                 * Sostituisco TextUtils.isEmpty(myEmail) in  myEmail.isEmpty()
                 */

                if(myEmail.isEmpty()) eMail.setError("Inserisci indirizzo e-mail.");
                else if(myPassword.isEmpty()) psswrd.setError("Inserisci password.");
                else login(myEmail, myPassword);

            }
        });

        registrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegistrazioneActivity.class));
            }
        });

    }

    private void login(String email, String password){


        /**
         * Visualizza pogressBar - Nasconde inserimento email e password
         */
        loginView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Sei entrato correttamente.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    /**
                     * Nasconde pogressBar
                     * distruggo Activity
                     */
                    progressBar.setVisibility(View.GONE);
                    finish();

                }
                else{
                    /**
                     * Nasconde pogressBar - Mostra inserimento email e password
                     */
                    progressBar.setVisibility(View.GONE);
                    loginView.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginActivity.this, "Errore " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
