package com.aaron.me.aidlserver;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.aaron.me.aidlserver.bean.Book;
import com.aaron.me.aidlserver.service.BookManagerService;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private IBookManager iBookManager;
    private TextView mBooksTextView;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBookManager =  IBookManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBooksTextView = (TextView) findViewById(R.id.book_list);

        bindService();
    }

    private void bindService() {
        Intent service = new Intent(this, BookManagerService.class);
        bindService(service, conn, Service.BIND_AUTO_CREATE);
    }

    public void addBook(View view) {
        if (iBookManager == null) {
            return;
        }
        try {
            iBookManager.addBook(new Book(0, "Bilibili"));
            List<Book> bookList = iBookManager.getBookList();
            mBooksTextView.setText(bookList.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null) {
            unbindService(conn);
        }
    }
}
