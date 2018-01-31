/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.ulb.psk;

import com.ulb.psk.streams.impl.BufferedOutputStream;
import java.io.IOException;
import com.ulb.psk.streams.impl.MemoryMappingOutputStream;
import com.ulb.psk.streams.impl.OutputStream;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@Fork(2)
public class MyBenchmark {

    @State(Scope.Thread)
    public static class WriteState {

        @Param({"10"}) //change
        private Integer dataVolume;
        @Param({"1"}) //change
        private Integer filesToWrite;

        private OutputStream[] osarray;
        private String strFileName;

        @Setup(Level.Invocation)
        public void setup() throws IOException {
            strFileName = "test_" + dataVolume + "_" + filesToWrite;
            osarray = new OutputStream[filesToWrite];
            for (int i = 0; i < filesToWrite; i++) {
                osarray[i] = new OutputStream(strFileName + "_" + i);
            }
        }

        @TearDown(Level.Invocation)
        public void doTearDown() throws IOException {
            for (OutputStream outputstream : osarray) {
                outputstream.close();
            }
        }
    }

    @State(Scope.Thread)
    public static class WriteBufferState {

        @Param({"20000000"})
        private Integer dataVolume;
        @Param({"8192", "10000", "100000", "1000000", "10000000", "20000000"})
        private Integer bufferSize;
        @Param({"1", "2", "3", "4", "5", "10", "15", "20", "25", "30"})
        private Integer filesToWrite;

        private static final Integer BITS_32 = 4;
        private BufferedOutputStream[] osarray;
        private String strFileName;

        @Setup(Level.Invocation)
        public void setup() throws IOException {
            strFileName = "test_" + dataVolume + "_" + filesToWrite;
            osarray = new BufferedOutputStream[filesToWrite];
            for (int i = 0; i < filesToWrite; i++) {
                osarray[i] = new BufferedOutputStream(
                        strFileName + "_" + i, bufferSize * BITS_32
                );
            }
        }

        @TearDown(Level.Invocation)
        public void doTearDown() throws IOException {
            for (BufferedOutputStream outputstream : osarray) {
                outputstream.close();
            }
        }
    }

    @State(Scope.Thread)
    public static class WriteMMappingState {

        @Param({"20000000"})
        private Integer dataVolume;
        @Param({"10000", "100000", "1000000", "10000000", "20000000"})
        private Integer bufferSize;
        @Param({"1", "2", "3", "4", "5", "10", "15", "20", "25", "30"})
        private Integer filesToWrite;

        private static final Integer BITS_32 = 4;
        private MemoryMappingOutputStream[] osarray;
        private String strFileName;

        @Setup(Level.Invocation)
        public void setup() throws IOException {
            strFileName = "test_" + dataVolume + "_" + filesToWrite;
            osarray = new MemoryMappingOutputStream[filesToWrite];
            for (int i = 0; i < filesToWrite; i++) {
                osarray[i] = new MemoryMappingOutputStream(
                        strFileName + "_" + i, bufferSize * BITS_32
                );
            }
        }

        @TearDown(Level.Invocation)
        public void doTearDown() throws IOException {
            for (MemoryMappingOutputStream outputstream : osarray) {
                outputstream.close();
            }
        }
    }

    /**
     * This method write a new file with random Integer numbers using
     * OutputStreams.
     *
     * @param state
     * @throws IOException
     */
    @Benchmark
    public void writeIntFile(WriteState state) throws IOException {
        int j = 0;
        for (int i = 0; i < state.dataVolume; i++) {
            int randomNum
                    = ThreadLocalRandom
                            .current()
                            .nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
            state.osarray[j].write(randomNum);
            j++;
            if (j == state.filesToWrite) {
                j = 0;
            }
        }
    }

    /**
     * This method write a new file with random Integer numbers using
     * BufferedOutputStreams.
     *
     * @param state
     * @throws IOException
     */
    @Benchmark
    public void bufferedWriteIntFile(WriteBufferState state) throws IOException {
        int j = 0;
        for (int i = 0; i < state.dataVolume; i++) {
            int randomNum
                    = ThreadLocalRandom
                            .current()
                            .nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
            state.osarray[j].write(randomNum);
            j++;
            if (j == state.filesToWrite) {
                j = 0;
            }
        }
    }

    /**
     * This method write a new file with random Integer numbers using
     * MemoryMappingOutpuStreams.
     *
     * @param state
     * @throws IOException
     */
    @Benchmark
    public void mmWriteIntFile(WriteMMappingState state) throws IOException {
        int j = 0;
        for (int i = 0; i < state.dataVolume; i++) {
            int randomNum
                    = ThreadLocalRandom
                            .current()
                            .nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
            state.osarray[j].write(randomNum);
            j++;
            if (j == state.filesToWrite) {
                j = 0;
            }
        }
    }

    /**
     * This is to run the test from the IDE
     *
     * @param args
     * @throws IOException
     * @throws RunnerException
     */
    public static void main(String... args) throws IOException, RunnerException {
        Options opts = new OptionsBuilder()
                .include(".*")
                .warmupIterations(2)
                .measurementIterations(2)
                .jvmArgs("-Xms4g", "-Xmx4g")
                .shouldDoGC(true)
                .forks(2)
                .build();

        new Runner(opts).run();
    }

}
