package com.f10r3nz4.bookish.data.model;

public class Book {

    /**
     * Aggiungo bookId per determinare l ID assegnato al libro da Firebase
     */

    String bookId, autore, genere, idCopertina, titolo;

    public Book() { }

    public Book(String bookId, String autore, String genere, String idCopertina, String titolo) {
        this.bookId = bookId;
        this.autore = autore;
        this.genere = genere;
        this.idCopertina = idCopertina;
        this.titolo = titolo;
    }


    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public  String getIdCopertina() {
        return idCopertina;
    }

    public void setIdCopertina(String idCopertina) {
        this.idCopertina = idCopertina;
    }


    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
}


