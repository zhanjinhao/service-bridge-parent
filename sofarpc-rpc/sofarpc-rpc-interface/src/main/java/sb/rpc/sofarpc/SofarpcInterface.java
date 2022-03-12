package sb.rpc.sofarpc;

public interface SofarpcInterface {

    String getHello();

    String getHelloByName(String name);

    String getHelloByNames(String... names);

    ResellOrderDetail getResellOrderDetail(String id);

}
