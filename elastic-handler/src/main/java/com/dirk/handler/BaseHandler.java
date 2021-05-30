package com.dirk.handler;

import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.env.Environment;
import org.elasticsearch.rest.RestChannel;
import org.elasticsearch.rest.RestHandler;
import org.elasticsearch.rest.RestRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName BaseHandler
 * @Description 请描述类的业务用途
 * @Author yangsongbai
 * @Date 2021/5/28 上午1:02
 * @email yangsongbaivat@163.com
 * @Version 1.0
 **/
public class BaseHandler implements RestHandler {

    private BaseHandler nextHandler;

    private List<RestRequestHandler> handlers = new ArrayList();;

    public BaseHandler() {
    }

    public BaseHandler(Environment environment) {

    }
    public void registerHandler(RestRequestHandler handler){
        handlers.add(handler);
    }

    public List<RestRequestHandler> getHandlers() {
        return handlers;
    }

    public void setNextHandler(BaseHandler handler) throws Exception {
         this.nextHandler = nextHandler;
    }

    public BaseHandler getNextHandler() throws Exception {
           return this.nextHandler;
    }

    @Override
    public void handleRequest(RestRequest restRequest, RestChannel restChannel, NodeClient nodeClient) throws Exception {

    }

    public void handleRequest(RestHandler sourceHandler, RestRequest restRequest, RestChannel restChannel, NodeClient nodeClient) throws Exception {
        boolean gon = true;
        if (handlers!=null&&this.getHandlers().size()>0) {
            for (RestRequestHandler hand : handlers) {
                if (!gon) break;
                gon = hand.doSomething();
            }
        }
      if(gon){
          if ( this.getNextHandler()!=null)  this.getNextHandler().handleRequest(sourceHandler,restRequest,restChannel,nodeClient);
          else sourceHandler.handleRequest(restRequest,restChannel,nodeClient);
          return;
        }
       sourceHandler.handleRequest(restRequest,restChannel,nodeClient);
    }

    @Override
    public boolean canTripCircuitBreaker() {
        return true;
    }

    @Override
    public boolean supportsContentStream() {
        return false;
    }
}
