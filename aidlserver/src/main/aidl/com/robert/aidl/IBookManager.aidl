// IBookManager.aidl
package com.robert.aidl;

// Declare any non-default types here with import statements
import com.robert.aidl.Book;

interface IBookManager {
    List<Book> getBooks();
    void addBook(in Book book);
}
