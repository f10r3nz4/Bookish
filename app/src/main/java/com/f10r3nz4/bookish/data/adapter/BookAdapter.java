package com.f10r3nz4.bookish.data.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.f10r3nz4.bookish.R;
import com.f10r3nz4.bookish.Ricerca.GialliActivity;
import com.f10r3nz4.bookish.data.common.DialogApp;
import com.f10r3nz4.bookish.data.model.Book;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookAdapter extends FirebaseRecyclerAdapter<Book, BookAdapter.myviewholder>  {

    public Context myContext;



    public BookAdapter(@NonNull FirebaseRecyclerOptions<Book> options, Context context) {
        super(options);
        myContext = context;

    }


    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull final Book book) {
        holder.titolo.setText(book.getTitolo());
        holder.autore.setText(book.getAutore());
        holder.genere.setText(book.getGenere());
        Picasso.get().load(book.getIdCopertina()).into(holder.copertina);
        //Glide.with(holder.copertina.getContext()).load(model.getCopertina()).into(holder.copertina);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               DialogApp dialogApp = new DialogApp();
               dialogApp.infoBook(myContext,book.getBookId(), book.getTitolo(),book.getAutore(),book.getGenere());
           }
       });

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        return new myviewholder(view);
    }



    class myviewholder extends RecyclerView.ViewHolder  {

        ImageView copertina;
        TextView titolo, autore, genere;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            copertina=itemView.findViewById(R.id.imgCopertina);
            titolo=itemView.findViewById(R.id.textTitolo);
            autore=itemView.findViewById(R.id.textAutore);
            genere=itemView.findViewById(R.id.textGenere);



        }


    }



}
