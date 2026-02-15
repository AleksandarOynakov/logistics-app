package com.austria.logistics.commands.showCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.NotLoggedInException;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.UserRole;

import java.util.List;

public class ShowRoutes extends BaseCommand {
    public ShowRoutes(Repository repository) {
        super(repository);
    }

    //NO ARGUMENTS ARE EXPECTED
    @Override
    protected String executeCommand(List<String> parameters) {
        User loggedUser = getRepository().getLoggedUser();

        if (loggedUser.getUserRole() != UserRole.MANAGER && loggedUser.getUserRole() != UserRole.EMPLOYEE) {
            throw new NotLoggedInException(Constants.USER_NOT_MANAGER_AND_NOT_EMPLOYEE);
        }

        return showRoutes();
    }

    private String showRoutes() {
        Repository repo = getRepository();
        StringBuilder output = new StringBuilder();
        repo.getRoutes().forEach(route -> output.append(route.toString()).append(System.lineSeparator()));
        if (repo.getRoutes().isEmpty()) {
            output.append(Constants.ROUTE_NOT_CREATED_YET_MESSAGE);
        }
        return output.toString();
    }

    @Override
    protected boolean requiresLogin() {
        return true;
    }

}
