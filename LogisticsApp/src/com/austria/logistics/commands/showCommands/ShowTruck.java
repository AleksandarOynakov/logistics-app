package com.austria.logistics.commands.showCommands;

import com.austria.logistics.commands.BaseCommand;
import com.austria.logistics.constants.Constants;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.NotLoggedInException;
import com.austria.logistics.models.contracts.User;
import com.austria.logistics.models.enums.UserRole;
import com.austria.logistics.models.vehicles.contracts.Truck;
import com.austria.logistics.utils.Parsers;
import com.austria.logistics.utils.Validators;

import java.util.List;

public class ShowTruck extends BaseCommand {
    private static final int EXPECTED_NUMBER_OF_ARGUMENTS = 1;
    private final Repository repo = getRepository();

    public ShowTruck(Repository repository) {
        super(repository);
    }

    //EXPECTED STRING TRUCK ID
    @Override
    protected String executeCommand(List<String> parameters) {
        User loggedUser = repo.getLoggedUser();

        if (loggedUser.getUserRole() != UserRole.MANAGER && loggedUser.getUserRole() != UserRole.EMPLOYEE) {
            throw new NotLoggedInException(Constants.USER_NOT_MANAGER_AND_NOT_EMPLOYEE);
        }
        Validators.validateArgumentsCount(parameters, EXPECTED_NUMBER_OF_ARGUMENTS);
        int truckId = Parsers.parseToInteger("Truck id", parameters.get(0));
        Truck truck = repo.findElementById(repo.getTrucks(),truckId);

        return showTruck(truck);
    }

    private String showTruck(Truck truck){
        return truck.toString();
    }

    @Override
    protected boolean requiresLogin() {
        return true;
    }

}
