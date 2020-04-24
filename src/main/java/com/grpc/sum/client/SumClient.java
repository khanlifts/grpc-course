package com.grpc.sum.client;

import com.proto.sum.Sum;
import com.proto.sum.SumRequest;
import com.proto.sum.SumResponse;
import com.proto.sum.SumServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class SumClient {

  public static void main(String[] args) {
    System.out.println("I'm the sum client");

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
      .usePlaintext() // force ssl to be deactivated for the dev phase
      .build();

    // create a greet service client (blocking)
    SumServiceGrpc.SumServiceBlockingStub sumClient = SumServiceGrpc.newBlockingStub(channel);

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
    SumResponse sumResponse = sumClient.sum(sumRequest);

    System.out.println(sumResponse.getResult());

    System.out.println("Shutting down channel");
    channel.shutdown();
  }
}
