package com.f10r3nz4.bookish.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;

import com.f10r3nz4.bookish.R;
import com.f10r3nz4.bookish.data.model.Book;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.*;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class NuovoLibroActivity extends AppCompatActivity {

    /**
     * Nella dichiarazione di variabili o componenti,
     * inserire sempre i modificatori di accesso in modo tale
     * da utilizzare al meglio la tecnica dell'incapsulamento
     */

    //Activity Components
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private EditText eAutore, eTitolo;
    private TextView tGenere, tCopertina;
    private ImageView icon_carica;
    private StorageReference mStorageRef;
    private Button btnAggiungi, btnCarica;
    private RadioGroup rbGenere;
    private RadioButton rbGiallo, rbClassici, rbHorror, rbPerRagazzi, rbNarrativa, rbFantasy;
    /**
     * Aggiungo un progressBar e utilizzo il ConstraintLayout
     * rinominandolo new_book_view
     */
    private ProgressBar progressBar;
    private ConstraintLayout newBookView;

    //Datas datas;
    /**
     * Ho creato un solo model dell'entità libro chiamato "Book"
     * e ho eliminato la classe Datas
     */
    private Book myBook;
    private DatabaseReference dbreff;
    public Uri copUri;
    private StorageTask uploadTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuovo_libro);

        //Firebase
        mStorageRef=FirebaseStorage.getInstance().getReference("Copertina");
        dbreff=FirebaseDatabase.getInstance().getReference().child("Libri");

        //Activity Components
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        eAutore = findViewById(R.id.editAutore);
        eTitolo = findViewById(R.id.editTitolo);
        tGenere = findViewById(R.id.textGenere);
        tCopertina = findViewById(R.id.textCopertina);
        icon_carica = findViewById(R.id.imageIconUp);
        btnCarica = findViewById(R.id.buttonCopertina);
        btnAggiungi = findViewById(R.id.buttonAggiungi);
        rbGenere = findViewById(R.id.groupGenere);
        rbGiallo = findViewById(R.id.radioGiallo);
        rbClassici = findViewById(R.id.radioClassici);
        rbHorror = findViewById(R.id.radioHorror);
        rbPerRagazzi = findViewById(R.id.radioPerRagazzi);
        rbNarrativa = findViewById(R.id.radioNarrativa);
        rbFantasy = findViewById(R.id.radioFantasy);

        progressBar = findViewById(R.id.progressBar);
        newBookView = findViewById(R.id.new_book_view);

        myBook = new Book();

        btnCarica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Filechooser();
            }
        });

        btnAggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fileuploader();
               /* if (uploadTask.isSuccessful()) {
                    startActivity(new Intent(getApplicationContext(), LettureActivity.class));
                }*/
            }
        });


        //menu
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_sfoglia:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;
                    case R.id.nav_letture:
                        startActivity(new Intent(getApplicationContext(), LettureActivity.class));
                        break;
                    case R.id.nav_libri:
                        startActivity(new Intent(getApplicationContext(), LibriActivity.class));
                        break;
                    case R.id.nav_esci:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        navigationView.setCheckedItem(R.id.nav_letture);

    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //aggiungi libro

    /*private String getExtension(Uri uri) {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((cr.getType(uri)));
    }*/

    private void Fileuploader() {

        progressBar.setVisibility(View.VISIBLE);
        newBookView.setVisibility(View.GONE);

        String g1 = rbClassici.getText().toString().trim();
        String g2 = rbFantasy.getText().toString().trim();
        String g3 = rbGiallo.getText().toString().trim();
        String g4 = rbHorror.getText().toString().trim();
        String g5 = rbNarrativa.getText().toString().trim();
        String g6 = rbPerRagazzi.getText().toString().trim();

        /*String idCopertina;
        idCopertina=System.currentTimeMillis()+"."+getExtension(copUri);*/

        myBook.setTitolo(eTitolo.getText().toString().trim());
        myBook.setAutore(eAutore.getText().toString().trim());

        if (rbClassici.isChecked()) myBook.setGenere(g1);
        else if (rbFantasy.isChecked()) myBook.setGenere(g2);
        else if (rbGiallo.isChecked()) myBook.setGenere(g3);
        else if (rbHorror.isChecked()) myBook.setGenere(g4);
        else if (rbNarrativa.isChecked()) myBook.setGenere(g5);
        else if (rbPerRagazzi.isChecked()) myBook.setGenere(g6);


        //dbreff.push().setValue(datas);
        //StorageReference Ref=mStorageRef.child(idCopertina);

        /**
         * Rinomino l'immagine con la data odierna in formato timestamp
         */

        String nameCopertina = String.valueOf(System.currentTimeMillis());
        StorageReference Ref=mStorageRef.child(nameCopertina);

        uploadTask = Ref.putFile(copUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                /**
                                 * Ricavo URL dell'immagine salvato su Storage
                                 */
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        /**
                                         * inizializzo la variabile idCopertina sulla nuova istanza di Book
                                         */
                                        myBook.setIdCopertina(imageUrl);

                                        /**
                                         * Salvo su RealTime DB
                                         */
                                        dbreff.push().setValue(myBook);
                                    }
                                });
                            }
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(NuovoLibroActivity.this, "Libro aggiunto!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressBar.setVisibility(View.GONE);
                        newBookView.setVisibility(View.VISIBLE);
                        Toast.makeText(NuovoLibroActivity.this, "Errore, riprova", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void Filechooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null) {
            copUri = data.getData();
            icon_carica.setImageURI(copUri);
        }
    }
    
}
