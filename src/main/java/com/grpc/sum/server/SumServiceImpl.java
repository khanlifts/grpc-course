package com.grpc.sum.server;

import com.proto.sum.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class SumServiceImpl extends SumServiceGrpc.SumServiceImplBase {

  @Override
  public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {

    // extract fields we need
    Sum sum = request.getSum();

    int firstNumber = sum.getFirstNum();
    int secondNumber = sum.getSecondNum();

    // create response
    int result = firstNumber + secondNumber;
    SumResponse response = SumResponse.newBuilder()
      .setResult(result)
      .build();

    // send response
    responseObserver.onNext(response);

    // complete the RPC call
    responseObserver.onCompleted();

  }

  @Override
  public void squareRoot(SquareRootRequest request, StreamObserver<SquareRootResponse> responseObserver) {

    int number = request.getNumber();

    if (number >= 0) {
      double numberRoot = Math.sqrt(number);
      responseObserver.onNext(
        SquareRootResponse.newBuilder()
          .setNumberRoot(numberRoot)
          .build()
      );
      responseObserver.onCompleted();
    } else {
      responseObserver.onError(
        Status.INVALID_ARGUMENT
          .withDescription("The number being sent is not positive")
          .augmentDescription("Number sent: " + number)
        .asRuntimeException()
      );
    }
  }
}
