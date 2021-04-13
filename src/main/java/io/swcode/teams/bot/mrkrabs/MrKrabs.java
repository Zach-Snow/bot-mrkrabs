package io.swcode.teams.bot.mrkrabs;

import com.codepoetics.protonpack.collectors.CompletableFutures;
import com.microsoft.bot.builder.*;
import com.microsoft.bot.schema.ChannelAccount;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.CompletableFuture;
import java.util.List;


public class MrKrabs extends ActivityHandler {

    /**No values should be initiated here as it holds the same value for all instances
     * These are for testing purposes, these will be replaced with data from database*/
    DataHashmapReturn showData = new DataHashmapReturn();

    private final String serviceViewable = showData.ResultSetToHashmap("command_type");
    private final String penaltyViewable = showData.ResultSetToHashmap("penalty_type");
    private final String reportViewable  = showData.ResultSetToHashmap("report_type");

    /**To track the state in switch case scenarios*/
    private final String startCmd = "/S";


    private final BotState userState;
    private final BotState conversationState;



    public MrKrabs(ConversationState conversationState, UserState userState) {
        this.conversationState = conversationState;
        this.userState = userState;
    }



    @Override
    protected CompletableFuture<Void> onMessageActivity(TurnContext turnContext) {

        StatePropertyAccessor<ConversationFlow> conversationStateAccessors =
                conversationState.createProperty("ConversationFlow");
        StatePropertyAccessor<UserInputTracker> userStateAccessors = userState.createProperty("UserInput");


        return userStateAccessors.get(turnContext, () -> new UserInputTracker()).thenCompose(Input -> {
            return conversationStateAccessors.get(turnContext, () -> new ConversationFlow()).thenCompose(flow -> {
                return takeUserInputs(flow, Input, turnContext);
            });
        })
                .thenCompose(result -> conversationState.saveChanges(turnContext))
                .thenCompose(result -> userState.saveChanges(turnContext));
    }

    private CompletableFuture<Void> takeUserInputs(ConversationFlow conversationFlow, UserInputTracker userInputTracker, TurnContext turnContext)
    {
        String input = null;

        if (StringUtils.isNotBlank(turnContext.getActivity().getText())) {
            input = turnContext.getActivity().getText();
        }
        switch (conversationFlow.getLastQuestionAsked())
        {
            case None:
                return turnContext.sendActivity("What is your command? Available one's are as follows,"+"\r\n" +"Service list: "+serviceViewable+"\r\n"+ "Penalty types: "+penaltyViewable+"\r\n"+
                        "Report Types: "+reportViewable+"\r\n"+"Format -> /command name@swcode.io type", null, null)
                        .thenRun(() -> {conversationFlow.setLastQuestionAsked(ConversationFlow.Question.Command);});
            case Command:
                int count = StringUtils.countMatches(input, " ");
                if(count ==2)
                {
                    String [] splitInput = input.split (" ");
                    ValidateInput validateInput = new ValidateInput();
                    boolean chekCmd = validateInput.retValidation(splitInput[0],"command_type", "bot_commands");
                    boolean chekUser = validateInput.retValidation(splitInput[1], "user_name","penalty_user");
                    boolean chekTyp = validateInput.retValidation(splitInput[2],"command_type", "bot_commands");

                    if (chekCmd == true && chekUser == true && chekTyp == true)
                    {
                        userInputTracker.command = splitInput[0];
                        userInputTracker.name = splitInput[1];
                        userInputTracker.type = splitInput[2];

                        return turnContext.sendActivity(String.format("You have chosen %s, User: %s and Type: %s.", userInputTracker.command, userInputTracker.name, userInputTracker.type), null, null)
                                .thenCompose(result -> turnContext.sendActivity("Are you sure (Y/N) ?", null, null))
                                .thenRun(() -> { conversationFlow.setLastQuestionAsked(ConversationFlow.Question.ConfirmPenalty); });
                    }
                    else
                    {
                        return turnContext.sendActivity(String.format("'"+input+"'"+ " is not a valid command. Please enter a valid command."+"\r\n"+"Format -> /command name@swcode.io type"), null, null)
                                .thenRun(() -> { conversationFlow.setLastQuestionAsked(ConversationFlow.Question.Command); });
                    }


                }
                else {
                    return turnContext.sendActivity(String.format("'"+input+"'"+ " is not a valid command. Please enter a valid command."), null, null)
                            .thenRun(() -> { conversationFlow.setLastQuestionAsked(ConversationFlow.Question.Command); });
                }

            case ConfirmPenalty:
                if(input.equalsIgnoreCase("Y"))
                {
                    String penaltyHandler = new PenaltyHandler(userInputTracker.name, userInputTracker.type).test();
                    return turnContext.sendActivity(
                            MessageFactory.text(String.format(penaltyHandler+" Please type '/S' to start again."), null, null)
                    ).thenApply(sendResult -> null)
                            .thenRun(() -> { conversationFlow.setLastQuestionAsked(ConversationFlow.Question.None); });
                }
                else if(input.equalsIgnoreCase("N"))
                {
                    return turnContext.sendActivity(String.format("You have canceled your input, please type '/S' to start again."), null, null)
                            .thenRun(() -> { conversationFlow.setLastQuestionAsked(ConversationFlow.Question.None); });
                }
                else
                {
                    return turnContext.sendActivity(String.format("That is a wrong input."), null, null)
                            .thenCompose(result -> turnContext.sendActivity("Please Enter Y or N ", null, null))
                            .thenRun(() -> { conversationFlow.setLastQuestionAsked(ConversationFlow.Question.ConfirmPenalty); });
                }

            case ConfirmReport:
                if(input.equalsIgnoreCase("Y"))
                {
                    String reportHandler = new ReportHandler(userInputTracker.name, userInputTracker.type).test();
                    return turnContext.sendActivity(
                            MessageFactory.text(String.format(reportHandler+" Please type '/S' to start again."), null, null)
                    ).thenApply(sendResult -> null)
                            .thenRun(() -> { conversationFlow.setLastQuestionAsked(ConversationFlow.Question.None); });
                }
                else if(input.equalsIgnoreCase("N"))
                {
                    return turnContext.sendActivity(String.format("You have canceled your input, please type '/S' to start again."), null, null)
                            .thenRun(() -> { conversationFlow.setLastQuestionAsked(ConversationFlow.Question.None); });
                }
                else
                {
                    return turnContext.sendActivity(String.format("That is a wrong input."), null, null)
                            .thenCompose(result -> turnContext.sendActivity("Please Enter Y or N ", null, null))
                            .thenRun(() -> { conversationFlow.setLastQuestionAsked(ConversationFlow.Question.ConfirmReport); });
                }


            default:
                return CompletableFuture.completedFuture(null);

        }

    }

    @Override
    protected CompletableFuture<Void> onMembersAdded(
            List<ChannelAccount> membersAdded,
            TurnContext turnContext
    ) {
        return membersAdded.stream()
                .filter(
                        member -> !StringUtils
                                .equals(member.getId(), turnContext.getActivity().getRecipient().getId())
                ).map(channel -> turnContext.sendActivity(MessageFactory.text("Greetings From Mr. Krabs! Enter "+ startCmd +" to start!")))
                .collect(CompletableFutures.toFutureList()).thenApply(resourceResponses -> null);
    }


}
