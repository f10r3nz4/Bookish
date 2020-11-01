package com.f10r3nz4.bookish.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class RegistrazioneActivity extends AppCompatActivity {

    private EditText nomeC, eMail, psswrd;
    private Button registrati;
    private TextView entra;
    private FirebaseAuth fAuth;
    private ProgressBar progressBar;
    private LinearLayout registrationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);


        nomeC = findViewById(R.id.editNome);
        eMail = findViewById(R.id.editEMail);
        psswrd = findViewById(R.id.editPassword);
        registrati = findViewById(R.id.btnRegistrati);
        entra = findViewById(R.id.textLogIn);

        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        registrationView = findViewById(R.id.registration_view);

        registrati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String myName = nomeC.getText().toString();
                String myEmail = eMail .getText().toString().trim();
                String myPassword = psswrd.getText().toString().trim();

                /**
                 * Creo un metodo a parte per la funzione registration e lo
                 * chiamo solo se email e password sono stati inseriti correttamente
                 *
                 * sostituisco TextUtils.isEmpty(myEmail) in myEmail.isEmpty
                 */
                if (myName.isEmpty()) nomeC.setError("Inserisci il tuo nome");
                else if(myEmail.isEmpty()) eMail.setError("Inserisci una password.");
                else if(myPassword.length() < 6) psswrd.setError("La password deve essere più lunga di 6 caratteri.");
                else registration(myEmail, myPassword, myName);

            }
        });

        entra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        }

        private void registration(final String email, String password, final String name) {

            /**
             * Visualizza pogressBar - Nasconde inserimento email e password
             */
            progressBar.setVisibility(View.VISIBLE);
            registrationView.setVisibility(View.GONE);

            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(RegistrazioneActivity.this, "Utente creato.", Toast.LENGTH_SHORT).show();
                        String idUtente = fAuth.getCurrentUser().getUid();
                        DatabaseReference utenteDB = FirebaseDatabase.getInstance().getReference().child("Utenti").child(idUtente);


                        Map post = new HashMap();
                        post.put("Nome", name);
                        post.put("e-Mail", email);

                        utenteDB.setValue(post);

                        /**
                         * Nasconde pogressBar
                         * distruggo Activity
                         */
                        progressBar.setVisibility(View.INVISIBLE);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                    else{

                        /**
                         * Nasconde pogressBar - Mostra inserimento email e password
                         */
                        progressBar.setVisibility(View.INVISIBLE);
                        registrationView.setVisibility(View.VISIBLE);

                        Toast.makeText(RegistrazioneActivity.this, "Errore " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }