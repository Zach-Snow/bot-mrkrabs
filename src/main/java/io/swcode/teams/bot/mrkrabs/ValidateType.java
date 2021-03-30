package io.swcode.teams.bot.mrkrabs;

import java.sql.*;

public class ValidateType implements DatabaseInterface {
    private String type;
    private boolean validation = false;

    private final String url = "jdbc:postgresql://localhost:5432/Penalty_bot";
    private final String dbUser = "Zakir";
    private final String passWord = "Zakir@413318";

    private final String penTypeValid = "SELECT penalty_type FROM bot_commands WHERE penalty_type = '" + type + "'";
    //private final String repTypeValid = "SELECT report_type FROM bot_commands WHERE report_type = '" + type + "'";

    public ValidateType(String type)
    {
        this.type = type;
    }

    @Override
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
            ResultSet penTypVal = statement.executeQuery(penTypeValid);
            //ResultSet repTypVal = statement.executeQuery(repTypeValid);
            while (penTypVal.next())
            {
                String getTypVal = penTypVal.getString("penalty_type");
                if(getTypVal.equals(type))
                {
                    validation = true;
                }
                else
                {
                    validation = false;
                }
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }
}
