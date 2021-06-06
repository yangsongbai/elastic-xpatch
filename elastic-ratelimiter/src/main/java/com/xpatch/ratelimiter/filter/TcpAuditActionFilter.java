package com.xpatch.ratelimiter.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.support.ActionFilter;
import org.elasticsearch.action.support.ActionFilterChain;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.tasks.Task;

import java.io.IOException;

/**
 * @ClassName TcpAuditActionFilter
 * @Description 请描述类的业务用途
 * @Author yangsongbai
 * @Date 2021/5/23 下午1:52
 * @email yangsongbaivat@163.com
 * @Version 1.0
 **/
public class TcpAuditActionFilter implements ActionFilter {
    protected final Logger log = LogManager.getLogger(com.xpatch.ratelimiter.filter.TcpAuditActionFilter.class);
    @Override
    public int order() {
        return Integer.MAX_VALUE;
    }

    @Override
    public <Request extends ActionRequest, Response extends ActionResponse> void apply(
            Task task,
            String action,
            Request request,
            ActionListener<Response> actionListener,
            ActionFilterChain<Request, Response> actionFilterChain) {
        ActionListener<Response> myActionListener = new AnotherActionListener(actionListener,
                request, System.currentTimeMillis());
        //进行过滤相关操作
        //
        if (request==null) log.info(String.format("request = %s",request));
        else {
            TransportAddress transportAddress = request.remoteAddress();
            if (transportAddress==null) ;
            else{
                String addr = transportAddress.getAddress();
                int port = transportAddress.getPort();
                log.info("action = {}",action);
                log.info(String.format("transportAddress = %s;transportAddress = %s ",addr,port));
                log.info(request.getDescription());
                log.info(request.getParentTask().getNodeId());
                log.info(request.getParentTask().getId());
            }
        }
        actionFilterChain.proceed(task, action, request, myActionListener);

    }

    class AnotherActionListener<Response extends ActionResponse, Request extends ActionRequest> implements ActionListener<Response> {
        private ActionListener<Response> actionListener;
        private Request request;
        private long startTime;

        public AnotherActionListener(ActionListener<Response> actionListener, Request request, long startTime) {
            this.actionListener = actionListener;
            this.request = request;
            this.startTime = startTime;
        }

        public void onResponse(Response response) {
            //对es的响应进行分类和更改，部分response不支持构造函数，需要看源码查找构造方法
            if (response instanceof GetIndexResponse) {
                GetIndexResponse temp= null;
                try {
                    temp = GetIndexResponse.fromXContent(null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                actionListener.onResponse((Response)temp);
            } {
                actionListener.onResponse(response);
            }
        }

        public void onFailure(Exception e) {
            actionListener.onFailure(e);
        }

    }
}
