package sample;

import java.util.List;

public class BookStore {

    private List<Book> bookList;

    private byte[] ba;

    private byte b;

    private char c;

    private short s;

    private int i;

    private long l;

    private float f;

    private double d;

    public BookStore(byte[] ba, byte b, char c, short s, int i, long l, float f, double d) {
        this.ba = ba;
        this.b = b;
        this.c = c;
        this.s = s;
        this.i = i;
        this.l = l;
        this.f = f;
        this.d = d;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public byte[] getBa() {
        return ba;
    }

    public byte getB() {
        return b;
    }

    public char getC() {
        return c;
    }

    public int getI() {
        return i;
    }

    public short getS() {
        return s;
    }

    public long getL() {
        return l;
    }

    public float getF() {
        return f;
    }

    public double getD() {
        return d;
    }
}
