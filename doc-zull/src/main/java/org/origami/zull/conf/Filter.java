package org.origami.zull.conf;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Filter extends ZuulFilter  {

    private static Logger LOG = LoggerFactory.getLogger(Filter.class);
    @Override
    public String filterType() {
        return "pre";
    }
    @Override
    public int filterOrder() {
        return 1;
    }
    @Override
    public boolean shouldFilter() {
        return true;
    }


    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        ctx.addZuulRequestHeader("Authorization", request.getHeader("Authorization"));

        LOG.info("Parametres : {}", request.getParameterMap()
                .entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + Stream.of(e.getValue()).collect(Collectors.toList()))
                .collect(Collectors.toList()));
        LOG.info("Headers : {}", "Authorization" + "=" + request.getHeader("Authorization"));
        LOG.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
        return null;
    }
}