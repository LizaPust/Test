package com.stormnet.cp.server.command.imp;

import com.stormnet.cp.data.Account;
import com.stormnet.cp.server.command.FirstCommand;
import com.stormnet.cp.server.db.DataBase;
import com.stormnet.cp.server.db.DbTable;
import com.stormnet.cp.server.db.DomUtils;
import com.stormnet.cp.server.idb.xml.AccountDao;
import org.json.JSONObject;
import org.json.JSONWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DeleteAccountCommand implements FirstCommand {
    @Override
    public void process(JSONObject requestData, JSONWriter jsonWriter) throws TransformerException {

        String login1 = requestData.getString("login");

        DataBase dataBase = DataBase.getDataBase();
        DbTable accountsTable = dataBase.getDbTable("accounts");
        Document document = accountsTable.getDbTableDocument();

        Element rootElement = document.getDocumentElement();

        List<Account> allAccounts = new ArrayList<>();
        NodeList accountList = rootElement.getElementsByTagName("account");

        for(int i = 0; i < accountList.getLength(); i++) {

            Element accountTag = (Element) accountList.item(i);

            String login = DomUtils.getChildTagData(accountTag, "login");

            if (login1.equals(login)) {
                accountTag.getParentNode().removeChild(accountTag);
            }

            DomUtils.saveDocument(document, accountsTable.getxmltableFilePath());
        }
    }
}