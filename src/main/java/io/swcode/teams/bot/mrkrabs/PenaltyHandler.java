package io.swcode.teams.bot.mrkrabs;

public class PenaltyHandler {
    private String user;
    private String type;


    public PenaltyHandler(String user, String type)
    {
        this.user = user;
        this.type = type;
    }

    public String test()
    {
        String testReply = user+ " has been penaltied";
        return testReply;
    }



}
