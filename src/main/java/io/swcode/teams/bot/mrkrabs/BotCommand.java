package io.swcode.teams.bot.mrkrabs;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "bot_command")
@Table(
        name = "bot_command",
        uniqueConstraints = {
                @UniqueConstraint(name = "id_unique", columnNames = "id")
        }
)
public class BotCommand {
    @Id
    @SequenceGenerator(
            name = "botCommand_sequence",
            sequenceName = "botCommand_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "botCommand_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Integer id;

    @Column(
            name = "start_command",
            nullable = true,
            columnDefinition = "TEXT",
            updatable = false
    )
    private String start_command;

    @Column(
            name = "command_type",
            nullable = true,
            columnDefinition = "TEXT",
            updatable = true
    )
    private String command_type;

    @Column(
            name = "penalty_type",
            nullable = true,
            columnDefinition = "TEXT",
            updatable = true
    )
    private String penalty_type;

    @Column(
            name = "report_type",
            nullable = true,
            columnDefinition = "TEXT",
            updatable = true
    )
    private String report_type;

    @Column(
            name = "command_desc",
            nullable = true,
            columnDefinition = "TEXT",
            updatable = true
    )
    private String command_desc;

    @Column(
            name = "penalty_amount",
            nullable = true,
            columnDefinition = "Integer",
            updatable = true
    )
    private Integer penalty_amount;

    public BotCommand(Integer id,
                      String start_command,
                      String command_type,
                      String penalty_type,
                      String report_type,
                      String command_desc,
                      Integer penalty_amount)
    {
        this.id = id;
        this.start_command = start_command;
        this.command_type = command_type;
        this.penalty_type = penalty_type;
        this.report_type = report_type;
        this.command_desc = command_desc;
        this.penalty_amount = penalty_amount;
    }

    public BotCommand() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStart_command() {
        return start_command;
    }

    public void setStart_command(String start_command) {
        this.start_command = start_command;
    }

    public String getCommand_type() {
        return command_type;
    }

    public void setCommand_type(String command_type) {
        this.command_type = command_type;
    }

    public String getPenalty_type() {
        return penalty_type;
    }

    public void setPenalty_type(String penalty_type) {
        this.penalty_type = penalty_type;
    }

    public String getReport_type() {
        return report_type;
    }

    public void setReport_type(String report_type) {
        this.report_type = report_type;
    }

    public String getCommand_desc() {
        return command_desc;
    }

    public void setCommand_desc(String command_desc) {
        this.command_desc = command_desc;
    }

    public Integer getPenalty_amount() {
        return penalty_amount;
    }

    public void setPenalty_amount(Integer penalty_amount) {
        this.penalty_amount = penalty_amount;
    }

    @Override
    public String toString() {
        return "BotCommand{" +
                "id=" + id +
                ", start_command='" + start_command + '\'' +
                ", command_type='" + command_type + '\'' +
                ", penalty_type='" + penalty_type + '\'' +
                ", report_type='" + report_type + '\'' +
                ", command_desc='" + command_desc + '\'' +
                ", penalty_amount=" + penalty_amount +
                '}';
    }
}
