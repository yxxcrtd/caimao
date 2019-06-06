package com.caimao.bana.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by WangXu on 2015/6/1.
 */
public class ByteArrayInputStream extends InputStream {
    private byte[] buffer;
    private int index;
    private int limit;
    private int mark;
    private boolean closed;

    public ByteArrayInputStream(byte[] data) {
        this(data, 0, data.length);
    }

    public ByteArrayInputStream(byte[] data, int offset, int length) {
        if(data == null) {
            throw new NullPointerException();
        } else if(offset >= 0 && offset + length <= data.length && length >= 0) {
            this.buffer = data;
            this.index = offset;
            this.limit = offset + length;
            this.mark = offset;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public int read() throws IOException {
        if(this.closed) {
            throw new IOException("Stream closed");
        } else {
            return this.index >= this.limit?-1:this.buffer[this.index++] & 255;
        }
    }

    public int read(byte[] data, int offset, int length) throws IOException {
        if(data == null) {
            throw new NullPointerException();
        } else if(offset >= 0 && offset + length <= data.length && length >= 0) {
            if(this.closed) {
                throw new IOException("Stream closed");
            } else if(this.index >= this.limit) {
                return -1;
            } else {
                if(length > this.limit - this.index) {
                    length = this.limit - this.index;
                }

                System.arraycopy(this.buffer, this.index, data, offset, length);
                this.index += length;
                return length;
            }
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public long skip(long amount) throws IOException {
        if(this.closed) {
            throw new IOException("Stream closed");
        } else if(amount <= 0L) {
            return 0L;
        } else {
            if(amount > (long)(this.limit - this.index)) {
                amount = (long)(this.limit - this.index);
            }

            this.index += (int)amount;
            return amount;
        }
    }

    public int available() throws IOException {
        if(this.closed) {
            throw new IOException("Stream closed");
        } else {
            return this.limit - this.index;
        }
    }

    public void close() {
        this.closed = true;
    }

    public void mark(int readLimit) {
        this.mark = this.index;
    }

    public void reset() throws IOException {
        if(this.closed) {
            throw new IOException("Stream closed");
        } else {
            this.index = this.mark;
        }
    }

    public boolean markSupported() {
        return true;
    }
}
