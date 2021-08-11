package com.stormnet.cp.server.idb.xml;

import com.stormnet.cp.data.Account;
import com.stormnet.cp.server.db.DataBase;
import com.stormnet.cp.server.db.DbTable;
import com.stormnet.cp.server.db.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    public List<Account> loadAllAccounts(){

        DataBase dataBase = DataBase.getDataBase();
        DbTable accountsTable = dataBase.getDbTable("accounts");
        Document document = accountsTable.getDbTableDocument();

        Element rootElement = document.getDocumentElement();

        List<Account> allAccounts = new ArrayList<>();
        NodeList accountList = rootElement.getElementsByTagName("account");

        for(int i = 0; i < accountList.getLength(); i++) {

            Element accountTag = (Element) accountList.item(i);

            String login = DomUtils.getChildTagData(accountTag,"login");
            String password = DomUtils.getChildTagData(accountTag,"password");
            boolean enabled = DomUtils.getChildTagBooleanData(accountTag, "enabled");
            LocalDate birthday = DomUtils.getChildTagLocalDateData(accountTag,"birthday");
            String country = DomUtils.getChildTagData(accountTag,"country");

            Account account = new Account();
            account.setLogin(login);
            account.setPassword(password);
            account.setEnabled(enabled);
            account.setCountry(country);
            account.setBirthday(birthday);

            allAccounts.add(account);
        }
        return allAccounts;
    }

    public void saveAccount(Account account) throws Exception {

        DataBase dataBase = DataBase.getDataBase();
        DbTable accountsTable = dataBase.getDbTable("accounts");
        Document document = accountsTable.getDbTableDocument();

        Element rootElement = document.getDocumentElement();

        Element accountTag = document.createElement("account");
        rootElement.appendChild(accountTag);

        String idStr = dataBase.getNextId("accounts").toString();

        DomUtils.appendTagToParentTag(document, "id", idStr, accountTag);
        DomUtils.appendTagToParentTag(document, "login", account.getLogin(), accountTag);
        DomUtils.appendTagToParentTag(document, "password", account.getPassword(), accountTag);
        String enableStr = Boolean.toString(account.isEnabled());
        DomUtils.appendTagToParentTag(document, "enabled", enableStr, accountTag);
        String birthdayStr = account.getBirthday().toString();
        DomUtils.appendTagToParentTag(document, "birthday", birthdayStr, accountTag);
        DomUtils.appendTagToParentTag(document, "country", account.getCountry(), accountTag);

        DomUtils.saveDocument(document, accountsTable.getxmltableFilePath());
        
    }

}