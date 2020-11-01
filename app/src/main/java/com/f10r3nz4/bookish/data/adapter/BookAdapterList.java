package com.f10r3nz4.bookish.data.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.f10r3nz4.bookish.R;
import com.f10r3nz4.bookish.data.common.DialogApp;
import com.f10r3nz4.bookish.data.model.Book;
import com.f10r3nz4.bookish.ui.LettureActivity;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;


public class BookAdapterList extends RecyclerView.Adapter<BookAdapterList.ResultsViewHolder> implements Filterable {

    private Context context;
    private List<Book> bookList;
    private List<Book> bookListFiltered;


    public BookAdapterList(Context context, List<Book> bookList){

        this.context = context;
        this.bookList = bookList;
        this.bookListFiltered = bookList;
    }

    public class ResultsViewHolder extends RecyclerView.ViewHolder {

        private ImageView copertina;
        private TextView titolo, autore, genere;
        private CardView bookCard;

        public ResultsViewHolder(View itemView) {
            super(itemView);

            copertina=itemView.findViewById(R.id.imgCopertina);
            titolo=itemView.findViewById(R.id.textTitolo);
            autore=itemView.findViewById(R.id.textAutore);
            genere=itemView.findViewById(R.id.textGenere);
            bookCard= itemView.findViewById(R.id.book_card);
        }
    }

    @Override
    public ResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_layout, parent, false);

        return new ResultsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ResultsViewHolder viewHolder, final int position) {

        final Book book = bookListFiltered.get(position);

        viewHolder.titolo.setText(book.getTitolo());
        viewHolder.autore.setText(book.getAutore());
        viewHolder.genere.setText(book.getGenere());
        Picasso.get().load(book.getIdCopertina()).into(viewHolder.copertina);

        viewHolder.bookCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogApp dialogApp = new DialogApp();
                dialogApp.infoBook(context,book.getBookId(), book.getTitolo(),book.getAutore(),book.getGenere());

            }
        });

    }

    @Override
    public int getItemCount() {
        return bookListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    bookListFiltered = bookList;

                } else {

                    List<Book> filteredList = new ArrayList<>();

                    for (Book book : bookList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (
                                book.getTitolo().toLowerCase().contains(charString.toLowerCase())
                        ||  book.getAutore().toLowerCase().contains(charString.toLowerCase())
                                        ||  book.getGenere().toLowerCase().contains(charString.toLowerCase())
                        ) {
                            filteredList.add(book);
                        }
                    }

                    bookListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = bookListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                bookListFiltered = (ArrayList<Book>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public interface  ResultsAdapterListener {
        void onResultSelected(Book result);
    }
}
