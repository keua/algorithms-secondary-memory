/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.psk.streams.impl;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author masterulb
 */
public class OutputStream implements com.ulb.psk.streams.intf.OutputStream {

    final DataOutputStream dos;
    private final String fileName;

    public OutputStream(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
        this.dos = new DataOutputStream(new FileOutputStream(this.fileName));
    }

    @Override
    public void write(Integer intToWrite) throws IOException {
        if (this.dos != null) {
            this.dos.writeInt(intToWrite);
        }
    }

    @Override
    public void close() throws IOException {
        if (this.dos != null) {
            this.dos.close();
        }
    }
}
