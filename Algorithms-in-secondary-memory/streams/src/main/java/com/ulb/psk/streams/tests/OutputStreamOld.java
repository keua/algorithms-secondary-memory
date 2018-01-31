/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.psk.streams.tests;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * @author masterulb
 */
public class OutputStreamOld {

    private static final String FILE_EXTENSION = ".data";
    private static final Integer BITS_32 = 4;
    private DataOutputStream dos;
    FileChannel fc;
    MappedByteBuffer mbb;
    Integer intStart = 0;
    Integer intBufferSize = BITS_32;

    /**
     * This method creates a new file.
     *
     * @param strFileName is the file name.
     * @throws java.io.FileNotFoundException
     */
    public void create(String strFileName) throws FileNotFoundException, IOException {

        this.dos
                = new DataOutputStream(
                        new FileOutputStream(strFileName + FILE_EXTENSION)
                );

    }

    /**
     * This method write an element to the stream.
     *
     * @param intToWrite the integer you want to write in the file.
     * @throws java.io.IOException
     */
    public void write(Integer intToWrite) throws IOException {
        if (this.dos != null) {
            this.dos.writeInt(intToWrite);
        }
    }

    /**
     * This method create a file which can be written using buffering.
     *
     * @param strFilePath the file name or path.
     * @param bufferSize the size of the buffer you want to use.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void fopen(String strFilePath, Integer bufferSize) throws FileNotFoundException, IOException {

        this.dos
                = new DataOutputStream(
                        new BufferedOutputStream(
                                new FileOutputStream(strFilePath + FILE_EXTENSION),
                                bufferSize
                        )
                );
    }

    /**
     * This method write one element which is a 32 Integer, from the buffer.
     *
     * @param intToWrite this is the 32 bit Integer to write.
     * @throws java.io.IOException
     */
    public void fwrite(Integer intToWrite) throws IOException {
        if (this.dos != null) {
            this.dos.writeInt(intToWrite);
        }
    }

    /**
     * This method close the stream.
     *
     * @throws java.io.IOException
     */
    public void close() throws IOException {
        if (this.dos != null) {
            this.dos.close();
        }
    }

    /**
     * This method create a new file which can be written with the memory
     * mapping method.
     *
     * @param fileName the file name or path.
     * @param bufferSize the buffer size you want to use to write the file.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void createMemoryMappingFile(
            String fileName, Integer bufferSize
    ) throws FileNotFoundException, IOException {
        
        File f = new File(fileName + FILE_EXTENSION);
        this.fc = new RandomAccessFile(f, "rw").getChannel();
        this.mbb = this.fc.map(FileChannel.MapMode.READ_WRITE, 0, bufferSize);
        this.intStart = 0;
        this.intBufferSize = bufferSize;
    }

    /**
     * This method allows you to write a 32 bit integer value inside of a memory
     * mapping file.
     *
     * @param intToWrite this is the 32 bit integer you want to write in the
     * file.
     * @throws IOException
     */
    public void writeMemoryMapping(Integer intToWrite) throws IOException {
        if (this.mbb != null && this.fc != null) {
            if (!this.mbb.hasRemaining()) {
                this.intStart += this.mbb.position();
                this.mbb
                        = this.fc.map(
                                FileChannel.MapMode.READ_WRITE,
                                this.intStart,
                                this.intBufferSize
                        );
            }
            this.mbb.putInt(intToWrite);
        }
    }

    /**
     * This method closes the channel opened for the Memory mapping algorithm.
     *
     * @throws java.io.IOException
     */
    public void closeMemoryMappingChannel() throws IOException {
        this.fc.close();
    }
}
