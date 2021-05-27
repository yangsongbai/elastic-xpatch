package com.dirk.plugin;

import com.dirk.authorization.config.AuthConfig;
import com.dirk.authorization.handler.HttpAuthHandler;
import com.dirk.ratelimiter.filter.TcpAuditActionFilter;
import com.dirk.ratelimiter.module.EsModule;
import org.apache.lucene.util.SetOnce;
import org.elasticsearch.action.support.ActionFilter;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.common.io.stream.NamedWriteableRegistry;
import org.elasticsearch.common.settings.Setting;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.util.concurrent.ThreadContext;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.env.Environment;
import org.elasticsearch.env.NodeEnvironment;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.NetworkPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.rest.RestHandler;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.watcher.ResourceWatcherService;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * @ClassName ElasticXPatchPlugin
 * @Description 请描述类的业务用途
 * @Author yangsongbai
 * @Date 2021/5/27 下午11:15
 * @email yangsongbaivat@163.com
 * @Version 1.0
 **/
public class ElasticXPatchPlugin  extends Plugin implements ActionPlugin, NetworkPlugin {
    public SetOnce<TcpAuditActionFilter> TcpAuditActionFilter = new SetOnce();
    public Settings settings;
    public static Client client;
    private HttpAuthHandler httpAuthHandler;

    public ElasticXPatchPlugin(Settings settings, Path configPath){
        super();
        AuthConfig authConfig = new AuthConfig(new Environment(settings,configPath));
        this.settings = settings;
        httpAuthHandler = new HttpAuthHandler(authConfig);
    }

    @Override
    public List<ActionFilter> getActionFilters() {
        //接口实现类可以向上转型为接口; 添加拦截器
        List<ActionFilter> filters = new ArrayList();
        TcpAuditActionFilter.set(new TcpAuditActionFilter());
        filters.add(TcpAuditActionFilter.get());
        return filters;
    }

    @Override
    public Collection<Module> createGuiceModules() {
        //注入插件
        List<Module> modules = new ArrayList();
        modules.add(new EsModule());
        return  modules;
    }

    @Override
    public List<Setting<?>> getSettings() {
        //添加yml配置文件字段
        ArrayList<Setting<?>> settings = new ArrayList();
        settings.addAll(super.getSettings());
        //settings.add(Setting.simpleString("zk.ip",new Setting.Property[]{Setting.Property.NodeScope}));
        return settings;
    }

    @Override
    public Collection<Object> createComponents(Client client, ClusterService clusterService, ThreadPool threadPool,
                                               ResourceWatcherService resourceWatcherService, ScriptService scriptService,
                                               NamedXContentRegistry xContentRegistry, Environment environment, NodeEnvironment nodeEnvironment,
                                               NamedWriteableRegistry namedWriteableRegistry) {
        this.client = client;
        return super.createComponents(client, clusterService, threadPool, resourceWatcherService, scriptService, xContentRegistry, environment, nodeEnvironment, namedWriteableRegistry);
    }


  /*  @Override
    public List<RestHandler> getRestHandlers(Settings settings, RestController restController,
           ClusterSettings clusterSettings, IndexScopedSettings indexScopedSettings,
                                             SettingsFilter settingsFilter, IndexNameExpressionResolver indexNameExpressionResolver, Supplier<DiscoveryNodes> nodesInCluster) {
         return singletonList(new SciHandler(settings, restController,clusterSettings,indexScopedSettings));
    }*/

   /* @Override
    public List<TransportInterceptor> getTransportInterceptors(NamedWriteableRegistry namedWriteableRegistry, ThreadContext threadContext) {
        return singletonList();
    }*/

    @Override
    public UnaryOperator<RestHandler> getRestHandlerWrapper(ThreadContext threadContext) {
        return sourceHandler -> (RestHandler) (request, channel, client) -> {
            this.httpAuthHandler.authorize(sourceHandler, request, channel, client);
        };
    }
}
