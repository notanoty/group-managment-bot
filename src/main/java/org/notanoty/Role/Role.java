package org.notanoty.Role;

import org.notanoty.Colors;
import org.notanoty.DB.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Role
{
    public static Roles getRole(long chat_id, long user_id)
    {

        String findUserQuery = "SELECT * FROM roles WHERE chat_id = ? and user_id  = ?";
        try
        {
            Connection connection = DB.connect();

            PreparedStatement preparedStatementFindUserId = connection.prepareStatement(findUserQuery);
            preparedStatementFindUserId.setLong(1, chat_id);
            preparedStatementFindUserId.setLong(2, user_id);

            ResultSet resultSet = preparedStatementFindUserId.executeQuery();

            if (resultSet.next())
            {

                String role = resultSet.getString("role");
                resultSet.close();
                preparedStatementFindUserId.close();
                connection.close();
                return Role.roleToEnum(role);
            }
            else
            {
                System.out.println(Colors.RED + "Error" + Colors.RESET + ": The user wasn't found");
                resultSet.close();
                preparedStatementFindUserId.close();
                connection.close();
            }


        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return Roles.ERROR_USER;
    }

    public static void setRole(long chat_id, long user_id, Roles role)
    {

        String strikeQuery = "INSERT INTO roles (chat_id, user_id, role) VALUES (?, ?, ?)";
        try
        {
            Connection connection = DB.connect();

            PreparedStatement preparedStatementStrike = connection.prepareStatement(strikeQuery);

            preparedStatementStrike.setLong(1, chat_id);
            preparedStatementStrike.setLong(2, user_id);
            preparedStatementStrike.setString(3, role.toString());

            int rowsInserted = preparedStatementStrike.executeUpdate();

            if (rowsInserted > 0)
            {
                System.out.println("A new strike record was inserted successfully!");
            }

            preparedStatementStrike.close();
            connection.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static Roles roleToEnum(String role)
    {
        return switch (role)
        {
            case "SUPER_USER" -> Roles.SUPER_USER;
            case "ADMIN" -> Roles.ADMIN;
            case "USER" -> Roles.NORMAL_USER;
            default -> Roles.ERROR_USER;
        };
    }
}
