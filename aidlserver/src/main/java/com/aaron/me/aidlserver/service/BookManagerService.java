package com.aaron.me.aidlserver.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.aaron.me.aidlserver.IBookManager;
import com.aaron.me.aidlserver.IOnNewBookArrivedListener;
import com.aaron.me.aidlserver.bean.Book;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class BookManagerService extends Service {

    private CopyOnWriteArrayList<Book> mBooks;
    private RemoteCallbackList<IOnNewBookArrivedListener> remoteCallbackList;

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * CopyOnWriteArrayList: {http://blog.csdn.net/wjwj1203/article/details/8109000}
         */
        mBooks = new CopyOnWriteArrayList<>();
        remoteCallbackList = new RemoteCallbackList<>();

        new Thread(new AddBookTask()).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBookManager;
    }

    IBookManager.Stub iBookManager = new IBookManager.Stub() {
        @Override
        public void addBook(com.aaron.me.aidlserver.bean.Book book) throws RemoteException {
            mBooks.add(book);
        }

        @Override
        public List<com.aaron.me.aidlserver.bean.Book> getBookList() throws RemoteException {
            return mBooks;
        }

        @Override
        public void registerBookManager(IOnNewBookArrivedListener listener) throws RemoteException {
            remoteCallbackList.register(listener);
        }

        @Override
        public void unRegisterBookManager(IOnNewBookArrivedListener listener) throws RemoteException {
            remoteCallbackList.unregister(listener);
        }
    };

    private class AddBookTask implements Runnable {
        private int i = 0;
        @Override
        public void run() {
            try {
                while (true) {
                    if (i < 50) {
                        Thread.sleep(2000);
                        onNewBookArrive(new Book(++i, "book_" + i));
                    } else {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void onNewBookArrive(Book book) {
        mBooks.add(book);
        int count = remoteCallbackList.beginBroadcast();
        for (int i=0; i<count; i++) {
            try {
                IOnNewBookArrivedListener bookArrivedListener = remoteCallbackList.getBroadcastItem(i);
                bookArrivedListener.onNewBookArrived(book);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        remoteCallbackList.finishBroadcast();
    }

}
