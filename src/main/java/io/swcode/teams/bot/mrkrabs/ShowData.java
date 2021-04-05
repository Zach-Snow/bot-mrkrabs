package io.swcode.teams.bot.mrkrabs;
import java.sql.*;
import java.util.*;

public class ShowData {

    private final String url = "jdbc:postgresql://localhost:5432/Penalty_bot";
    private final String dbUser = "Zakir";
    private final String passWord = "Zakir@413318";
    ArrayList list = new ArrayList(100);



    public List  resultSetToHashmap()  {
        try
        {
            Connection connection = null;
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, dbUser, passWord);
            System.out.println("Connected to PostgreSQL database!");
            Statement statement = connection.createStatement();
            ResultSet retCommands = statement.executeQuery("SELECT command_type, report_type, penalty_type  FROM bot_commands");
            ResultSetMetaData metaData = retCommands.getMetaData();
            int columns = metaData.getColumnCount();
            while (retCommands.next())
            {
                HashMap row = new HashMap(columns);
                for (int i=1; i<=columns; i++ )
                {
                    row.put(metaData.getColumnName(i), retCommands.getObject(i));

                }
                list.add(row);
            }
            statement.close();
            retCommands.close();
        }

        catch (SQLException | ClassNotFoundException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
        System.out.println(list);
        return list;
    }

}

