package repositories;

import config.Database;
import entities.TodoList;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TodoListRepositoryDbImpl implements TodoListRepository {
    private final Database database;

    public TodoListRepositoryDbImpl(final Database database) {
        this.database = database;
    }

    @Override
    public TodoList[] getAll() {
        Connection connection = database.getConnection();
        String sqlStatement = "SELECT * FROM todos";
        List<TodoList> todoLists = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                TodoList todoList = new TodoList();
                Integer id = resultSet.getInt(1);
                String todo = resultSet.getString(2);
                todoList.setTodo(todo);
                todoList.setId(id);
                todoLists.add(todoList);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return todoLists.toArray(TodoList[]::new);
    }

    @Override
    public void add(final TodoList todoList) {
        String sqlStatement = "INSERT INTO todo(todo) values(?)";
        Connection connection = database.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, todoList.getTodo());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Insert successful !");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private Integer getDbId(final Integer number) {
        TodoList[] todoLists = getAll();
        Long countElement = Arrays.stream(todoLists).filter(Objects::nonNull).count();
        if (countElement.intValue() == 0) {
            return null;
        }
        var dbId = todoLists[number - 1].getId();
        return dbId;
    }

    @Override
    public Boolean remove(final Integer id) {
        String sqlStatement = "DELETE FROM todos WHERE id = ?";
        Connection connection = database.getConnection();
        var dbId = getDbId(id);
        if (dbId == null) {
            return false;
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, dbId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Delete successful");
                return true;
            }
            return false;

        } catch (Exception e) {
            System.out.println("Delete unsuccessful !");
            return false;
        }
    }

    @Override
    public Boolean edit(final TodoList todoList) {
        String sqlStatement = "UPDATE todos SET todo = ? WHERE id = ?";
        Connection connection = database.getConnection();
        var dbId = getDbId(todoList.getId());
        if (dbId == null) {
            return false;
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, todoList.getTodo());
            preparedStatement.setInt(2, dbId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Update successful");
                return true;
            }
            return false;

        } catch (Exception e) {
            System.out.println("Update unsuccessful !");
            return false;
        }
    }
}
