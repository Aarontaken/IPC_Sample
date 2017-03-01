package com.aaron.me.aidlserver.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Aaron on 2017/2/28.
 * 实现Parcelable的数据才能在进程中传递
 */
public class Book implements Parcelable {

    public int id;
    public String name;

    /** 注意这里读的顺序和写的顺序要一致!!! */
    public Book(Parcel source) {
        id = source.readInt();
        name = source.readString();
    }

    public Book(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    /** 返回当前对象的内容描述,几乎都为0,若含有文件描述符返回1 */
    @Override
    public int describeContents() {
        return 0;
    }

    /** 将当前对象写入序列化结构中*/
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    /** 用于完成反序列化功能 */
    public static final Creator<Book> CREATOR = new Creator<Book>(){

        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
