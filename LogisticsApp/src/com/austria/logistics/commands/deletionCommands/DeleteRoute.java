package com.austria.logistics.commands.deletionCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.NotLoggedInException;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.utils.Parsers;

import java.util.List;

public class DeleteRoute extends BaseCommand {
    private final Repository repo = getRepository();
    public DeleteRoute(Repository repository) {
        super(repository);
    }
    //EXPECTS STRING ROUTE ID
    @Override
    protected String executeCommand(List<String> parameters) {
        User loggedUser = repo.getLoggedUser();

        if (loggedUser.getUserRole() != UserRole.MANAGER && loggedUser.getUserRole() != UserRole.EMPLOYEE) {
            throw new NotLoggedInException(Constants.USER_NOT_MANAGER_AND_NOT_EMPLOYEE);
        }

        int routeId = Parsers.parseToInteger("Route id", parameters.get(0));

        return deleteRoute(routeId);
    }

    private String deleteRoute(int routeId){
        repo.deleteRoute(routeId);
        return String.format(Constants.ROUTE_SUCCESSFULLY_DELETED_MESSAGE,routeId);
    }

    @Override
    protected boolean requiresLogin() {
        return true;
    }

}
