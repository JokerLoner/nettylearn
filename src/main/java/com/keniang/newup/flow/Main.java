package com.keniang.newup.flow;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.LongStream;

public class Main {
    final static int MAX_SLEEP_DURATION = 3;
    // Used to generate sleep time
    final static Random sleepTimeGenerator = new Random();

    public static void main(String[] args) {

        SubmissionPublisher<Long> pub = new SubmissionPublisher<>();
        // Create three subscribers
        SimpleSubscriber sub1 = new SimpleSubscriber("S1", 2);
        SimpleSubscriber sub2 = new SimpleSubscriber("S2", 5);
        SimpleSubscriber sub3 = new SimpleSubscriber("S3", 6);
        SimpleSubscriber sub4 = new SimpleSubscriber("S4", 10);
        // Subscriber to the publisher
        pub.subscribe(sub1);
        pub.subscribe(sub2);
        pub.subscribe(sub3);
        // Subscribe the 4th subscriber after 2 seconds
        subscribe(pub, sub4, 2);
        // Start publishing items
        Thread pubThread = publish(pub, 5);
        try {
            // Wait until the publisher is finished
            pubThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        CompletableFuture<Void> subTask = null;
        try (SubmissionPublisher<Long> pub1 = new SubmissionPublisher<>()) {
            // Print the buffer size used for each subscriber
            System.out.println("Subscriber Buffer Size: " + pub1.getMaxBufferCapacity());
            // Add a subscriber to the publisher. The subscriber prints the published elements
            subTask = pub1.consume(System.out::println);
            LongStream.range(1L, 6L).forEach(pub1::submit);
        }
        try {
            // Wait until the subscriber is complete
            subTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        //Create Publisher
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

        //Register Subscriber
        MySubscriber<String> subscriber = new MySubscriber<>();
        publisher.subscribe(subscriber);

        //Publish items
        System.out.println("Publishing Items...");
        String[] items = {"1", "x", "2", "x", "3", "x"};
        Arrays.asList(items).stream().forEach(i -> publisher.submit(i));
        publisher.close();
    }

    public static Thread publish(SubmissionPublisher<Long> pub, long count) {
        Thread t = new Thread(() -> {
            for (long i = 1; i <= count; i++) {
                pub.submit(i);
                sleep(i);
            }
            // Close the publisher
            pub.close();
        });
        // Start the thread
        t.start();
        return t;
    }
    private static void sleep(Long item) {
        // Wait for 1 to 3 seconds
        int sleepTime = sleepTimeGenerator.nextInt(MAX_SLEEP_DURATION) + 1;
        try {
            System.out.printf("Published %d. Sleeping for %d sec.%n", item, sleepTime);
            TimeUnit.SECONDS.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static void subscribe(SubmissionPublisher<Long> pub, Flow.Subscriber<Long> sub,
                                  long delaySeconds) {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(delaySeconds);
                pub.subscribe(sub);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
