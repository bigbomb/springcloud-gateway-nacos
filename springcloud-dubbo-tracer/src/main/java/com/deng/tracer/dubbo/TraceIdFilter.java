package com.deng.tracer.dubbo;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * @author bigbomb
 * @version 1.0
 * @date 2020/2/20 14:35
 * @className TraceIdFilter
 * @desc TraceId Dubbo过滤器，用于在服务之间传递TraceId
 * 根据服务端/消费端选择具体的TraceId传递策略
 */
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER}, value = "tracing")
@Slf4j
public class TraceIdFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TraceIdFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext rpcContext = RpcContext.getContext();
        String traceId = RpcContext.getContext().getAttachment(TraceIdConst.TRACE_ID);
        if (StringUtils.isBlank(traceId) ) {
            if (rpcContext.isConsumerSide()) {
                RpcContext.getContext().setAttachment(TraceIdConst.TRACE_ID, MDC.get(TraceIdConst.TRACE_ID));
//            MDC.put(TraceIdConst.TRACE_ID, traceId);
                LOGGER.debug("[TraceIdFilter-consumerSide] set TraceId to TraceIdUtil and RpcContext,TraceId={}", traceId);
            }

        }else
        {
            if (rpcContext.isProviderSide()) {
                // 服务提供方，从Rpc上下文获取traceId
                traceId = RpcContext.getContext().getAttachment(TraceIdConst.TRACE_ID);
                MDC.put(TraceIdConst.TRACE_ID, traceId);
                LOGGER.debug("[TraceIdFilter-providerSide] get TraceId from RpcContext and set it to thr trace Context,TraceId={}", traceId);
            }
        }

        Result result = invoker.invoke(invocation);
        // after
        if (rpcContext.isProviderSide()) {
            // clear traceId from MDC
            MDC.remove(TraceIdConst.TRACE_ID);
        }

        return result;
    }
}
