package sb.core.operation.spi;

import sb.pull.ProviderSystemConfig;

public interface PSDExecutor { 
    Object buildStub(String interfaceClazz, String serviceName, String version, ProviderSystemConfig providerSystemConfig);
}