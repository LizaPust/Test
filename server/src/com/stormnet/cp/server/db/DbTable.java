package com.stormnet.cp.server.db;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Objects;

public class DbTable {

    private  String tableName;

    private  String xmltableFilePath;

    private  Integer maxId;

    private Element maxIdTag;

    private Document dbTableDocument;

    public DbTable(String tableName, String xmltableFilePath, Integer maxId, Element maxIdTag, Document dbTableDocument) {
        this.tableName = tableName;
        this.xmltableFilePath = xmltableFilePath;
        this.maxId = maxId;
        this.maxIdTag = maxIdTag;
        this.dbTableDocument = dbTableDocument;
    }

    public String getTableName() {
        return tableName;
    }

    public String getxmltableFilePath() {
        return xmltableFilePath;
    }

    public Integer getMaxId() {

        maxId++;
        return maxId;
    }

    public Element getMaxIdTag() {
        return maxIdTag;
    }

    public Document getDbTableDocument() {
        return dbTableDocument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DbTable dbTable = (DbTable) o;
        return tableName.equals(dbTable.tableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName);
    }
}
