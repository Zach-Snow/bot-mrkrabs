package io.swcode.teams.bot.mrkrabs;

import java.sql.*;

public class ValidateUser implements DatabaseInterface {
    private String user;
    private boolean validation = false;

    private final String url = "jdbc:postgresql://localhost:5432/Penalty_bot";
    private final String dbUser = "Zakir";
    private final String passWord = "Zakir@413318";

    public ValidateUser (String user)
    {
        this.user = user;
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
            ResultSet userVal = statement.executeQuery("SELECT user_name FROM penalty_user WHERE user_name ='" + user + "'");
            while (userVal.next())
            {
                String getUsrVal = userVal.getString("user_name");
                if(getUsrVal.equals(user))
                {
                    validation = true;
                }
                else
                {
                    validation = false;
                }
            }
            statement.close();
            userVal.close();
        }
        catch (SQLException | ClassNotFoundException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }
}
