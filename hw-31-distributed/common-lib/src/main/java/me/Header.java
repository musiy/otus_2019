package me;

public class Header {

    public static final String PROTOCOL_NAME = "MS-BROKER";

    public static final String VERSION = "1";

    private String protocolName;

    private String protocolVersion;

    private Address address;

    public Header(String protocolName, String protocolVersion, Address address) {
        this.protocolName = protocolName;
        this.protocolVersion = protocolVersion;
        this.address = address;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public Address getAddress() {
        return address;
    }
}
