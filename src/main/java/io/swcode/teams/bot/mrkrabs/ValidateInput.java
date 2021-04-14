package io.swcode.teams.bot.mrkrabs;

import java.sql.*;


public class ValidateInput implements DatabaseReportInterface {
    private String input;

    private boolean validation = false;

    private final String url = "jdbc:postgresql://localhost:5432/Penalty_bot";
    private final String dbUser = "Zakir";
    private final String passWord = "Zakir@413318";

    public ValidateInput(String input)
    {
        this.input = input;
    }


    @Override
    public boolean retValidation(String tableRow, String tableName)
    {
        executeQuery(tableRow, tableName);
        return validation;
    }


    @Override
    public void executeQuery(String tableRow, String tableName) {

        try
        {
            Connection connection = null;
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, dbUser, passWord);
            System.out.println("Connected to PostgreSQL database!");
            Statement statement = connection.createStatement();
            ResultSet commandVal = statement.executeQuery("SELECT "+tableRow+" FROM "+tableName+" WHERE "+tableRow+" ='"+input+"'");
            while (commandVal.next())
            {
                String getComVal = commandVal.getString(tableRow);
                System.out.println(getComVal);
                if(getComVal.equals(input))
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
