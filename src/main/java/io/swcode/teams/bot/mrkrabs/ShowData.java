package io.swcode.teams.bot.mrkrabs;
import java.sql.*;
import java.util.*;

public class ShowData {

    private final String url = "jdbc:postgresql://localhost:5432/Penalty_bot";
    private final String dbUser = "Zakir";
    private final String passWord = "Zakir@413318";



    public String  ResultSetToHashmapCommand(String getViewable)  {
        ArrayList viewableList = new ArrayList(100);
        try
        {
            Connection comConnection = null;
            Connection comDescConnection = null;
            Class.forName("org.postgresql.Driver");
            comConnection = DriverManager.getConnection(url, dbUser, passWord);
            comDescConnection = DriverManager.getConnection(url, dbUser, passWord);
            Statement comStatement = comConnection.createStatement();
            Statement comDescStatement = comDescConnection.createStatement();
            ResultSet retCommands = comStatement.executeQuery("SELECT "+getViewable+" FROM bot_commands ORDER BY id");
            ResultSet retComDesc = comDescStatement.executeQuery("SELECT  command_desc  FROM bot_commands");
            ResultSetMetaData metaData = retCommands.getMetaData();
            int columns = metaData.getColumnCount();
            while (retCommands.next() && retComDesc.next())
            {
                HashMap row = new HashMap(columns);
                if (getViewable.equals("command_type"))
                {
                    for (int i=1; i<=columns; i++ )
                    {
                        String desc = retComDesc.getString("command_desc");
                        row.put(retCommands.getObject(i), desc);

                    }
                }
                else
                {
                    for (int i=1; i<=columns; i++ )
                    {
                        row.put(retCommands.getObject(i), null);

                    }
                }
                viewableList.add(row);


            }
            comStatement.close();
            comDescStatement.close();
            retCommands.close();
            retComDesc.close();
        }

        catch (SQLException | ClassNotFoundException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }

        String retrivedViewable = viewableList.toString().replace("{", "")
                .replace("}","")
                .replace("[","")
                .replace("]","")
                .replace("=null", "");
        return retrivedViewable;
    }


}

