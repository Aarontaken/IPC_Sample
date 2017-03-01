package com.aaron.me.aidlserver.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.aaron.me.aidlserver.IBookManager;
import com.aaron.me.aidlserver.bean.Book;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class BookManagerService extends Service {
    private CopyOnWriteArrayList<Book> books;

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * CopyOnWriteArrayList: {http://blog.csdn.net/wjwj1203/article/details/8109000}
         */
        books = new CopyOnWriteArrayList<>();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBookManager;
    }

    IBookManager.Stub iBookManager = new IBookManager.Stub() {
        @Override
        public void addBook(com.aaron.me.aidlserver.bean.Book book) throws RemoteException {
            books.add(book);
        }

        @Override
        public List<com.aaron.me.aidlserver.bean.Book> getBookList() throws RemoteException {
            return books;
        }
    };
}
