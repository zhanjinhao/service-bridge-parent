package sb.client.pojo;

import java.util.Objects;

public class URLPojo {

    private String schema;

    private String domain;

    private String port;

    private String spsid;

    private String serviceName;

    private String version;

    private int connectTimeOut = 10000;

    private int readTimeOut = 10000;

    /**
     * @param schema
     * @param domain
     * @param port
     * @param spsid
     * @param serviceName
     * @param version
     */
    public URLPojo(String schema, String domain, String port, String spsid, String serviceName, String version) {
        this.schema = schema;
        this.domain = domain;
        this.port = port;
        this.spsid = spsid;
        this.serviceName = serviceName;
        this.version = version;
    }


    public URLPojo(String schema, String domain, String port, String spsid, String version) {
        this.schema = schema;
        this.domain = domain;
        this.port = port;
        this.spsid = spsid;
        this.version = version;
    }

    /**
     * @param schema
     * @param domain
     * @param port
     * @param spsid
     * @param serviceName
     * @param version
     * @param connectTimeOut
     * @param readTimeOut
     */
    public URLPojo(String schema, String domain, String port, String spsid, String serviceName, String version, int connectTimeOut, int readTimeOut) {
        this.schema = schema;
        this.domain = domain;
        this.port = port;
        this.spsid = spsid;
        this.serviceName = serviceName;
        this.version = version;
        this.connectTimeOut = connectTimeOut;
        this.readTimeOut = readTimeOut;
    }


    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSpsid() {
        return spsid;
    }

    public void setSpsid(String spsid) {
        this.spsid = spsid;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public String getFullPath() {
        return schema + "://" + domain + ":" + port + "/api/invoke/" + spsid + "/" + serviceName + "/" + version + "/";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        URLPojo urlPojo = (URLPojo) o;
        return connectTimeOut == urlPojo.connectTimeOut &&
                readTimeOut == urlPojo.readTimeOut &&
                Objects.equals(schema, urlPojo.schema) &&
                Objects.equals(domain, urlPojo.domain) &&
                Objects.equals(port, urlPojo.port) &&
                Objects.equals(spsid, urlPojo.spsid) &&
                Objects.equals(serviceName, urlPojo.serviceName) &&
                Objects.equals(version, urlPojo.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schema, domain, port, spsid, serviceName, version, connectTimeOut, readTimeOut);
    }
}
