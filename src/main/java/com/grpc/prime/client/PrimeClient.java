package com.grpc.prime.client;

import com.proto.prime.Prime;
import com.proto.prime.PrimeManyTimesRequest;
import com.proto.prime.PrimeServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class PrimeClient {
  public static void main(String[] args) {
    System.out.println("I'm the prime client");

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
      .usePlaintext()
      .build();

    // create a prime service client (blocking - synchronous)
    PrimeServiceGrpc.PrimeServiceBlockingStub primeClient = PrimeServiceGrpc.newBlockingStub(channel);

    long inputNumber = 2222222222222L;
    // prepare the request
    PrimeManyTimesRequest primeManyTimesRequest = PrimeManyTimesRequest.newBuilder()
      .setPrime(Prime.newBuilder().setNumber(inputNumber))
      .build();

    // stream responses (blocking)
    primeClient.primeManyTimes(primeManyTimesRequest).forEachRemaining(PrimeManyTimesResponse ->
      System.out.println(PrimeManyTimesResponse.getResult()));

    System.out.println("Shutting down channel");
    channel.shutdown();
  }
}
