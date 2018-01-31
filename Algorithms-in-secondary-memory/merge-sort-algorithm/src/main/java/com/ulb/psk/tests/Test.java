/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.psk.tests;

import com.ulb.psk.implementation.MultiWayMerge;
import com.ulb.psk.streams.impl.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author masterulb
 */
public class Test {

    public static void main(String[] args) throws IOException {

        ArrayList<ArrayList<Integer>> nali = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            ArrayList<Integer> stream = new ArrayList<>();
            for (int j = 0; j < 100; j++) {
                int randomNum
                        = ThreadLocalRandom.current().nextInt(-1000, 1000);
                stream.add(randomNum);
            }
            Collections.sort(stream);
            nali.add(stream);
        }

        MultiWayMerge.merge(nali);

        String fileName = "test";
        Integer fileSize = 1000;
        Integer memFirstPass = 10;
        Integer d = 10;
        Integer bufferToWrite = 10 * 4;
        BufferedInputStream isb = new BufferedInputStream(fileName, bufferToWrite);
        isb.open();
        System.out.println("Printing the integers unsorted");
        for (int i = 0; !isb.endOfStream(); i++) {
            System.out.println(i + ". " + isb.read());
        }
        MultiWayMerge.externalMerge(fileName, fileSize, memFirstPass, d, bufferToWrite);
        BufferedInputStream isa = new BufferedInputStream("Iteration_11", bufferToWrite);
        isa.open();
        System.out.println("Printing the integers after the external multiway algorithm");
        for (int i = 0; !isa.endOfStream(); i++) {
            System.out.println(i + ". " + isa.read());
        }
    }
}
