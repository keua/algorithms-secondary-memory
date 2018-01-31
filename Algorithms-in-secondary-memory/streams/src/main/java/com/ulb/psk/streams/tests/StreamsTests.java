/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.psk.streams.tests;

import com.ulb.psk.streams.impl.BufferedInputStream;
import com.ulb.psk.streams.impl.BufferedOutputStream;
import java.io.IOException;
import com.ulb.psk.streams.impl.InputStream;
import com.ulb.psk.streams.impl.MemoryMappingInputStream;
import com.ulb.psk.streams.impl.MemoryMappingOutputStream;
import com.ulb.psk.streams.impl.OutputStream;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author masterulb
 */
public class StreamsTests {

    private static final Integer BITS_32 = 4;

    /**
     * This method write a new file with random Integer numbers.
     *
     * @param qtyNumbers represents the quantity of numbers to write in the
     * file.
     * @param fileName file name or path.
     * @throws IOException
     */
    public void writeIntFile(Integer qtyNumbers, String fileName) throws IOException {
        OutputStream os = new OutputStream(fileName);
        for (int i = 0; i < qtyNumbers; i++) {
            int randomNum
                    = ThreadLocalRandom.current()
                            .nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
            os.write(randomNum);
        }
        os.close();
    }

    /**
     * This method read the Integer numbers from a file.
     *
     * @param fileName
     * @throws IOException
     */
    public void readIntFile(String fileName) throws IOException {
        InputStream is = new InputStream(fileName);
        is.open();
        while (!is.endOfStream()) {
            System.out.println(is.read());
        }
    }

    /**
     * This method read the Integer numbers from a file with buffers.
     *
     * @param strFilePath file name or path.
     * @param bufferSize buffer size you want use.
     * @throws IOException
     */
    public void readIntFileBuffer(
            String strFilePath, Integer bufferSize
    ) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(strFilePath, bufferSize);
        bis.open();
        while (!bis.endOfStream()) {
            System.out.println(bis.read());
        }
    }

    /**
     * This method write a new file with random Integer numbers using buffers.
     *
     * @param qtyNumbers represents the quantity of numbers to write in the
     * file.
     * @param strFilePath file name or path.
     * @param bufferSize buffer size you want use.
     * @throws IOException
     */
    public void writeIntFileBuffer(
            Integer qtyNumbers, String strFilePath, Integer bufferSize
    ) throws IOException {
        BufferedOutputStream os = new BufferedOutputStream(strFilePath, bufferSize);
        for (int i = 0; i < qtyNumbers; i++) {
            int randomNum
                    = ThreadLocalRandom.current()
                            .nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
            os.write(randomNum);
        }
        os.close();
    }

    /**
     * This method invokes the memory mapping channel to write it with some
     * random integer values.
     *
     * @param qtyNumbers the quantity of integer values you want to write.
     * @param fileName the name of the file to write.
     * @param bufferSize the buffer size you want to use.
     * @throws IOException
     */
    public void writeIntFileMemoryMapping(
            Integer qtyNumbers, String fileName, Integer bufferSize
    ) throws IOException {
        MemoryMappingOutputStream mmos
                = new MemoryMappingOutputStream(fileName, bufferSize);
        for (int i = 0; i < qtyNumbers; i++) {
            int randomNum
                    = ThreadLocalRandom.current()
                            .nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
            mmos.write(randomNum);
        }
        mmos.close();
    }

    /**
     * This method invokes the memory mapping channel to read a file of integer
     * values.
     *
     * @param fileName the file name you want to read.
     * @param bufferSize the buffer size you want to use.
     * @throws IOException
     */
    public void readIntFileMemoryMapping(
            String fileName, Integer bufferSize
    ) throws IOException {
        MemoryMappingInputStream mmis
                = new MemoryMappingInputStream(fileName, bufferSize);
        mmis.open();
        while (!mmis.endOfStream()) {
            System.out.println(mmis.read());
        }
    }

    public static void main(String... args) throws IOException {
        StreamsTests test = new StreamsTests();
        String fileName = "test";
        Integer bufferSize = 10 * BITS_32;
        Integer qtyIntegers = 1000;
        // write and read without buffering
        System.out.println("//write and read without buffering");
        test.writeIntFile(qtyIntegers, fileName);
        test.readIntFile(fileName);
        // write and read with buffering
        System.out.println("//write and read with buffering");
        test.writeIntFileBuffer(qtyIntegers, fileName + "_buffer", bufferSize);
        test.readIntFileBuffer(fileName + "_buffer", bufferSize);
        //write and read with memory mapping
        System.out.println("//write and read with memory mapping");
        test.writeIntFileMemoryMapping(qtyIntegers, fileName + "_mapping", bufferSize);
        test.readIntFileMemoryMapping(fileName + "_mapping", bufferSize);
    }
}
