package com.aaron.me.aidlclient;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aaron.me.aidlserver.IBookManager;
import com.aaron.me.aidlserver.IOnNewBookArrivedListener;
import com.aaron.me.aidlserver.bean.Book;

/**
 * 观察者模式demo,客户端订阅服务端"新增书本"事件
 */
public class AIDLSample_2_Activity extends AppCompatActivity {
    private static final int MSG_NEW_BOOK = 0;

    private TextView mTextViewLog;
    private IBookManager mIBookManager;
    private MyHandler mMyHandler;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIBookManager = IBookManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {}
    };

    private IOnNewBookArrivedListener mOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            //该回调会在客户端的Binder线程池执行, 需要handler
            mMyHandler.obtainMessage(MSG_NEW_BOOK, book).sendToTarget();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidlsample_2_);
        mTextViewLog = (TextView) findViewById(R.id.tv_log);
        mMyHandler = new MyHandler();

        bindService();
    }

    private void bindService() {
        Intent service = new Intent();
        service.setClassName("com.aaron.me.aidlserver", "com.aaron.me.aidlserver.service.BookManagerService");
        bindService(service, conn, Service.BIND_AUTO_CREATE);
    }

    public void registerListener(View view) {
        if (mIBookManager != null) {
            try {
                mIBookManager.registerBookManager(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void unRegisterListener(View view) {
        if (mIBookManager != null) {
            try {
                mIBookManager.unRegisterBookManager(mOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_NEW_BOOK:
                    newBookArrived((Book) msg.obj);
                    break;
            }
        }
    }

    private void newBookArrived(Book book) {
        mTextViewLog.append("\nnew book arrived : " + book.toString());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null) {
            unbindService(conn);
        }
    }
}
