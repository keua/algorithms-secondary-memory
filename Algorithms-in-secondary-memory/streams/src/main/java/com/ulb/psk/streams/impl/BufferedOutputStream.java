/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.psk.streams.impl;

import com.ulb.psk.streams.intf.OutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author masterulb
 */
public class BufferedOutputStream implements OutputStream {

    private final DataOutputStream dos;

    /**
     * This method create a file which can be written using buffering.
     *
     * @param strFileName
     * @param bufferSize the size of the buffer you want to use.
     * @throws FileNotFoundException
     */
    public BufferedOutputStream(
            String strFileName, Integer bufferSize
    ) throws FileNotFoundException {
        this.dos
                = new DataOutputStream(
                        new java.io.BufferedOutputStream(
                                new FileOutputStream(strFileName),
                                bufferSize
                        )
                );
    }

    @Override
    public void write(Integer value) throws IOException {
        if (this.dos != null) {
            this.dos.writeInt(value);
        }
    }

    @Override
    public void close() throws IOException {
        if (this.dos != null) {
            this.dos.close();
        }
    }

}
