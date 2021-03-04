package io.swcode.teams.bot.mrkrabs;

public class ReportHandler {
    private String user;
    private String type;


    public ReportHandler(String user, String type)
    {
        this.user = user;
        this.type = type;
    }

    public String test()
    {
        String testReply = "The report for " +user+ " is this.";
        return testReply;
    }
}
