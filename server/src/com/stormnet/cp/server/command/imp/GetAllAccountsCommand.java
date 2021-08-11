package com.stormnet.cp.server.command.imp;

import com.stormnet.cp.data.Account;
import com.stormnet.cp.server.command.FirstCommand;
import com.stormnet.cp.server.idb.xml.AccountDao;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.util.List;

public class GetAllAccountsCommand implements FirstCommand {


    @Override
    public void process(JSONObject requestData, JSONWriter jsonWriter) {

        AccountDao accountDao = new AccountDao();

        List <Account> allAccounts;
        try {
           allAccounts =  accountDao.loadAllAccounts();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        jsonWriter.key("response-data").array();

        for (Account account :allAccounts) {

            jsonWriter.object();
            jsonWriter.key("login").value(account.getLogin());
            jsonWriter.key("password").value(account.getPassword());
            jsonWriter.key("enabled").value(account.isEnabled());
            jsonWriter.key("birthday").value(account.getBirthday());
            jsonWriter.key("country").value(account.getCountry());
            jsonWriter.endObject();

        }

        jsonWriter.endArray();

    }
}
