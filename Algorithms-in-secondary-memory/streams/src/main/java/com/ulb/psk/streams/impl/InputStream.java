/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.psk.streams.impl;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author masterulb
 */
public class InputStream implements com.ulb.psk.streams.intf.InputStream {

    private DataInputStream dis;
    private boolean eof;
    private final String fileName;

    public InputStream(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void open() throws FileNotFoundException, IOException {
        this.dis = new DataInputStream(new FileInputStream(this.fileName));
        this.eof = false;
    }

    @Override
    public Integer read() throws IOException {
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

    @Override
    public Boolean endOfStream() {
        return this.eof;
    }

    @Override
    public void close() throws IOException {
        if (this.dis != null) {
            this.dis.close();
        }
    }

}
