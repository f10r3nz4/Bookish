package com.f10r3nz4.bookish.data.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.f10r3nz4.bookish.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DialogApp {


    public void infoBook(
            Context context,
            final String bookId,
            String title,
            String author,
            String gender){

        final DatabaseReference dbreff;
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        dbreff= FirebaseDatabase.getInstance().getReference()
                .child("Utenti")
                .child(currentFirebaseUser.getUid());



        final Dialog dialog = new Dialog(context);
        dialog .requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog .setCancelable(false);
        dialog .setContentView(R.layout.book_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView titleBook = dialog.findViewById(R.id.title_book);
        titleBook.setText(title);

        TextView authorBook = dialog.findViewById(R.id.author_book);
        authorBook.setText(author);

        TextView genderBook = dialog.findViewById(R.id.gender_book);
        genderBook.setText(gender);

        final RadioButton inReading = dialog.findViewById(R.id.inReading_btn);
        final RadioButton read = dialog.findViewById(R.id.read_btn);


        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String statusBook = "";

                Log.i("TEST ID", bookId);

                if(snapshot != null)
                    statusBook = snapshot.child("Books").child(bookId).child("status").getValue(String.class);

                if (statusBook != null) {
                    if (statusBook.equals("Read")) read.setChecked(true);
                    else if (statusBook.equals("inReading")) inReading.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        };
        dbreff.addListenerForSingleValueEvent(eventListener);


        Button actionBtn = dialog .findViewById(R.id.action_btn);
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(read.isChecked()) dbreff.child("Books").child(bookId).child("status").setValue("Read");
                else if (inReading.isChecked()) dbreff.child("Books").child(bookId).child("status").setValue("inReading");
                dialog.dismiss();
            }
        });
        dialog.show();

    }

}
