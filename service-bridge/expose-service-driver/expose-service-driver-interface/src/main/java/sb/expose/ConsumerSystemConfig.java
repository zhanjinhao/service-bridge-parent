package sb.expose;

import java.util.Objects;

public class ConsumerSystemConfig {

    private String registryIp;
    private String registryPort;
    private String registryType;
    private String msgProtocol;
    private String loadBalance;
    private String serverPort;
    private String others;
    private String scsid;
    private String exposeServiceDriverId;

    public String getScsid() {
        return scsid;
    }

    public void setScsid(String scsid) {
        this.scsid = scsid;
    }

    public String getExposeServiceDriverId() {
        return exposeServiceDriverId;
    }

    public void setExposeServiceDriverId(String exposeServiceDriverId) {
        this.exposeServiceDriverId = exposeServiceDriverId;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getLoadBalance() {
        return loadBalance;
    }

    public void setLoadBalance(String loadBalance) {
        this.loadBalance = loadBalance;
    }

    public String getRegistryIp() {
        return registryIp;
    }

    public void setRegistryIp(String registryIp) {
        this.registryIp = registryIp;
    }

    public String getRegistryPort() {
        return registryPort;
    }

    public void setRegistryPort(String registryPort) {
        this.registryPort = registryPort;
    }

    public String getRegistryType() {
        return registryType;
    }

    public void setRegistryType(String registryType) {
        this.registryType = registryType;
    }

    public String getMsgProtocol() {
        return msgProtocol;
    }

    public void setMsgProtocol(String msgProtocol) {
        this.msgProtocol = msgProtocol;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    @Override
    public String toString() {
        return "ExposeRegistryConfig{" +
                "registryIp='" + registryIp + '\'' +
                ", registryPort='" + registryPort + '\'' +
                ", registryType='" + registryType + '\'' +
                ", msgProtocol='" + msgProtocol + '\'' +
                ", loadBalance='" + loadBalance + '\'' +
                ", serverPort='" + serverPort + '\'' +
                ", others='" + others + '\'' +
                ", exposeServiceDriverId='" + exposeServiceDriverId + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsumerSystemConfig that = (ConsumerSystemConfig) o;
        return Objects.equals(registryIp, that.registryIp) &&
                Objects.equals(registryPort, that.registryPort) &&
                Objects.equals(registryType, that.registryType) &&
                Objects.equals(msgProtocol, that.msgProtocol) &&
                Objects.equals(loadBalance, that.loadBalance) &&
                Objects.equals(serverPort, that.serverPort) &&
                Objects.equals(others, that.others) &&
                Objects.equals(exposeServiceDriverId, that.exposeServiceDriverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registryIp, registryPort, registryType, msgProtocol, loadBalance, serverPort, others, exposeServiceDriverId);
    }
}