/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.psk.streams.tests;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * @author masterulb
 */
public class InputStreamOld {

    private static final String FILE_EXTENSION = ".data";
    private static final Integer BITS_32 = 4;
    private DataInputStream dis;
    private boolean eof;
    private Integer intBufferSize = BITS_32;
    private Integer intLastValue = -1;
    private long longFileSize = 0;
    private Integer intCurrentPosition = 0;
    private Integer intAbsolutPosition = 0;
    private MappedByteBuffer mbb;
    private FileChannel fc;

    /**
     * This method open an existing file for reading.
     *
     * @param strFilePath
     * @throws java.io.FileNotFoundException
     */
    public void open(String strFilePath) throws FileNotFoundException, IOException {

        this.dis
                = new DataInputStream(
                        new FileInputStream(strFilePath + FILE_EXTENSION)
                );
        this.eof = false;

    }

    /**
     * This method read the next element from the stream.
     *
     * @return the read number.
     * @throws java.io.IOException
     */
    public Integer readNext() throws IOException {
        Integer intNum = null;
        if (this.dis != null) {
            intNum = this.dis.readInt();
            if (this.dis.available() <= 0) {
                this.eof = true;
                this.dis.close();
            }
        }
        return intNum;
    }

    /**
     * This method creates a file which can be read with buffers.
     *
     * @param strFilePath the file name or path.
     * @param bufferSize the size of the buffer you want to use.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void fopen(
            String strFilePath, Integer bufferSize
    ) throws FileNotFoundException, IOException {
        this.dis
                = new DataInputStream(
                        new BufferedInputStream(
                                new FileInputStream(strFilePath + FILE_EXTENSION),
                                bufferSize
                        )
                );
        this.eof = false;
    }

    /**
     * This method reads element by element, each one with a size of 32-bit,
     * from the stream and stores them in a buffer.
     *
     * @throws java.io.IOException
     * @return the 32 bit Integer read.
     */
    public Integer fread() throws IOException {
        Integer intNum = null;
        if (this.dis != null) {
            intNum = this.dis.readInt();
            if (this.dis.available() <= 0) {
                this.eof = true;
                this.dis.close();
            }
        }
        return intNum;
    }

    /**
     * This method a boolean operation that returns true if the end of stream
     * has been reached.
     *
     * @return true if the EOF has been reached.
     */
    public boolean endOfStream() {
        return this.eof;
    }

    /**
     * This method open a file using memory mapping and needs a file path and a
     * buffer size to works.
     *
     * @param strFilePath the file name or path.
     * @param bufferSize the buffer size you want to use to read the file.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void openMemoryMappedFile(
            String strFilePath, Integer bufferSize
    ) throws FileNotFoundException, IOException {
        this.fc
                = new RandomAccessFile(
                        strFilePath + FILE_EXTENSION, "rw"
                ).getChannel();
        this.mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, bufferSize);
        this.longFileSize = fc.size();
        this.intCurrentPosition = 0;
        this.intAbsolutPosition = this.intCurrentPosition;
        this.intLastValue = -1;
        this.intBufferSize = bufferSize;
        this.eof = false;

    }

    /**
     * This method return one integer number read from the memory mapping file.
     *
     * @return @throws IOException
     */
    public Integer readMemoryMappedFile() throws IOException {
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
                            this.intBufferSize
                    );
        }
        if (this.intCurrentPosition + BITS_32 > this.longFileSize) {
            this.eof = true;
            this.fc.close();
        }
        return this.intLastValue;
    }
}
