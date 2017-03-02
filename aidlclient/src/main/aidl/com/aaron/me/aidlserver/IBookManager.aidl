// IBookManager.aidl
package com.aaron.me.aidlserver;

// Declare any non-default types here with import statements
// 注意手动导包
import com.aaron.me.aidlserver.bean.Book;
import com.aaron.me.aidlserver.IOnNewBookArrivedListener;

interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void addBook(in Book book);

    List<Book> getBookList();

    // 订阅
    void registerBookManager(IOnNewBookArrivedListener listener);
    // 取消订阅
    void unRegisterBookManager(IOnNewBookArrivedListener listener);
}
