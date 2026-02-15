package com.austria.logistics.commands.showCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.NotLoggedInException;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.models.vehicles.contracts.Truck;

import java.util.List;

public class ShowTrucks extends BaseCommand {
    private final Repository repo = getRepository();

    public ShowTrucks(Repository repository) {
        super(repository);
    }

    //NO ARGUMENTS ARE EXPECTED
    @Override
    public String executeCommand(List<String> parameters) {
        User loggedUser = repo.getLoggedUser();

        if(loggedUser.getUserRole() != UserRole.MANAGER && loggedUser.getUserRole() != UserRole.EMPLOYEE){
            throw new NotLoggedInException(Constants.USER_NOT_MANAGER_AND_NOT_EMPLOYEE);
        }

        return showTrucks();
    }

    private String showTrucks() {
        StringBuilder output = new StringBuilder();
        List<Truck> trucks = repo.getTrucks();

        trucks.forEach(truck -> output.append(truck.toString()));

        return output.toString();
    }
    @Override
    protected boolean requiresLogin() {
        return true;
    }

}
