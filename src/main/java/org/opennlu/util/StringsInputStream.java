package org.opennlu.util;

import opennlp.tools.util.InputStreamFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by René Preuß on 6/14/2017.
 */
public class StringsInputStream <T extends Iterable<String>> extends InputStream implements InputStreamFactory {

    private ByteArrayInputStream bais = null;

    public StringsInputStream(final T strings) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (String line : strings) {
            outputStream.write(line.getBytes());
        }
        bais = new ByteArrayInputStream(outputStream.toByteArray());
    }

    @Override
    public int read() throws IOException {
        return bais.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return bais.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return bais.read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return bais.skip(n);
    }

    @Override
    public int available() throws IOException {
        return bais.available();
    }

    @Override
    public void close() throws IOException {
        bais.close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        bais.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        bais.reset();
    }

    @Override
    public boolean markSupported() {
        return bais.markSupported();
    }

    @Override
    public InputStream createInputStream() throws IOException {
        return this;
    }
}
