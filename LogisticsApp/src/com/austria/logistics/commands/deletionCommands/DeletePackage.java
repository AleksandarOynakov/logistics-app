package com.austria.logistics.commands.deletionCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.NotLoggedInException;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.utils.Parsers;

import java.util.List;

public class DeletePackage extends BaseCommand {
    private final Repository repo = getRepository();

    public DeletePackage(Repository repository) {
        super(repository);
    }

        //EXPECTS STRING Package ID
    @Override
    protected String executeCommand(List<String> parameters) {
        User loggedUser = repo.getLoggedUser();

        if (loggedUser.getUserRole() != UserRole.MANAGER && loggedUser.getUserRole() != UserRole.EMPLOYEE) {
            throw new NotLoggedInException(Constants.USER_NOT_MANAGER_AND_NOT_EMPLOYEE);
        }

        int pkgId = Parsers.parseToInteger("Package id", parameters.get(0));

        return deletePackage(pkgId);
    }

    private String deletePackage(int pkgId){
        repo.deletePackage(pkgId);
        return String.format(Constants.PACKAGE_SUCCESSFULLY_DELETED_MESSAGE,pkgId);
    }

    @Override
    protected boolean requiresLogin() {
        return true;
    }
}
