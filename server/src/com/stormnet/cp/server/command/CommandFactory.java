package com.stormnet.cp.server.command;

import com.stormnet.cp.server.command.imp.*;

import java.util.HashMap;
import java.util.Map;

public class CommandFactory {

    private static final Map<String, FirstCommand> allComands;

    static{
        allComands = new HashMap<>();

        allComands.put("get-all-accounts",new GetAllAccountsCommand());
        allComands.put("save-account",new SaveAccountCommand());
        allComands.put("edit-account",new EditAccountCommand());
        allComands.put("delete-account",new DeleteAccountCommand());
        allComands.put("get-all-countries",new GetAllCountriesCommand());
    }

    public static FirstCommand getCommand(String commandName){
    FirstCommand serverCommand = allComands.get(commandName);
    if (serverCommand == null){
        return new NotFoundCommand();
    }
    return serverCommand;
    }
}
