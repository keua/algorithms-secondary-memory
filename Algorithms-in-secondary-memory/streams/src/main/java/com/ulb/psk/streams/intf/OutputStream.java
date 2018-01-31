/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.psk.streams.intf;

import java.io.IOException;

/**
 *
 * @author masterulb
 */
public interface OutputStream {

    /**
     * This method write an element to the stream.
     *
     * @param value the integer you want to write in the file.
     * @throws java.io.IOException
     */
    public void write(Integer value) throws IOException;

    /**
     * This method close the stream.
     *
     * @throws java.io.IOException
     */
    public void close() throws IOException;
}
