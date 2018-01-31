# External-memory merge-sort algorithm

This project consists in a set of implementations to reach the External-memory merge-sort algorithm implementations. In the first part we have to implement inpustreams and outpustreams playing with different parameters to test which of the diferents configurations are the best one.

The implementation basically is a maven project witho 4 sub-maven projects inside
```
Algorithms-in-secondary-memory
├── merge-sort-algorithm
│   ├── src/main/java
│   │   ├── com.ulb.psk.benchmark
│   │   │   ├── MyBenchmark.java
│   │   ├── com.ulb.psk.tests
│   │   │   ├── Test.java
├── read-benchmarking
│   ├── src/main/java
│   │   ├── com.ulb.psk.benchmark
│   │   │   ├── MyBenchmark.java
├── write-benchmarking
│   ├── src/main/java
│   │   ├── com.ulb.psk.benchmark
│   │   │   ├── MyBenchmark.java
── streams
│   ├── src/main/java
│   │   ├── com.ulb.psk.streams
│   │   │   ├── impl
│   │   │   │   ├── BufferedInputStream.java
│   │   │   │   ├── BufferedOutputStream.java
│   │   │   │   ├── InputStream.java
│   │   │   │   ├── OutputStream.java
│   │   │   │   ├── MemoryMappingInputStream.java
│   │   │   │   ├── MemoryMappingOutputStream.java
│   │   │   ├── intf
│   │   │   │   ├── InputStream.java
│   │   │   │   ├── OutputStream.java
│   │   │   ├── tests
│   │   │   │   ├── StreamTests.java
```

## Getting Started

To create a new project with Java and JMH we should use `Maven` who provides us and archetype for the tests, we should run the following command in the terminal.

```
mvn archetype:generate -DinteractiveMode=false -DarchetypeGroupId=org.openjdk.jmh -DarchetypeArtifactId=jmh-java-benchmark-archetype -DgroupId=com.ulb.psk -DartifactId=merge-sort-algorithm -Dversion=1.0
```

We can open the project with `NetBeans IDE` and programming there.

### Prerequisites

- Java 1.8
- Maven
- NetBeans IDE

### Installing

 Then we can run build the jar file to run with:

 ```
 mvn clean install
 ```

## Running the tests

 you have to open a terminal and go to the `jars folder`, you will find the following jar files:

- external-multi-way-merege-test.jar -> `test if the algorithm works`
- streams-test.jar -> `test if the streams work`
- external-merge-sort-benchmarks.jar -> `run the algorithm benchmarking`
- read-benchmarks.jar -> `run the reading benchmarking`
- write-benchmarks.jar -> `run the writting benchmarking`

 Finally you can run it with:
 ```
 java -jar the-jar-you-want-to-run.jar
 ```

`note:` if you want to change the JMH parameters you can, but you have to clean and build all the project. The new jars will be in the target/ folder of each sub-maven project, for example: `\Algorithms-in-secondary-memory\read-benchmarking\target`

## Deployment


## Built With

* [Java](https://java.com/en/download/) - The language used
* [Maven](https://maven.apache.org/) - Dependency Management
* [JMH](http://openjdk.java.net/projects/code-tools/jmh/) - Used to benchmark the algorithms
* [Netbeans](https://netbeans.org/) - works on IDE 

## Contributing

To contribute to this project you can clone the repositroy

```
git clone http://wit-projects.ulb.ac.be/rhodecode/INFO-H-417/2017-2018-1/project-bourdais-kashef-ubeda
```

`important:` you must work in the `working-branch`, code your part and then do a `Merge request` to the `Master` to add your changes in the `Master` branch.

## Versioning

We use kallithea and this our repository    `http://wit-projects.ulb.ac.be/rhodecode/INFO-H-417/2017-2018-1/project-bourdais-kashef-ubeda`

## Authors

* **Pierre** - *Merge sort algorithm* 
* **Shafag** - *Merge sort algorithm* 
* **Keneth** - *Merge sort algorithm* 


## License


## Acknowledgments

