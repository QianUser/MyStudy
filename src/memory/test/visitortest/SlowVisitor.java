package memory.test.visitortest;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class SlowVisitor {

    private final RandomAccessFile randomAccessFile;

    public SlowVisitor(String filename, String mode) throws IOException {
        this(new File(filename), mode);
    }

    public SlowVisitor(File file, String mode) throws IOException {
        this.randomAccessFile = new RandomAccessFile(file, mode.toLowerCase());
    }

    public long length() throws IOException {
        return randomAccessFile.length();
    }

    public long position() throws IOException {
        return randomAccessFile.getFilePointer();
    }

    public boolean eofChar() throws IOException {
        return randomAccessFile.getFilePointer() + 1 >= randomAccessFile.length();
    }

    public boolean eof() throws IOException {
        return randomAccessFile.getFilePointer() >= randomAccessFile.length();
    }

    public boolean eofChar(long distance) throws IOException {
        long position = randomAccessFile.getFilePointer();
        return position + 2 * distance + 1 >= randomAccessFile.length() || position + 2 * distance < 0;
    }

    public boolean eof(long distance) throws IOException {
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

    public void writeChar(char c) throws IOException {
        randomAccessFile.writeChar(c);
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 2);
    }

    public void write(byte b) throws IOException {
        randomAccessFile.write(b);
        randomAccessFile.seek(randomAccessFile.getFilePointer() - 1);
    }

    public void write(byte[] bytes) throws IOException {
        randomAccessFile.write(bytes);
        randomAccessFile.seek(randomAccessFile.getFilePointer() - bytes.length);
    }

    public void close() throws IOException {
        randomAccessFile.close();
    }

}
