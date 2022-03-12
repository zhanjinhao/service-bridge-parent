package sb.pull;

import java.util.Objects;

public class ProviderSystemConfig {

    private String registryIp;
    private String registryPort;
    private String registryType;
    private String msgProtocol;
    private String loadBalance;
    private String pullServiceDriverId;
    private String others;
    private String spsid;

    public String getSpsid() {
        return spsid;
    }

    public void setSpsid(String spsid) {
        this.spsid = spsid;
    }

    public String getPullServiceDriverId() {
        return pullServiceDriverId;
    }

    public void setPullServiceDriverId(String pullServiceDriverId) {
        this.pullServiceDriverId = pullServiceDriverId;
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
        return "PullRegistryConfig{" +
                "registryIp='" + registryIp + '\'' +
                ", registryPort='" + registryPort + '\'' +
                ", registryType='" + registryType + '\'' +
                ", msgProtocol='" + msgProtocol + '\'' +
                ", loadBalance='" + loadBalance + '\'' +
                ", others='" + others + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProviderSystemConfig that = (ProviderSystemConfig) o;
        return Objects.equals(registryIp, that.registryIp) &&
                Objects.equals(registryPort, that.registryPort) &&
                Objects.equals(registryType, that.registryType) &&
                Objects.equals(msgProtocol, that.msgProtocol) &&
                Objects.equals(loadBalance, that.loadBalance) &&
                Objects.equals(pullServiceDriverId, that.pullServiceDriverId) &&
                Objects.equals(others, that.others);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registryIp, registryPort, registryType, msgProtocol, loadBalance, pullServiceDriverId, others);
    }
}