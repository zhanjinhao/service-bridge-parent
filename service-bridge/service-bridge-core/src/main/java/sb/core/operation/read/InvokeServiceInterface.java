package sb.core.operation.read;

import javax.servlet.http.HttpServletRequest;

public interface InvokeServiceInterface {

    Object invoke(String spsid, String serviceName, String version, String fullMethodName, HttpServletRequest request) throws Exception;


}
