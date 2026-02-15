package com.austria.logistics.commands.deletionCommands;

import com.austria.logistics.commands.contracts.Command;
import com.austria.logistics.commands.creationCommands.CreateRoute;
import com.austria.logistics.core.RepositoryImpl;
import com.austria.logistics.core.contracts.Repository;
import com.austria.logistics.exceptions.ElementNotFoundException;
import com.austria.logistics.exceptions.NotLoggedInException;
import com.austria.logistics.models.UserImpl;
import com.austria.logistics.models.enums.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class DeleteRouteTest {
    Repository repository;
    Command createRoute;
    Command deleteRoute;
    @BeforeEach
    void setUp() {
        repository = new RepositoryImpl();
        repository.login(new UserImpl("Test","Test","Test","Test", "test@test.bg", UserRole.EMPLOYEE));
        createRoute = new CreateRoute(repository);
        deleteRoute = new DeleteRoute(repository);
    }

    @Test
    void executeCommand_Should_Throw_Error_When_Not_LoggedIn() {
        //Arrange
        repository.logout();
        //Act,Assert
        Assertions.assertThrows(NotLoggedInException.class, () -> deleteRoute.execute(List.of()));
    }

    @Test
    void executeCommand_Should_Throw_Error_When_Not_LoggedIn_As_Employee_Or_Manager() {
        //Arrange
        repository.logout();
        repository.login(new UserImpl("Test", "Test", "Test", "Test", "test@test.bg", UserRole.CUSTOMER));
        //Act,Assert
        Assertions.assertThrows(NotLoggedInException.class, () -> deleteRoute.execute(List.of()));
    }

    @Test
    void executeCommand_Should_Throw_Error_When_RouteId_NotFound() {
        //Act,Assert
        Assertions.assertThrows(ElementNotFoundException.class, () -> deleteRoute.execute(List.of("1")));
    }

    @Test
    void executeCommand_Should_Delete_Route() {
        //Arrange
        createRoute.execute(List.of());
        createRoute.execute(List.of());
        //Act
       String result = deleteRoute.execute(List.of("2"));
        // Assert
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, repository.getRoutes().size()),
                () -> Assertions.assertEquals(1, repository.getRoutes().get(0).getId()),
                () -> Assertions.assertEquals("Route with id 2 was successfully deleted!", result)
        );
    }

}