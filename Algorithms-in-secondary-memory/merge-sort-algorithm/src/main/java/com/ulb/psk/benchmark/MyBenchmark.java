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
package com.ulb.psk.benchmark;

import com.ulb.psk.implementation.MultiWayMerge;
import com.ulb.psk.streams.impl.MemoryMappingOutputStream;
import com.ulb.psk.streams.intf.OutputStream;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
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

    @Param({"40000000"})
    public Integer fileSize; // N
    @Param({"1000000"})
    Integer firstPhaseMemory; // M
    @Param({"2","3","4","5","10","15","20","30","40"})
    Integer d; // d
    private static final Integer BITS_32 = 4;
    private static final Integer BUFFER_TO_WRITE = BITS_32 * 10000;

    @Setup
    public void setup() throws IOException {
        OutputStream os
                = new MemoryMappingOutputStream(
                        "test_" + fileSize, fileSize * BITS_32
                );
        for (int i = 0; i < fileSize; i++) {
            int randomNum
                    = ThreadLocalRandom.current()
                            .nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
            os.write(randomNum);
        }
        os.close();
    }

    @Benchmark
    public void externalMerge() throws IOException {
        MultiWayMerge.externalMerge(
                "test_" + fileSize,
                fileSize, // N
                firstPhaseMemory, // M
                d, // d
                BUFFER_TO_WRITE
        );
    }
    /**
     * This is for run in the IDE
     * @param args
     * @throws IOException
     * @throws RunnerException 
     */
    public static void main(String... args) throws IOException, RunnerException {
        Options opts = new OptionsBuilder()
                .include(".*")
                .warmupIterations(2)
                .measurementIterations(2)
                .jvmArgs("-Xms2g", "-Xmx2g")
                .shouldDoGC(true)
                .forks(2)
                .build();
        new Runner(opts).run();
    }

}
