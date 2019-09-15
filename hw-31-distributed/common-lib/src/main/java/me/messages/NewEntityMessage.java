package me.messages;

import me.Address;
import me.Message;

import java.util.Map;

public class NewEntityMessage extends Message {

    private Map<String, String> props;

    private String entityName;

    public NewEntityMessage(Address from, Address to, Map<String, String> props, String entityName) {
        super(from, to);
        this.props = props;
        this.entityName = entityName;
    }

    public Map<String, String> getProps() {
        return props;
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
