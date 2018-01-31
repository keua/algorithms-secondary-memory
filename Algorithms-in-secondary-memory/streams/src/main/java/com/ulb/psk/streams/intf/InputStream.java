/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.psk.streams.intf;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author masterulb
 */
public interface InputStream {

    /**
     * This method open a file to read.
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void open() throws FileNotFoundException, IOException;

    /**
     * This method reads element by element, each one with a size of 32-bit.
     *
     * @throws java.io.IOException
     * @return the 32 bit Integer read.
     */
    public Integer read() throws IOException;

    /**
     * This method is validation that returns true if the end of stream
     * has been reached.
     *
     * @return true if the EOF has been reached.
     */
    public Boolean endOfStream();
    
    /**
     * This method close the stream.
     *
     * @throws java.io.IOException
     */
    public void close() throws IOException;

}
