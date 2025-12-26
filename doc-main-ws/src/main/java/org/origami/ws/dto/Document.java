package org.origami.ws.dto;

public class Document {

    private String name;
    private String type;
    private Boolean enable;

    public Document() {
    }

    public Document(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "Document{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", enable=" + enable +
                '}';
    }
}
