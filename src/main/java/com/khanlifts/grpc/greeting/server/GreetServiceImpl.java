package com.khanlifts.grpc.greeting.server;

import com.proto.greet.*;
import io.grpc.Context;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {

  @Override
  public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
    // extract fields we need
    Greeting greeting = request.getGreeting();
    String firstName = greeting.getFirstName();

    // create the response
    String result = "Hello " + firstName;
    GreetResponse response = GreetResponse.newBuilder()
      .setResult(result)
      .build();

    // send the response
    responseObserver.onNext(response);

    // complete the RPC call
    responseObserver.onCompleted();
  }

  @Override
  public void greetManyTimes(GreetManyTimesRequest request, StreamObserver<GreetManyTimesResponse> responseObserver) {

    String firstName = request.getGreeting().getFirstName();

    try {
      for (int i = 0; i < 10; i++) {
        String result = "Hello " + firstName + " response number: " + i;
        GreetManyTimesResponse response = GreetManyTimesResponse.newBuilder()
          .setResult(result)
          .build();

        responseObserver.onNext(response);
        Thread.sleep(1000L);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      responseObserver.onCompleted();
    }
  }

  @Override
  public void greetWithDeadline(GreetWithDeadlineRequest request, StreamObserver<GreetWithDeadlineResponse> responseObserver) {

    Context current = Context.current();

    try {
      for (int i = 0; i < 3; i++) {
        if (!current.isCancelled()) {
          System.out.println("Sleep for 100ms");
          Thread.sleep(100);
        } else {
          return;
        }
      }

      System.out.println("Send a response");
      responseObserver.onNext(
        GreetWithDeadlineResponse.newBuilder()
          .setResult("Hello " + request.getGreeting().getFirstName())
          .build()
      );

      responseObserver.onCompleted();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
