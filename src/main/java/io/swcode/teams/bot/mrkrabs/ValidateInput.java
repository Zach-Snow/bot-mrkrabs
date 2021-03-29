package io.swcode.teams.bot.mrkrabs;

import java.util.Set;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ValidateInput implements DatabaseInterface {
    private String command;
    private String user;
    private String type;

    private boolean validation;

    /**
     * Database connection stuff
     */
    private final String url = "jdbc:postgresql://localhost:5432/Penalty_bot";
    private final String dbUser = "Zakir";
    private final String passWord = "Zakir@413318";
    private final String comValid = "SELECT command_type FROM bot_commands WHERE command_type ='" + command + "'";
    private final String userValid = "SELECT user_name FROM penalty_user WHERE user_name ='" + user + "'";
    private final String typeValid = "SELECT command_type FROM bot_commands WHERE command_type = '" + type + "'";

    DbConnect dbConnect = new DbConnect();

    public ValidateInput(String command, String user, String type) {
        this.command = command;
        this.user = user;
        this.type = type;
    }


    public boolean retValidation()
    {
        executeQuery();
        return validation;
    }


    @Override
    public void executeQuery() {
        try
        {
            Connection connection = null;
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, dbUser, passWord);
            System.out.println("Connected to PostgreSQL database!");
            Statement statement = connection.createStatement();
            ResultSet commandVal = statement.executeQuery(comValid);
            ResultSet userVal = statement.executeQuery(userValid);
            ResultSet typVal = statement.executeQuery(typeValid);
            while (commandVal.next() && userVal.next() && typVal.next()) {
                String getComVal = commandVal.getString("command_type");
                String getUsrVal = userVal.getString("user_name");
                String getTypVal = typVal.getString("command_type");
                if (getComVal.equals(command) && getUsrVal.equals(user) && getTypVal.equals(type))
                {
                    validation = true;
                }
                else
                {
                    validation = false;
                }
            }

        } /*catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC driver not found.");
            e.printStackTrace();
        }*/ catch (SQLException | ClassNotFoundException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }
}
