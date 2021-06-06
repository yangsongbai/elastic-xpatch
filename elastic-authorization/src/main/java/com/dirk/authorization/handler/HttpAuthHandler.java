package com.xpatch.authorization.handler;

import com.xpatch.authorization.config.AuthConfig;
import com.xpatch.handler.BaseHandler;
import com.xpatch.handler.RestRequestHandler;
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

    protected final Logger log = LogManager.getLogger(com.xpatch.authorization.handler.HttpAuthHandler.class);
    private AuthConfig authConfig;

    public HttpAuthHandler() {
    }

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
       // log.info(request.getHeaders().isEmpty());
        //检查是否授权等逻辑
       /*if (true) {
           try {
               return true;
           } catch (Exception e) {
               e.printStackTrace();
           }
       }else{
           noAuthorized(channel,"");
       }*/
        String method =  request.method().name();
        log.info("method  =  {}",method);
        log.info("request =  {}",request.content().utf8ToString());
       return true;
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
        if (authorize(sourceHandler,restRequest,restChannel,nodeClient)){
            boolean gon = true;
            if (this.getHandlers()!=null&&this.getHandlers().size()>0) {
                for (RestRequestHandler hand : this.getHandlers()) {
                    if (!gon) break;
                    gon = hand.doSomething();
                }
            }
            if (gon){
                if (this.getNextHandler()!=null)
                    this.getNextHandler().handleRequest(sourceHandler,restRequest,restChannel,nodeClient);
                else sourceHandler.handleRequest(restRequest,restChannel,nodeClient);
                return ;
            }
            sourceHandler.handleRequest(restRequest,restChannel,nodeClient);
            return;
        }
        sourceHandler.handleRequest(restRequest, restChannel, nodeClient);
    }

}
