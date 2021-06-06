package com.xpatch.ratelimiter.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.settings.ClusterSettings;
import org.elasticsearch.common.settings.IndexScopedSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.*;

import java.io.IOException;
import java.net.SocketAddress;

/**
 * @ClassName SciHandler
 * @Description 请描述类的业务用途
 * @Author yangsongbai
 * @Date 2021/5/23 下午5:54
 * @email yangsongbaivat@163.com
 * @Version 1.0
 **/
public class SciHandler extends BaseRestHandler {
    protected final Logger log = LogManager.getLogger(com.xpatch.ratelimiter.handler.SciHandler.class);
    public SciHandler(Settings settings, RestController controller, ClusterSettings clusterSettings, IndexScopedSettings indexScopedSettings) {
        super(settings);
       /* controller.registerHandler(RestRequest.Method.POST, "/", this);
        controller.registerHandler(RestRequest.Method.DELETE, "/", this);
        controller.registerHandler(RestRequest.Method.OPTIONS, "/", this);
        controller.registerHandler(RestRequest.Method.CONNECT, "/", this);
        controller.registerHandler(RestRequest.Method.PATCH, "/", this);
        controller.registerHandler(RestRequest.Method.PUT, "/", this);
        controller.registerHandler(RestRequest.Method.HEAD, "/", this);
        controller.registerHandler(RestRequest.Method.TRACE, "/", this);
        controller.registerHandler(RestRequest.Method.GET, "/", this);*/
    }

    @Override
    public String getName() {
        return "SciHandler";
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient client) throws IOException {
        log.info("----------------SciHandler-------start-------------");
        long t1 = System.currentTimeMillis();
        String name = request.param("name");
        long cost = System.currentTimeMillis() - t1;
        SocketAddress socketAddress = request.getRemoteAddress();
        log.info(String.format("----------------socketAddress--%s-----------"),socketAddress);
        log.info("----------------SciHandler-------start-------------");
        if ("".equals("")){
           return (channel) -> {
            final BytesRestResponse response = new BytesRestResponse(RestStatus.UNAUTHORIZED, "Not authorized");
            response.addHeader("WWW-Authenticate", "Basic realm=\"ES\"");
            channel.sendResponse(response);
            //   new XPackClient(nodeClient).security().putRole(putRoleRequest,new RestToXContentListener(channel));
           //       new XPackClient(nodeClient).security().putUser(putRoleRequest,new RestToXContentListener(channel));
           };
        }else {
            return (channel) -> {
                handleRequest(request, channel, client);
            };
        }
    }
}
