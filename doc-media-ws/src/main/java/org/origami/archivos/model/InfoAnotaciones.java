/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.origami.archivos.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author eduar
 */
public class InfoAnotaciones implements Serializable {

    private Integer page;
    private String type;
    private String author;
    private String content;
    private Date creationDate;
    private Date modifiedDate;

    public InfoAnotaciones() {
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("InfoAnotaciones{");
        sb.append("page=").append(page);
        sb.append(", type=").append(type);
        sb.append(", author=").append(author);
        sb.append(", content=").append(content);
        sb.append(", creationDate=").append(creationDate);
        sb.append(", modifiedDate=").append(modifiedDate);
        sb.append('}');
        return sb.toString();
    }

}
