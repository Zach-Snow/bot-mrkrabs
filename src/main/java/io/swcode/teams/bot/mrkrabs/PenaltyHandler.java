package io.swcode.teams.bot.mrkrabs;

public class PenaltyHandler implements DatabaseReportInterface{
    private String user;
    private String type;

    private final String url = "jdbc:postgresql://localhost:5432/Penalty_bot";
    private final String dbUser = "Zakir";
    private final String passWord = "Zakir@413318";


    public PenaltyHandler(String user, String type)
    {
        this.user = user;
        this.type = type;
    }


    @Override
    public void executeQuery(String tableRow, String tableName) {

    }

    @Override
    public boolean retValidation(String tableRow, String tableName) {
        return false;
    }
}
