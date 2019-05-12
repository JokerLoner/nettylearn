package com.keniang.newup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        var javastack = "javastack";
        System.out.println(javastack);

        Stream.of(1,34,4545,231,1).takeWhile(n -> n < 300).collect(Collectors.toList()).forEach(System.out::println);
        Stream.of(1,34,4545,231,1).dropWhile(n -> n < 300).collect(Collectors.toList()).forEach(System.out::println);

        //InputStream 加强
        //InputStream 终于有了一个非常有用的方法：transferTo，
        // 可以用来将数据直接传输到 OutputStream，这是在处理原始数据流时非常常见的一种用法，如下示例。
        var inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("javastack.txt");
        var javastack2 = File.createTempFile("javastack2","txt");
        try (var outputStream = new FileOutputStream(javastack2)) {
            inputStream.transferTo(outputStream);
        }


        //这是 Java 9 开始引入的一个处理 HTTP 请求的的孵化 HTTP Client API，
        // 该 API 支持同步和异步，而在 Java 11 中已经为正式可用状态，你可以在 java.net 包中找到这个 API。
        var request = HttpRequest.newBuilder()
                .uri(URI.create("https://javastack.cn"))
                .GET()
                .build();
        var client = HttpClient.newHttpClient();
// 同步
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
// 异步
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println);




    }
}
