// IOnNewBookArrivedListener.aidl
package com.aaron.me.aidlserver;

// Declare any non-default types here with import statements
import com.aaron.me.aidlserver.bean.Book;

interface IOnNewBookArrivedListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onNewBookArrived(in Book book);
}
