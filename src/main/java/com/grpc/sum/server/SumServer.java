package com.grpc.sum.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class SumServer {

  public static void main(String[] args) throws IOException, InterruptedException {
    System.out.println("Hello Sum Server");

    Server server = ServerBuilder.forPort(50051)
      .addService(new SumServiceImpl())
      .build();

    server.start();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.out.println("Received Shutdown Request");
      server.shutdown();
      System.out.println("Successfully stopped the server");
    }));

    server.awaitTermination();
  }
}
