package com.demo.code.StreamTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        StreamTest streamTest = new StreamTest(1, null);
        StreamTest streamTest2 = new StreamTest(1, null);

        ArrayList<StreamTest> streamTests = new ArrayList<>();
        streamTests.add(streamTest);
        streamTests.add(streamTest2);


        List<String> collect = streamTests.stream().map(StreamTest::getViidCode).collect(Collectors.toList());

        System.out.println(collect);

    }
}
