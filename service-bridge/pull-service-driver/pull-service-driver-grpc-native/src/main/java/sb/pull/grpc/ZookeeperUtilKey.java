package sb.pull.grpc;

public class ZookeeperUtilKey {

    private String namespace;
    private String connectUrl;

    public ZookeeperUtilKey(String namespace, String connectUrl) {
        this.namespace = namespace;
        this.connectUrl = connectUrl;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getConnectUrl() {
        return connectUrl;
    }

    public void setConnectUrl(String connectUrl) {
        this.connectUrl = connectUrl;
    }
}
