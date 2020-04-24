package com.grpc.sum.server;

import com.proto.sum.Sum;
import com.proto.sum.SumRequest;
import com.proto.sum.SumResponse;
import com.proto.sum.SumServiceGrpc;
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
}
