package com.alizardo.remote;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class RemoteHelloClient implements RemoteHello {

    public InitialContext createContext() throws NamingException {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        props.put(Context.PROVIDER_URL, "remote+http://192.168.1.41:8080");
        props.put(Context.SECURITY_PRINCIPAL, "ejb");
        props.put(Context.SECURITY_CREDENTIALS, "ejb-123!");
        return new InitialContext(props);
    }

    // org.wildfly.naming.client.store.RelativeContext cannot be cast to class com.alizardo.remote.RemoteHello
    // java.lang.IllegalArgumentException: COM00008: Parameter 'moduleName' must not be empty
    public RemoteHello lookup(InitialContext context) throws NamingException {
        final String rcal = "ejb:/ejb-server/RemoteHello!com.alizardo.remote.RemoteHello";
        return (RemoteHello) context.lookup(rcal);
    }

    @Override
    public String sayHello() {
        try {
            InitialContext context = createContext();
            System.out.println("Context created");
            final RemoteHello remote = lookup(context);
            System.out.println("Lookup successful");
            final String result = remote.sayHello();
            System.out.println("sayHello succeed: " + result);
            return result;
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
