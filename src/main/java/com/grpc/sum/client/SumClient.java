package com.grpc.sum.client;

import com.proto.sum.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class SumClient {

  public static void main(String[] args) {
    System.out.println("I'm the sum client");

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
      .usePlaintext() // force ssl to be deactivated for the dev phase
      .build();

//    doSumCall(channel);

    doErrorCall(channel);

    System.out.println("Shutting down channel");
    channel.shutdown();
  }

  public static void doSumCall (ManagedChannel channel) {
    // create a greet service client (blocking)
    SumServiceGrpc.SumServiceBlockingStub sumBlockingStub = SumServiceGrpc.newBlockingStub(channel);

    // create a protocol buffer sum message
    Sum sum = Sum.newBuilder()
      .setFirstNum(10)
      .setSecondNum(3)
      .build();

    // do the same for a sum request
    SumRequest sumRequest = SumRequest.newBuilder()
      .setSum(sum)
      .build();

    // call the RPC and get back a SumResponse (protocol buffer)
    SumResponse sumResponse = sumBlockingStub.sum(sumRequest);

    System.out.println(sumResponse.getResult());
  }

  public static void doErrorCall(ManagedChannel channel) {
    SumServiceGrpc.SumServiceBlockingStub blockingStub = SumServiceGrpc.newBlockingStub(channel);

    int inputNumber = -1;
    try {
      SquareRootResponse squareRootResponse = blockingStub.squareRoot(
        SquareRootRequest.newBuilder()
          .setNumber(inputNumber)
          .build()
      );
    } catch (StatusRuntimeException e) {
      System.out.println("Got an exception for square root!");
      e.printStackTrace();
    }
  }
}
