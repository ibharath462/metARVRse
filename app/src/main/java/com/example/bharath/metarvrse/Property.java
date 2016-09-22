package com.example.bharath.metarvrse;

/**
 * Created by Bharath on 9/22/2016.
 */
public class Property {

    private String name,author,image;
    private String isbn;
    public Property(String name,String author,String isbn,String image){

        this.name=name;
        this.author=author;
        this.image=image;
        this.isbn=isbn;
    }
    public String getName(){
        return name;
    }
    public  String getAuthor(){
        return author;
    }
    public String getImage(){
        return image;
    }
    public String getIsbn(){
        return isbn;
    }
}
