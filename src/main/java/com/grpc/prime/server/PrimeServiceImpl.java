package com.grpc.prime.server;

import com.proto.prime.PrimeManyTimesRequest;
import com.proto.prime.PrimeManyTimesResponse;
import com.proto.prime.PrimeServiceGrpc;
import io.grpc.stub.StreamObserver;

public class PrimeServiceImpl extends PrimeServiceGrpc.PrimeServiceImplBase {

  @Override
  public void primeManyTimes(PrimeManyTimesRequest request, StreamObserver<PrimeManyTimesResponse> responseObserver) {
    long number = request.getPrime().getNumber();

    long divisor = 2;

    while (number > 1) {
      if (number % divisor == 0) {
        number /= divisor;
        PrimeManyTimesResponse response = PrimeManyTimesResponse.newBuilder()
          .setResult(divisor)
          .build();
        responseObserver.onNext(response);
      } else {
        divisor += 1;
      }
    }

    responseObserver.onCompleted();
  }
}
