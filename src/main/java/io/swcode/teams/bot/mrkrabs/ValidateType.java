package io.swcode.teams.bot.mrkrabs;

import java.sql.*;

public class ValidateType implements DatabaseReportInterface {
    private String type;
    private boolean validation = false;

    private final String url = "jdbc:postgresql://localhost:5432/Penalty_bot";
    private final String dbUser = "Zakir";
    private final String passWord = "Zakir@413318";


    public ValidateType(String type)
    {
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
            ResultSet penTypVal = statement.executeQuery("SELECT penalty_type FROM bot_commands WHERE penalty_type = '" + type + "'");
            while (penTypVal.next())
            {
                String getTypVal = penTypVal.getString("penalty_type");
                if(getTypVal.equals(type))
                {
                    validation = true;
                }
                else {}
            }
            statement.close();
            penTypVal.close();

        }
        catch (SQLException | ClassNotFoundException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }
}
