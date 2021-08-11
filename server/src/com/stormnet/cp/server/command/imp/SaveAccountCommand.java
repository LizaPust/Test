package com.stormnet.cp.server.command.imp;

import com.stormnet.cp.data.Account;
import com.stormnet.cp.server.command.FirstCommand;
import com.stormnet.cp.server.idb.xml.AccountDao;
import org.json.JSONObject;
import org.json.JSONWriter;

import java.time.LocalDate;

public class SaveAccountCommand implements FirstCommand {
    @Override
    public void process(JSONObject requestData, JSONWriter jsonWriter) {

        String login = requestData.getString("login");
        String password = requestData.getString("password");
        boolean enabled = requestData.getBoolean("enabled");

        String birthdayStr = requestData.getString("birthday");
        LocalDate birthday = LocalDate.parse(birthdayStr);

        String country = requestData.getString("country");

        Account account = new Account();
        account.setLogin(login);
        account.setPassword(password);
        account.setEnabled(enabled);
        account.setCountry(country);
        account.setBirthday(birthday);

        AccountDao accountDao = new AccountDao();
        try {
            accountDao.saveAccount(account);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
