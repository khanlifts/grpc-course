package com.khanlifts.grpc.greeting.client;

import com.proto.dummy.DummyServiceGrpc;
import com.proto.greet.*;
import io.grpc.*;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GreetingClient {

  public static void main(String[] args) {
    System.out.println("I am a client");

    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
      .usePlaintext() // force ssl to be deactivated for the development phase
      .build();

//    System.out.println("Creating Stub");


    // old and dummy
    // DummyServiceGrpc.DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(channel);
    // DummyServiceGrpc.DummyServiceFutureStub asyncClient = DummyServiceGrpc.newFutureStub(channel);

    // create a greet service client (blocking - synchronous)
    GreetServiceGrpc.GreetServiceBlockingStub greetClient = GreetServiceGrpc.newBlockingStub(channel);

    // UNARY
//    // create a protocol buffer greeting message
//    Greeting greeting = Greeting.newBuilder()
//      .setFirstName("Cyril")
//      .setLastName("Khan")
//      .build();
//
//    // do the same for a greet request
//    GreetRequest greetRequest = GreetRequest.newBuilder()
//      .setGreeting(greeting)
//      .build();
//
//    // call the RPC and get back a GreetResponse (protocol buffers)
//    GreetResponse greetResponse = greetClient.greet(greetRequest);
//
//    System.out.println(greetResponse.getResult());

    // SERVER STREAMING
    // prepare the request
//    GreetManyTimesRequest greetManyTimesRequest = GreetManyTimesRequest.newBuilder()
//      .setGreeting(Greeting.newBuilder().setFirstName("Cyril"))
//      .build();

    // stream responses (blocking)
//    greetClient.greetManyTimes(greetManyTimesRequest).forEachRemaining(greetManyTimesResponse ->
//      System.out.println(greetManyTimesResponse.getResult()));

    doBidiStreamingCall(channel);

//    doUnaryCallWithDeadline(channel);


    System.out.println("Shutting down channel");
    channel.shutdown();
  }

  private static void doBidiStreamingCall(Channel channel) {
    GreetServiceGrpc.GreetServiceStub asyncClient = GreetServiceGrpc.newStub(channel);

    CountDownLatch latch = new CountDownLatch(1);

    StreamObserver<GreetEveryoneRequest> requestObserver = asyncClient.greetEveryone(new StreamObserver<GreetEveryoneResponse>() {
      @Override
      public void onNext(GreetEveryoneResponse value) {
        System.out.println("Response from server: " + value.getResult());
      }

      @Override
      public void onError(Throwable t) {
        latch.countDown();
      }

      @Override
      public void onCompleted() {
        System.out.println("Server is done sending data");
        latch.countDown();
      }
    });

    Arrays.asList("Cyril", "George", "Patricia", "Sven").forEach(name -> {

      System.out.println("Sending: " + name);

      Greeting greeting = Greeting.newBuilder().setFirstName(name).build();

      GreetEveryoneRequest greetEveryoneRequest = GreetEveryoneRequest.newBuilder().setGreeting(greeting).build();

      requestObserver.onNext(greetEveryoneRequest);

      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });

    requestObserver.onCompleted();

    try {
      latch.await(3, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static void doUnaryCallWithDeadline(Channel channel) {
    GreetServiceGrpc.GreetServiceBlockingStub blockingStub = GreetServiceGrpc.newBlockingStub(channel);

    // first call with 3000ms
    try {
      System.out.println("Sending a request out with a deadline of 3000ms");
      Greeting.Builder deadLineGreeting = Greeting.newBuilder().setFirstName("Cyril");
      GreetWithDeadlineRequest greetWithDeadlineRequest = GreetWithDeadlineRequest.newBuilder()
        .setGreeting(deadLineGreeting)
        .build();
      GreetWithDeadlineResponse response = blockingStub.withDeadline(Deadline.after(3000, TimeUnit.MILLISECONDS))
        .greetWithDeadline(greetWithDeadlineRequest);
      System.out.println(response.getResult());
    } catch (StatusRuntimeException e) {
      if (e.getStatus() == Status.DEADLINE_EXCEEDED) {
        System.out.println("Deadline has been exceeded, we don't want the response");
      } else {
        e.printStackTrace();
      }
    }

    // second call with 100ms
    try {
      System.out.println("Sending a request out with a deadline of 100ms");
      Greeting.Builder deadLineGreeting = Greeting.newBuilder().setFirstName("Cyril");
      GreetWithDeadlineRequest greetWithDeadlineRequest = GreetWithDeadlineRequest.newBuilder()
        .setGreeting(deadLineGreeting)
        .build();
      GreetWithDeadlineResponse response = blockingStub.withDeadline(Deadline.after(100, TimeUnit.MILLISECONDS))
        .greetWithDeadline(greetWithDeadlineRequest);
      System.out.println(response.getResult());
    } catch (StatusRuntimeException e) {
      if (e.getStatus() == Status.DEADLINE_EXCEEDED) {
        System.out.println("Deadline has been exceeded, we don't want the response");
      } else {
        e.printStackTrace();
      }
    }

  }
}
