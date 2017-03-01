package com.aaron.me.aidlclient;

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

import com.aaron.me.aidlserver.IBookManager;
import com.aaron.me.aidlserver.bean.Book;

import java.util.List;

public class AIDLSample_1_Activity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private IBookManager iBookManagerService;
    private TextView mBookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidlsample_1_);
        mBookList = (TextView) findViewById(R.id.book_list);

        bindService();
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBookManagerService = IBookManager.Stub.asInterface(service);

            // 设置服务端binder断开连接的回调
            try {
                service.linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iBookManagerService = null;
        }
    };

    public void onAddBook(View view) {
        if (iBookManagerService != null) {
            try {
                iBookManagerService.addBook(new Book(1, "aaron的书"));
                List<Book> bookList = iBookManagerService.getBookList();
                mBookList.setText(bookList.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (iBookManagerService == null) {
                return;
            }
            iBookManagerService.asBinder().unlinkToDeath(mDeathRecipient, 0);
            iBookManagerService = null;

            //重新绑定
            bindService();
        }
    };

    private void bindService() {
        Intent service = new Intent();
        service.setComponent(new ComponentName("com.aaron.me.aidlserver", "com.aaron.me.aidlserver.service.BookManagerService"));
        bindService(service, conn, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null) {
            unbindService(conn);
        }
    }
}
