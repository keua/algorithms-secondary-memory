/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ulb.psk.implementation;

import com.ulb.psk.streams.impl.MemoryMappingInputStream;
import com.ulb.psk.streams.impl.MemoryMappingOutputStream;
import com.ulb.psk.streams.intf.InputStream;
import com.ulb.psk.streams.intf.OutputStream;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

/**
 *
 * @author masterulb
 */
public class MultiWayMerge {

    static Integer d;
    static Integer n;
    static HashMap<Integer, Integer> map;
    static HashMap<Integer, Integer> indexMap;
    static PriorityQueue<Map.Entry<Integer, Integer>> pqueue;
    static ArrayList<Integer> sortedIntegers;

    /**
     *
     * @param inputstreams
     */
    public static void merge(ArrayList<ArrayList<Integer>> inputstreams) {

        d = inputstreams.size();
        n = inputstreams.get(0).size();
        indexMap = new HashMap<>();
        Integer indxStreams = 0;
        Integer indxstream = 0;
        Integer currentIndex = 0;
        map = new HashMap<>();
        sortedIntegers = new ArrayList<>();

        pqueue
                = new PriorityQueue<>(
                        d,
                        Comparator.comparing(entry -> entry.getValue())
                );

        for (int i = 0; i < d; i++) {
            ArrayList<Integer> stream = inputstreams.get(i);
            map.put(i, stream.get(0));
            indexMap.put(i, 0);
        }

        pqueue.addAll(map.entrySet());

        while (!pqueue.isEmpty()) {

            indxStreams = pqueue.peek().getKey();
            sortedIntegers.add(pqueue.poll().getValue());

            currentIndex = indexMap.get(indxStreams);
            indxstream = ++currentIndex;
            indexMap.put(indxStreams, indxstream);

            if (indxstream < inputstreams.get(indxStreams).size()) {

                Integer value = inputstreams.get(indxStreams).get(indxstream);
                HashMap.Entry<Integer, Integer> e
                        = new AbstractMap.SimpleEntry<>(indxStreams, value);
                pqueue.add(e);

            }
        }

    }

    /**
     *
     * @param inputstreams
     * @param size
     * @param buffer
     * @param fileName
     * @return
     * @throws java.io.IOException
     */
    public static InputStream mergeStreams(
            ArrayList<InputStream> inputstreams,
            Integer size,
            Integer buffer,
            String fileName
    ) throws IOException {

        d = inputstreams.size();
        n = size;
        Integer indxStreams = 0;
        map = new HashMap<>();
        OutputStream os = new MemoryMappingOutputStream(fileName, buffer);
        pqueue
                = new PriorityQueue<>(
                        d,
                        Comparator.comparing(entry -> entry.getValue())
                );

        for (int i = 0; i < d; i++) {
            inputstreams.get(i).open();
            map.put(i, inputstreams.get(i).read());
        }

        pqueue.addAll(map.entrySet());

        while (!pqueue.isEmpty()) {

            indxStreams = pqueue.peek().getKey();
            os.write(pqueue.poll().getValue());

            if (!inputstreams.get(indxStreams).endOfStream()) {

                Integer value = inputstreams.get(indxStreams).read();
                HashMap.Entry<Integer, Integer> e
                        = new AbstractMap.SimpleEntry<>(indxStreams, value);
                pqueue.add(e);

            }
        }
        os.close();
        return new MemoryMappingInputStream(fileName, buffer);
    }

    /**
     *
     * @param file
     * @param size
     * @param memFirstPhase
     * @param d
     * @param bufferToWrite
     * @throws java.io.IOException
     */
    public static void externalMerge(
            String file,
            Integer size, // N
            Integer memFirstPhase, // M
            Integer d,
            Integer bufferToWrite
    ) throws IOException {
        // Opening the file
        InputStream is = new MemoryMappingInputStream(file, memFirstPhase * 4);
        is.open();
        // First generate N/M files 
        Integer nm = size / memFirstPhase;
        LinkedList<InputStream> isQueue = new LinkedList<>();
        ArrayList<Integer> arrayToWrite = new ArrayList<>();
        ArrayList<InputStream> streamsToMerge = new ArrayList<>();

        // Filling the N/M files
        for (int i = 0; !is.endOfStream(); i++) {
            for (int j = 0; j < memFirstPhase; j++) {
                arrayToWrite.add(is.read());
            }
            // Sorting the stream
            Collections.sort(arrayToWrite);
            // Writting the stream
            OutputStream os
                    = new MemoryMappingOutputStream("nm_" + i, bufferToWrite);
            for (Integer value : arrayToWrite) {
                os.write(value);
            }
            os.close();
            arrayToWrite.clear();
            isQueue.add(new MemoryMappingInputStream("nm_" + i, bufferToWrite));
        }
        // Start merging
        Integer iteration = 1;
        while (isQueue.size() > 1) {
            // Load in memory
            for (int i = 0; i < d; i++) {
                if (!isQueue.isEmpty()) {
                    streamsToMerge.add(isQueue.poll());
                }
            }
            // Merge
            isQueue.add(
                    MultiWayMerge.mergeStreams(
                            streamsToMerge,
                            size,
                            bufferToWrite,
                            "Iteration_" + iteration
                    )
            );
            streamsToMerge.clear();
            iteration++;
        }
    }
}
