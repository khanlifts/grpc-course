package com.grpc.prime.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class PrimeServer {

  public static void main(String[] args) throws IOException, InterruptedException {
    System.out.println("Hello gRPC");

    Server server = ServerBuilder.forPort(50051)
      .addService(new PrimeServiceImpl())
      .build();

    server.start();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.out.println("Received Shutdown Request");
      server.shutdown();
      System.out.println("Successfully shut down the server");
    }));

    server.awaitTermination();
  }
}
