/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.psk.streams.impl;

import com.ulb.psk.streams.intf.InputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * @author masterulb
 */
public class MemoryMappingInputStream implements InputStream {

    private static final Integer BITS_32 = 4;
    private final String fileName;
    private final Integer bufferSize;
    private boolean eof;
    private Integer intLastValue;
    private long longFileSize;
    private Integer intCurrentPosition;
    private Integer intAbsolutPosition;
    private MappedByteBuffer mbb;
    private FileChannel fc;

    public MemoryMappingInputStream(String fileName, Integer bufferSize) {
        this.fileName = fileName;
        this.bufferSize = bufferSize;
        this.intLastValue = -1;
        this.longFileSize = 0;
        this.intCurrentPosition = 0;
        this.intAbsolutPosition = 0;
        this.eof = false;
    }

    @Override
    public void open() throws FileNotFoundException, IOException {
        this.fc = new RandomAccessFile(this.fileName, "rw").getChannel();
        this.mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, this.bufferSize);
        this.longFileSize = fc.size();
    }

    @Override
    public Integer read() throws IOException {
        while (this.mbb.hasRemaining()) {
            this.intLastValue = mbb.getInt();
            this.intCurrentPosition += BITS_32;
            if (this.mbb.hasRemaining()) {
                return this.intLastValue;
            }
        }
        if (this.intCurrentPosition < this.longFileSize) {
            this.intAbsolutPosition = this.intAbsolutPosition + this.mbb.position();
            this.mbb
                    = fc.map(
                            FileChannel.MapMode.READ_ONLY,
                            this.intAbsolutPosition,
                            this.bufferSize
                    );
        }
        if (this.intCurrentPosition + BITS_32 > this.longFileSize) {
            this.eof = true;
            this.fc.close();
        }
        return this.intLastValue;
    }

    @Override
    public Boolean endOfStream() {
        return this.eof;
    }

    @Override
    public void close() throws IOException {
        this.fc.close();
    }

}
