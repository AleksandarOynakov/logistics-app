package com.austria.logistics.commands.showCommands;

import com.austria.logistics.commands.assignCommands.AssignLocation;
import com.austria.logistics.commands.assignCommands.AssignTruck;
import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.commands.creationCommands.CreateRoute;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.NotLoggedInException;
import com.austria.logistics.models.UserImpl;
import com.austria.logistics.models.enums.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


class ShowRoutesTest {
    private Repository repository;
    private Command createRoute;
    private Command assignLocation;
    private Command showRoutes;
    private Command assignTruck;

    @BeforeEach
    void setUp(){
        repository = new RepositoryImpl();
        createRoute = new CreateRoute(repository);
        assignLocation = new AssignLocation(repository);
        showRoutes = new ShowRoutes(repository);
        assignTruck = new AssignTruck(repository);
        repository.login(new UserImpl("Test","Test","Test","Test","Test@test.bg", UserRole.EMPLOYEE));
    }

    @Test
    void executeCommand_Should_Throw_Error_When_Not_LoggedIn() {
        //Arrange
        repository.logout();
        //Act,Assert
        Assertions.assertThrows(NotLoggedInException.class, () -> showRoutes.execute(List.of()));
    }

    @Test
    void executeCommand_Should_Throw_Error_When_Not_LoggedIn_As_Employee_Or_Manager() {
        //Arrange
        repository.logout();
        repository.login(new UserImpl("Test1","Test","Test","Test","Test1@test.bg", UserRole.CUSTOMER));
        //Act,Assert
        Assertions.assertThrows(NotLoggedInException.class, () -> showRoutes.execute(List.of()));
    }

    @Test
    void executeCommand_Should_Return_Info_Message_When_NoRoutes() {
        //Act,Assert
        Assertions.assertEquals("No routes are created yet!", showRoutes.execute(List.of()));
    }

    @Test
    void executeCommand_Should_Return_Routes() {
        //Arrange
        createRoute.execute(List.of());
        assignLocation.execute(List.of("1","Sydney","Feb","20","13:00"));
        assignLocation.execute(List.of("1","Darwin"));
        createRoute.execute(List.of());
        assignLocation.execute(List.of("2","Darwin","Feb","25","17:00"));
        createRoute.execute(List.of());
        assignLocation.execute(List.of("3","Darwin","Feb","22","16:00"));
        assignLocation.execute(List.of("3","Sydney"));
        assignTruck.execute(List.of("3","Man"));

        String excepted = "Current schedule for route with id 1:\n" +
                "No assigned truck to the route.\n" +
                "City: Sydney, Scheduled time: Feb 20 13:00\n" +
                "City: Darwin, Scheduled time: Feb 22 10:14\n" +
                "\n" +
                "Current schedule for route with id 2:\n" +
                "No assigned truck to the route.\n" +
                "City: Darwin, Scheduled time: Feb 25 17:00\n" +
                "\n" +
                "Current schedule for route with id 3:\n" +
                "The route has assigned truck Man with id 1011.\n" +
                "City: Darwin, Scheduled time: Feb 22 16:00\n" +
                "City: Sydney, Scheduled time: Feb 24 13:14\n" +
                "\n";

        //Act,Assert
        Assertions.assertEquals(excepted.replace("\r\n", "\n").replace("\r", "\n").trim(),
                showRoutes.execute(List.of()).replace("\r\n", "\n").replace("\r", "\n").trim());
    }

}