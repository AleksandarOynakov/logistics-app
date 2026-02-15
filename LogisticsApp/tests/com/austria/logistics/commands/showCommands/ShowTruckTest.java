package com.austria.logistics.commands.showCommands;

import com.austria.logistics.commands.assignCommands.AssignTruck;
import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.commands.creationCommands.CreateRoute;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.NotLoggedInException;
import com.austria.logistics.models.UserImpl;
import com.austria.logistics.models.contracts.Route;
import com.austria.logistics.models.enums.CityName;
import com.austria.logistics.models.enums.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;


class ShowTruckTest {
    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026, 1, 24, 12, 0);
    private Repository repository;
    private Command showTruck;

    @BeforeEach
    void setUp() {
        repository = new RepositoryImpl();
        repository.login(new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.EMPLOYEE));
        Command createRoute = new CreateRoute(repository);
        createRoute.execute(List.of());

        Route route = repository.getRoutes().get(0);
        route.addFirstLocationToRoute(CityName.BRI, FIXED_TIME);
        route.addLocationToRoute(CityName.ADL);
        route.addLocationToRoute(CityName.BRI);

        Command assignTruck = new AssignTruck(repository);
        assignTruck.execute(List.of(String.valueOf(route.getId()), "Man"));

        showTruck = new ShowTruck(repository);
    }

    @Test
    void executeCommand_Should_Throw_Error_When_Not_LoggedIn() {
        //Arrange
        repository.logout();
        //Act,Assert
        Assertions.assertThrows(NotLoggedInException.class, () -> showTruck.execute(List.of()));
    }

    @Test
    void executeCommand_Should_Throw_Error_When_Not_LoggedIn_As_Employee_Or_Manager() {
        //Arrange
        repository.logout();
        repository.login(new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.CUSTOMER));
        //Act,Assert
        Assertions.assertThrows(NotLoggedInException.class, () -> showTruck.execute(List.of()));
    }

    @Test
    void executeCommand_Should_Return_Truck() {
        //Act,Assert
        Assertions.assertEquals("Man with id 1011 is assigned to route with id 1, current weight is 0 kg and max capacity is 37000 kg\n", showTruck.execute(List.of("1011")));
    }

}