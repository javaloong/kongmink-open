package org.javaloong.kongmink.open.rest.auth.policy.internal;

import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageContentsList;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.service.invoker.FactoryInvoker;
import org.apache.cxf.service.invoker.Invoker;

import java.lang.reflect.Method;
import java.util.List;

public abstract class AbstractPolicyAuthorizationInterceptor extends AbstractPhaseInterceptor<Message> {

    private Object serviceObject;

    public AbstractPolicyAuthorizationInterceptor(String phase) {
        super(phase);
    }

    public void setServiceObject(Object object) {
        this.serviceObject = object;
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        final Object theServiceObject = getServiceObject(message);
        if (theServiceObject == null) {
            return;
        }

        final Method method = getServiceMethod(message);
        if (method == null) {
            return;
        }

        final List<Object> arguments = MessageContentsList.getContentsList(message);

        handlePolicy(message, theServiceObject, method, arguments);
    }

    protected Object getServiceObject(Message message) {
        if (serviceObject != null) {
            return serviceObject;
        }
        Object current = message.getExchange().get(Message.SERVICE_OBJECT);
        if (current != null) {
            return current;
        }
        Endpoint e = message.getExchange().getEndpoint();
        if (e != null && e.getService() != null) {
            Invoker invoker = e.getService().getInvoker();
            if (invoker instanceof FactoryInvoker) {
                FactoryInvoker factoryInvoker = (FactoryInvoker) invoker;
                if (factoryInvoker.isSingletonFactory()) {
                    return factoryInvoker.getServiceObject(message.getExchange());
                }
            }
        }
        return null;
    }

    protected Method getServiceMethod(Message message) {
        Message inMessage = message.getExchange().getInMessage();
        Method method = null;
        if (inMessage != null) {
            method = MessageUtils.getTargetMethod(inMessage).orElse(null);
        }
        if (method == null) {
            method = message.getExchange().get(Method.class);
        }
        return method;
    }

    protected abstract void handlePolicy(Message message, Object resourceInstance,
                                             Method method, List<Object> arguments);
}
