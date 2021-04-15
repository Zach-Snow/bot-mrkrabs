package io.swcode.teams.bot.mrkrabs;

import java.sql.*;

public class PenaltyHandler implements DatabaseReportInterface{
    private String user;
    private String type;

    private final String url = "jdbc:postgresql://localhost:5432/Penalty_bot";
    private final String dbUser = "Zakir";
    private final String passWord = "Zakir@413318";


    public PenaltyHandler(String user, String type)
    {
        this.user = user;
        this.type = type;
    }


    @Override
    public void executeQuery(String tableRow, String tableName){
        try
        {
            Connection connection = null;
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, dbUser, passWord);
            System.out.println("Connected to PostgreSQL database!");
            Statement statement = connection.createStatement();
            ResultSet commandVal = statement.executeQuery("UPDATE penalty_user SET penalty_amount = 0 WHERE user_name LIKE 'zakir@swcode.io'");
            while (commandVal.next())
            {

            }
            statement.close();
            commandVal.close();
        }
        catch (SQLException | ClassNotFoundException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }

    }

    @Override
    public boolean retValidation(String tableRow, String tableName) {
        return false;
    }
}
