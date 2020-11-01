package com.f10r3nz4.bookish.Ricerca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.f10r3nz4.bookish.R;
import com.f10r3nz4.bookish.ui.MainActivity;
import com.f10r3nz4.bookish.data.adapter.BookAdapter;
import com.f10r3nz4.bookish.data.model.Book;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class RicercaAutoreActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    BookAdapter adapter;
    ImageButton indietro;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricerca_autore);

        indietro = findViewById(R.id.btnIndietro);
        toolbar = findViewById(R.id.toolbar2);
        recyclerView = findViewById(R.id.rcLista);

        setSupportActionBar(toolbar);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*FirebaseRecyclerOptions<Book> options =
                new FirebaseRecyclerOptions.Builder<Book>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Libri"), Book.class)
                        .build();*/

        /**
         * Aggiungo alla chiamata di FirebaseRecyclerOptions
         * la funzione new SnapshotParser in modo tale da poter istanziare
         * e modificare l'oggetto "Book" per ottenere anche
         * l ID del child su Firebase
         */

        Query query = FirebaseDatabase.getInstance().getReference().child("Libri");
        FirebaseRecyclerOptions<Book> options =
                new FirebaseRecyclerOptions.Builder<Book>()
                        .setQuery(query, new SnapshotParser<Book>(){
                            @NonNull
                            @Override
                            public Book parseSnapshot(@NonNull DataSnapshot snapshot) {
                                Book myBook = snapshot.getValue(Book.class);
                                myBook.setBookId(snapshot.getKey());
                                return myBook;
                            }
                        }).build();

        adapter = new BookAdapter(options, this);

        recyclerView.setAdapter(adapter);

        indietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem item = menu.findItem(R.id.app_bar_search);

        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void processsearch(String s) {
//CASE SENSITIVE, non posso usare toUppercase perchè i caratteri maiuscoli non sono consecutivi

        /*FirebaseRecyclerOptions<Book> options =
                new FirebaseRecyclerOptions.Builder<Book>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Libri").orderByChild("autore").startAt(s).endAt(s+"\uf8ff"), Book.class)
                        .build();*/
        /**
         * Aggiungo alla chiamata di FirebaseRecyclerOptions
         * la funzione new SnapshotParser in modo tale da poter istanziare
         * e modificare l'oggetto "Book" per ottenere anche
         * l ID del child su Firebase
         */

        Query query = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Libri")
                .orderByChild("autore")
                .startAt(s).endAt(s+"\uf8ff");

        FirebaseRecyclerOptions<Book> options =
                new FirebaseRecyclerOptions.Builder<Book>()
                        .setQuery(query, new SnapshotParser<Book>(){
                            @NonNull
                            @Override
                            public Book parseSnapshot(@NonNull DataSnapshot snapshot) {
                                Book myBook = snapshot.getValue(Book.class);
                                myBook.setBookId(snapshot.getKey());
                                return myBook;
                            }
                        }).build();

        adapter = new BookAdapter(options, this);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
}