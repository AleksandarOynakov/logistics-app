package com.austria.logistics.commands.deletionCommands;

import com.austria.logistics.commands.assignCommands.AssignLocation;
import com.austria.logistics.commands.assignCommands.AssignPackage;
import com.austria.logistics.commands.assignCommands.AssignTruck;
import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.commands.creationCommands.CreatePackage;
import com.austria.logistics.commands.creationCommands.CreateRoute;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.CannotDeleteException;
import com.austria.logistics.exceptions.ElementNotFoundException;
import com.austria.logistics.exceptions.NotLoggedInException;
import com.austria.logistics.models.UserImpl;
import com.austria.logistics.models.enums.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeletePackageTest {
    private Repository repository;
    private Command createPackage;
    private Command deletePackage;
    private Command createRoute;
    private Command assignTruck;
    private Command assignLocation;
    private Command assignPackage;

    @BeforeEach
    void setUp() {
        repository = new RepositoryImpl();
        repository.login(new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.EMPLOYEE));
        createPackage = new CreatePackage(repository);
        deletePackage = new DeletePackage(repository);
        createRoute = new CreateRoute(repository);
        assignTruck = new AssignTruck(repository);
        assignLocation = new AssignLocation(repository);
        assignPackage = new AssignPackage(repository);
    }

    @Test
    void execute_Should_Throw_Error_When_User_Not_LoggedIn() {
        //Arrange
        repository.logout();
        //Act,Assert
        Assertions.assertThrows(NotLoggedInException.class, () -> deletePackage.execute(List.of()));
    }

    @Test
    void executeCommand_Should_Throw_Error_When_Not_LoggedIn_As_Employee_Or_Manager() {
        //Arrange
        repository.logout();
        repository.login(new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.CUSTOMER));
        //Act,Assert
        Assertions.assertThrows(NotLoggedInException.class, () -> deletePackage.execute(List.of()));
    }

    @Test
    void executeCommand_Should_Throw_Error_When_Package_NotFound(){
        //Act,Assert
        Assertions.assertThrows(ElementNotFoundException.class, () -> deletePackage.execute(List.of("99")));
    }

    @Test
    void executeCommand_Should_Throw_Error_When_Package_Assigned(){
        createRoute.execute(List.of());
        assignLocation.execute(List.of("1", "Sydney", "Feb","20", "13:00"));
        assignLocation.execute(List.of("1", "Darwin"));
        assignTruck.execute(List.of("1", "Man"));
        createPackage.execute(List.of("Sydney","Darwin","30","test@test.bg"));
        assignPackage.execute(List.of("2","1011"));
        //Act,Assert
        Assertions.assertThrows(CannotDeleteException.class, () -> deletePackage.execute(List.of("2")));
    }

    @Test
    void executeCommand_Should_Delete_Package(){
        //Arrange
        createPackage.execute(List.of("Sydney","Darwin","30","test@test.bg"));
        //Act
        String result = deletePackage.execute(List.of("1"));
        //Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals(0,repository.getRoutes().size()),
                () -> Assertions.assertEquals("Package with id 1 was successfully deleted!", result)
        );
    }


}