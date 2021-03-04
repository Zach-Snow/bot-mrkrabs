package io.swcode.teams.bot.mrkrabs;

public class ConversationFlow {

    private Question lastQuestionAsked = Question.None;

    /**
     * Identifies the last question asked.
     */
    public enum Question {
        Command,
        ConfirmPenalty,
        ConfirmReport,
        None //Our last action did not involved a question.

    }

    /**
     * Gets the last question asked.
     * @return The last question asked.
     */
    public Question getLastQuestionAsked() {
        return lastQuestionAsked;
    }

    /**
     * Sets the last question asked.
     * @param withLastQuestionAsked the last question asked.
     */
    public void setLastQuestionAsked(Question withLastQuestionAsked) {
        this.lastQuestionAsked = withLastQuestionAsked;
    }
}
