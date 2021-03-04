package io.swcode.teams.bot.mrkrabs;

import java.util.Set;

public class DbConnect implements DatabaseInterface{
    private String user;
    private String cmdTyp;
    private static final Set<String> penaltyList = Set.of("/late", "/rude", "/disrespect");
    private static final Set<String> reportlist = Set.of("/aggregate","/Alltime");

    public DbConnect (String user, String cmdTyp)
    {
        this.user = user;
        this.cmdTyp = cmdTyp;
    }

    @Override
    public String executeQuery()
    {
        String returnVal = null;
        if(cmdTyp.equals("/penalty"))
        {
            returnVal = String.valueOf(penaltyList);
        }
        else if (cmdTyp.equals("/report"))
        {
            returnVal = String.valueOf(reportlist);
        }
        return returnVal;
    }

}
