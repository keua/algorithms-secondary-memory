/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.psk.streams.impl;

import com.ulb.psk.streams.intf.OutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * @author masterulb
 */
public class MemoryMappingOutputStream implements OutputStream {

    FileChannel fc;
    MappedByteBuffer mbb;
    Integer intStart;
    private final String fileName;
    private final Integer bufferSize;

    /**
     * This method create a new file which can be written with the memory
     * mapping method.
     *
     * @param fileName the file name or path.
     * @param bufferSize the buffer size you want to use to write the file.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public MemoryMappingOutputStream(
            String fileName, Integer bufferSize
    ) throws FileNotFoundException, IOException {
        this.fileName = fileName;
        this.bufferSize = bufferSize;
        File f = new File(this.fileName);
        f.delete();
        this.fc = new RandomAccessFile(f, "rw").getChannel();
        this.mbb = this.fc.map(FileChannel.MapMode.READ_WRITE, 0, this.bufferSize);
        this.intStart = 0;
    }

    @Override
    public void write(Integer value) throws IOException {
        if (this.mbb != null && this.fc != null) {
            if (!this.mbb.hasRemaining()) {

                this.intStart += this.mbb.position();
                this.mbb
                        = this.fc.map(
                                FileChannel.MapMode.READ_WRITE,
                                this.intStart,
                                this.bufferSize
                        );
            }
            this.mbb.putInt(value);
        }
    }

    @Override
    public void close() throws IOException {
        if (this.fc != null) {
            this.fc.close();
        }
    }

}
