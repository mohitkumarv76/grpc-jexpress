package com.flipkart.gjex.core.filter.http;

import com.flipkart.gjex.core.filter.RequestParams;
import com.flipkart.gjex.core.filter.ResponseParams;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * Filter for logging http access log requests
 * @author ajay.jalgaonkar
 *
 */

@Singleton
@Named("HttpAccessLogFilter")
public class HttpAccessLogFilter extends GjexHttpFilter {
  private long startTime;
  private StringBuilder sb;

  @Override
  public void doProcessRequest(RequestParams<ServletRequest, Set<String>> requestParams) {
    startTime  = System.currentTimeMillis();
    sb = new StringBuilder();
  }

  @Override
  public void doProcessResponseHeaders(Set<String> responseHeaders) {
    if (getRequest() instanceof HttpServletRequest && getResponse() instanceof HttpServletResponse){
      HttpServletRequest httpServletRequest= (HttpServletRequest) getRequest();
      HttpServletResponse httpServletResponse = (HttpServletResponse) getResponse();
      sb.append(httpServletRequest.getHeader("x-forwarded-for")).append(" ")
          .append(httpServletRequest.getRequestURI()).append(" ")
          .append(httpServletResponse.getStatus()).append(" ")
          .append(httpServletResponse.getHeader("Content-Length")).append(" ");
    } else {
      sb.append("Did not get HTTP request").append(" ");
    }
  }

  @Override
  public void doProcessResponse(ResponseParams<ServletResponse> response) {
    sb.append(getRequest().getRemoteAddr()).append(" ");
    sb.append(System.currentTimeMillis()-startTime);
    info("access-log", sb.toString());
  }
}