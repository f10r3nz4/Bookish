package com.f10r3nz4.bookish.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.f10r3nz4.bookish.R;
import com.f10r3nz4.bookish.data.adapter.BookAdapterList;
import com.f10r3nz4.bookish.data.model.Book;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LibriActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ImageButton updateListBtn;

    /**
     * Aggiungio il recyclerView
     */
    private RecyclerView recyclerView;

    //Firebase
    private FirebaseDatabase database;
    private DatabaseReference userBookDatabase;
    private DatabaseReference bookDatabase;

    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

    //Data
    ArrayList<Book> booksList = new ArrayList<Book>();

    private BookAdapterList bookAdapterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libri);


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        updateListBtn = findViewById(R.id.update_list_btn);

        updateListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (booksList.size() != 1) {
                    booksList = new ArrayList<Book>();
                    getBooksId();
                } else if(booksList.size() == 1) {
                    booksList = new ArrayList<Book>();
                    updateRecyclerView();
                }
            }
        });


        /**
         * Firebase
         */
        database = FirebaseDatabase.getInstance();
        userBookDatabase =
                database.getReference("Utenti")
                        .child(currentFirebaseUser.getUid())
                        .child("Books");
        bookDatabase =
                database.getReference("Libri");

        /**
         * Aggiungio il recyclerView
         */
        recyclerView = findViewById(R.id.rv_books);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /**
         * Chiamo il metodo de lettura id dei libri dell'utente in DB
         */
        getBooksId();

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

        navigationView.setCheckedItem(R.id.nav_libri);
    }

    private void getBooksId(){

        /**
         * Ottengo la lista dei libri letti o in lettura dell'utente
         */
        userBookDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                /**
                 * Iterazione della lista dei libri ottenuti
                 */
                for (DataSnapshot userBook: snapshot.getChildren()) {

                    /**
                     * Leggo lo stato del libro dell'utente.
                     * Se il libro è in lettura, popolo la lista
                     */
                    String bookStatus = String.valueOf(userBook.child("status").getValue());
                    if (bookStatus.equals("Read"))
                        getBook(userBook.getKey());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    private void getBook(String bookId){

        bookDatabase.child(bookId).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot book) {
                Book myBook = book.getValue(Book.class);
                myBook.setBookId(book.getKey());
                booksList.add(myBook);
                updateRecyclerView();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


    }

    private void updateRecyclerView(){
        bookAdapterList = new BookAdapterList(this, booksList);
        recyclerView.setAdapter(bookAdapterList);
    }

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

}
