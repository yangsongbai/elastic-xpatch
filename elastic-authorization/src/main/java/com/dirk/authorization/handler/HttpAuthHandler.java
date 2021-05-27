package com.dirk.authorization.handler;

import com.dirk.authorization.config.AuthConfig;
import com.dirk.handler.BaseHandler;
import com.dirk.handler.RestRequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.env.Environment;
import org.elasticsearch.rest.*;

/**
 * @ClassName HttpAuthHandler
 * @Description 请描述类的业务用途
 * @Author yangsongbai
 * @Date 2021/5/23 下午3:26
 * @email yangsongbaivat@163.com
 * @Version 1.0
 **/
public class HttpAuthHandler extends BaseHandler {

    protected final Logger log = LogManager.getLogger(com.dirk.authorization.handler.HttpAuthHandler.class);
    private AuthConfig authConfig;

    public HttpAuthHandler(Environment environment) {
        super(environment);
    }

    /**
     * 检查es是否授权
     * @param sourceHandler
     * @param request
     * @param channel
     * @param client
     */
    public boolean authorize(RestHandler sourceHandler, RestRequest request, RestChannel channel, NodeClient client) {
        log.info("----------------authorize-------start-------------");
        log.info(request.getHeaders().isEmpty());
        log.info(request.getRemoteAddress());
        //检查是否授权等逻辑
       if (true) {
           try {
               return true;
           } catch (Exception e) {
               e.printStackTrace();
           }
       }else{
           noAuthorized(channel,"");
       }
        log.info("----------------authorize-------end-------------");
       return false;
    }
    private void noAuthorized(final RestChannel channel, String message) {
        log.warn(message);
        final BytesRestResponse response = new BytesRestResponse(RestStatus.UNAUTHORIZED, "Not authorized");
        response.addHeader("WWW-Authenticate", "Basic realm=\"ES\"");
        channel.sendResponse(response);
    }

    /**
     *
     * @param sourceHandler
     * @param restRequest
     * @param restChannel
     * @param nodeClient
     * @throws Exception
     */
    public void handleRequest(RestHandler sourceHandler, RestRequest restRequest, RestChannel restChannel, NodeClient nodeClient) throws Exception {
        BaseHandler handler =   this.getNextHandler();
        if (handler==null) return;
        if (authorize(sourceHandler,restRequest,restChannel,nodeClient)){
            boolean gon = true;
            for (RestRequestHandler hand:this.getHandlers()){
                if (!gon) break;
                gon = hand.doSomething();
            }
            if (gon){
                handler.handleRequest(sourceHandler,restRequest,restChannel,nodeClient);
            } else{
                sourceHandler.handleRequest(restRequest,restChannel,nodeClient);
            }
        }
        else{
            sourceHandler.handleRequest(restRequest, restChannel, nodeClient);
        }
    }
}
