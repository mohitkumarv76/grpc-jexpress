package com.flipkart.gjex.core.filter.grpc;

import com.flipkart.gjex.core.filter.RequestParams;
import com.flipkart.gjex.core.filter.ResponseParams;
import com.flipkart.gjex.core.logging.Logging;
import com.google.protobuf.GeneratedMessageV3;
import io.grpc.Metadata;

/**
 * Filter for logging grpc access log requests
 * @author ajay.jalgaonkar
 *
 */
public class GrpcAccessLogGjexGrpcFilter<R extends GeneratedMessageV3,
    S extends GeneratedMessageV3> extends GjexGrpcFilter<R,S> implements Logging {
  private long startTime = 0;
  private RequestParams<R, Metadata> requestParams;
  @Override
  public GjexGrpcFilter<R,S> getInstance(){
    return new GrpcAccessLogGjexGrpcFilter<>();
  }

  @Override
  public void doProcessRequest(RequestParams<R, Metadata> requestParams) {
    this.startTime = System.currentTimeMillis();
    this.requestParams = requestParams;
  }

  @Override
  public void doProcessResponseHeaders(Metadata responseHeaders) {}

  @Override
  public void doProcessResponse(ResponseParams<S> responseParams) {
    String size = null;
    if (responseParams.getResponse() != null){
      size = String.valueOf(responseParams.getResponse().getSerializedSize());
    }
    StringBuilder sb = new StringBuilder()
        .append(requestParams.getClientIp()).append(" ")
        .append(requestParams.getResourcePath()).append(" ")
        .append(size).append(" ")
        .append(System.currentTimeMillis()-startTime);
    info("access-log", sb.toString());
  }
}
