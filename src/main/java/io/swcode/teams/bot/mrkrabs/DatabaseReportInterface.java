package io.swcode.teams.bot.mrkrabs;

public interface DatabaseReportInterface {

    public void executeQuery(String tableRow, String tableName);
    public boolean retValidation(String tableRow, String tableName);
}
