package io.swcode.teams.bot.mrkrabs;

import java.util.Set;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DbConnect implements DatabaseInterface{
    private String cmdTyp;

    /**remember to remove*/
    private static final Set<String> penaltyList = Set.of("/late", "/rude", "/disrespect");
    private static final Set<String> reportlist = Set.of("/aggregate","/Alltime");

    private final String url = "jdbc:postgresql://localhost:5432/PenaltyBot";
    private final String user = "postgres";
    private final String passWord = "Zakir@413318";
    private final String startQuery = "SELECT start_commands FROM bot_commands WHERE start_commands ='/s'";

    public DbConnect()
    {}

    public DbConnect (String cmdTyp)
    {
        this.cmdTyp = cmdTyp;
    }


    @Override
    public void executeQuery()
    {
        try (Connection connection = DriverManager.getConnection(url, user, passWord)) {

            System.out.println("Connected to PostgreSQL database!");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(startQuery);
            while (resultSet.next()) {
                String getVal = resultSet.getString("start_commands");
                if(getVal.equals(cmdTyp))
                {
                    retValue();
                }
            }

        } /*catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC driver not found.");
            e.printStackTrace();
        }*/ catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }

    public String retValue()
    {
        return cmdTyp;
    }

}
