package sb.expose.rest;

import sb.expose.ConsumerSystemConfig;
import sb.expose.ExposeServiceDriver;

public class ExposeServiceDriverRest implements ExposeServiceDriver {

    @Override
    public Object buildSkeleton(Class interfaceClazz, Object stub, String serviceName, String version, ConsumerSystemConfig consumerSystemConfig, Object... others) {

        storeMethods(interfaceClazz, stub, serviceName, version, consumerSystemConfig, others);

        return true;
    }

    @Override
    public Boolean removeSkeleton(Class interfaceClazz, Object service, String serviceName, String version, ConsumerSystemConfig consumerSystemConfig, Object... others) {

        removeMethods(interfaceClazz, service, serviceName, version, consumerSystemConfig, others);

        return null;
    }

    @Override
    public String getExposeServiceDriverId() {
        return "expose-service-driver-rest-self";
    }

    @Override
    public Boolean checkRegistryAlive(ConsumerSystemConfig consumerSystemConfig) {
        return null;
    }
}
