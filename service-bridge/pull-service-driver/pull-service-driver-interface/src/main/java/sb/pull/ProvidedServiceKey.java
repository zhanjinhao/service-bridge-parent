package sb.pull;

public class ProvidedServiceKey {

    private String name;

    private String version;

    public ProvidedServiceKey(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public ProvidedServiceKey() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ProvidedServiceKey{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
