package memory.test.test.visitor;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class SlowVisitor {

    private File file;

    private RandomAccessFile randomAccessFile;

    public SlowVisitor() throws IOException {
        setFile(null);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) throws IOException {
        if (this.randomAccessFile != null) {
            close();
        }
        this.file = file;
        if (file == null) {
            this.randomAccessFile = null;
        } else {
            if (!file.exists() || file.canWrite()) {
                this.randomAccessFile = new RandomAccessFile(file, "rw");
            } else {
                this.randomAccessFile = new RandomAccessFile(file, "r");
            }
        }
    }

    public long getLength() throws IOException {
        return randomAccessFile.length();
    }

    public long getPosition() throws IOException {
        return randomAccessFile.getFilePointer();
    }

    public boolean isEofChar() throws IOException {
        return randomAccessFile.getFilePointer() + 1 >= randomAccessFile.length();
    }

    public boolean isEof() throws IOException {
        return randomAccessFile.getFilePointer() >= randomAccessFile.length();
    }

    public boolean isEofChar(long distance) throws IOException {
        long position = randomAccessFile.getFilePointer();
        return position + 2 * distance + 1 >= randomAccessFile.length() || position + 2 * distance < 0;
    }

    public boolean isEof(long distance) throws IOException {
        long position = randomAccessFile.getFilePointer();
        return position + distance >= randomAccessFile.length() || position + distance < 0;
    }

    public void forwardChar() throws IOException {
        long position = randomAccessFile.getFilePointer();
        if (position + 2 > randomAccessFile.length()) {
            throw new EOFException();
        }
        randomAccessFile.seek(position + 2);
    }

    public void forwardChar(long distance) throws IOException {
        long position = randomAccessFile.getFilePointer();
        if (position + 2 * distance > randomAccessFile.length()) {
            throw new EOFException();
        }
        randomAccessFile.seek(position + 2 * distance);
    }

    public void forward() throws IOException {
        long position = randomAccessFile.getFilePointer();
        if (position + 1 > randomAccessFile.length()) {
            throw new EOFException();
        }
        randomAccessFile.seek(position + 1);
    }

    public void forward(long distance) throws IOException {
        long position = randomAccessFile.getFilePointer();
        if (position + distance > randomAccessFile.length()) {
            throw new EOFException();
        }
        randomAccessFile.seek(position + distance);
    }

    public void backChar() throws IOException {
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 2);
    }

    public void backChar(long distance) throws IOException {
        long position = randomAccessFile.getFilePointer();
        if (position - 2 * distance > randomAccessFile.length()) {
            throw new EOFException();
        }
        randomAccessFile.seek(position - 2 * distance);
    }

    public void back() throws IOException {
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 1);
    }

    public void back(long distance) throws IOException {
        long position = randomAccessFile.getFilePointer();
        if (position - distance > randomAccessFile.length()) {
            throw new EOFException();
        }
        randomAccessFile.seek(position - distance);
    }

    public char peekChar() throws IOException {
        long position = randomAccessFile.getFilePointer();
        if (position + 1 >= randomAccessFile.length()) {
            throw new EOFException();
        }
        char c = randomAccessFile.readChar();
        randomAccessFile.seek(position);
        return c;
    }

    public byte peek() throws IOException {
        long position = randomAccessFile.getFilePointer();
        if (position >= randomAccessFile.length()) {
            throw new EOFException();
        }
        byte b = randomAccessFile.readByte();
        randomAccessFile.seek(position);
        return b;
    }

    public char nextChar() throws IOException {
        long position = randomAccessFile.getFilePointer();
        if (position + 3 >= randomAccessFile.length()) {
            throw new EOFException();
        }
        randomAccessFile.seek(position + 2);
        char c = randomAccessFile.readChar();
        randomAccessFile.seek(position);
        return c;
    }

    public char nextChar(long distance) throws IOException {
        long position = randomAccessFile.getFilePointer();
        if (position + 2 * distance + 1 >= randomAccessFile.length()) {
            throw new EOFException();
        }
        randomAccessFile.seek(position + 2 * distance);
        char c = randomAccessFile.readChar();
        randomAccessFile.seek(position);
        return c;
    }

    public byte next() throws IOException {
        long position = randomAccessFile.getFilePointer();
        if (position + 1 >= randomAccessFile.length()) {
            throw new EOFException();
        }
        randomAccessFile.seek(position + 1);
        byte b = randomAccessFile.readByte();
        randomAccessFile.seek(position);
        return b;
    }

    public byte next(long distance) throws IOException {
        long position = randomAccessFile.getFilePointer();
        if (position + distance >= randomAccessFile.length()) {
            throw new EOFException();
        }
        randomAccessFile.seek(position + distance);
        byte b = randomAccessFile.readByte();
        randomAccessFile.seek(position);
        return b;
    }

    public char previousChar() throws IOException {
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 2);
        return randomAccessFile.readChar();
    }

    public char previousChar(long distance) throws IOException {
        long position = randomAccessFile.getFilePointer();
        if (position - 2 * distance + 1 >= randomAccessFile.length()) {
            throw new EOFException();
        }
        randomAccessFile.seek(position - 2 * distance);
        char c = randomAccessFile.readChar();
        randomAccessFile.seek(position);
        return c;
    }

    public byte previous() throws IOException {
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 1);
        return randomAccessFile.readByte();
    }

    public byte previous(long distance) throws IOException {
        long position = randomAccessFile.getFilePointer();
        if (position - distance >= randomAccessFile.length()) {
            throw new EOFException();
        }
        randomAccessFile.seek(position - distance);
        byte b = randomAccessFile.readByte();
        randomAccessFile.seek(position);
        return b;
    }

    public void write(char c) throws IOException {
        randomAccessFile.writeChar(c);
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 2);
    }

    public void write(byte b) throws IOException {
        randomAccessFile.write(b);
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 1);
    }

    public void close() throws IOException {
        randomAccessFile.close();
    }

}
