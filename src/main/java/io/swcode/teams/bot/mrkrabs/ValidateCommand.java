package io.swcode.teams.bot.mrkrabs;

import java.sql.*;
import java.util.Set;


public class ValidateCommand implements DatabaseInterface {
    private String command;

    private boolean validation = false;

    /**
     * Database connection stuff
     */
    private final String url = "jdbc:postgresql://localhost:5432/Penalty_bot";
    private final String dbUser = "Zakir";
    private final String passWord = "Zakir@413318";


    public ValidateCommand(String command)
    {
        this.command = command;
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
            ResultSet commandVal = statement.executeQuery("SELECT command_type FROM bot_commands WHERE command_type ='"+command+"'");
            while (commandVal.next())
            {
                String getComVal = commandVal.getString("command_type");
                System.out.println(getComVal);
                if(getComVal.equals(command))
                {
                    validation = true;
                }
                else{}
            }
            statement.close();
            commandVal.close();
        }
        catch (SQLException | ClassNotFoundException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }
}
