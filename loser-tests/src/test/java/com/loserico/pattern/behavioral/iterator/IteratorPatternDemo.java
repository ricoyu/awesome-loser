package com.loserico.pattern.behavioral.iterator;

public class IteratorPatternDemo {

    public static void main(String[] args) {
        BookShelf bookShelf = new BookShelf();
        bookShelf.addBook(new Book("Design Patterns"));
        bookShelf.addBook(new Book("Effective Java"));
        bookShelf.addBook(new Book("Clean Code"));

        Iterator iterator = bookShelf.iterator();
        while (iterator.hasNext()) {
            Book book = (Book)iterator.next();
            System.out.println(book.getName());
        }
    }
}