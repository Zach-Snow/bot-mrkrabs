// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package io.swcode.teams.bot.mrkrabs;

import com.codepoetics.protonpack.collectors.CompletableFutures;
import com.microsoft.bot.builder.*;
import com.microsoft.bot.schema.ChannelAccount;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * This class implements the functionality of the Bot.
 *
 * <p>
 * This is where application specific logic for interacting with the users would be added. For this
 * sample, the {@link #onMessageActivity(TurnContext)} echos the text back to the user. The {@link
 * #onMembersAdded(List, TurnContext)} will send a greeting to new conversation participants.
 * </p>
 */
public class MrKrabs extends ActivityHandler {

    /**No values should be initiated here as it holds the same value for all instances
     * These are for testing purposes, these will be replaced with data from database*/
    private  final Set <String> serviceList = new HashSet<>(Arrays.asList("/penalty","/report"));
    private  final Set <String>  penaltyList = Set.of("late", "rude", "disrespect");
    private  final Set <String>  reportList = Set.of("aggregate", "aggregateAll");

    /**To track the state in switch case scenarios*/
    private final String startCmd = "/S";
    private final String penaltyState ="1";
    private final String reportState ="2";
    private final String errorState = "3";


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
                return turnContext.sendActivity("What is your command? Available one's are as follows,"+"\r\n" +"Service list: "+serviceList+"\r\n"+ "Penalty types: "+penaltyList+"\r\n"+
                        "Report Types: "+reportList+"\r\n"+"Format -> /command name@swcode.io type", null, null)
                        .thenRun(() -> {conversationFlow.setLastQuestionAsked(ConversationFlow.Question.Command);});
            case Command:
                int count = StringUtils.countMatches(input, " ");
                if(count ==2)
                {
                    String [] splitInput = input.split (" ");
                    ValidateCommand validateCommand = new ValidateCommand(splitInput[0]);
                    ValidateUser validateUser = new ValidateUser(splitInput[1]);
                    ValidateType validateType = new ValidateType(splitInput[2]);
                    boolean chekCmd = validateCommand.retValidation();
                    boolean chekUser = validateUser.retValidation();
                    boolean chekTyp = validateType.retValidation();

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

    /*private String validateType(String getType)
    {
        String retString;
        if(penaltyList.contains(getType))
        {
            retString = penaltyState;
        }
        else if (reportList.contains(getType))
        {
            retString = reportState;
        }
        else if (getType ==null)
        {
            retString = errorState;
        }
        else
        {
            retString = errorState;
        }
        return retString;
    }


    public String validateCommand(String getCommand)
    {
        String retString;
        if (serviceList.contains(getCommand) && getCommand.equalsIgnoreCase("/penalty"))
        {
            retString = penaltyState;
        }
        else if(serviceList.contains(getCommand) && getCommand.equalsIgnoreCase("/report"))
        {
            retString =reportState;
        }
        else
        {
            retString = errorState;
        }
        return retString;
    }

    public String validateName(String getName)
    {
        String retString;
        String userIDChk = "@swcode.io";
        if(getName.endsWith(userIDChk))
        {
            if(getName.equals(userIDChk))
            {
                retString =errorState;
            }
            else
            {
                retString = penaltyState;
            }

        }
        else
        {
            retString = errorState;
        }
        return retString;
    }*/


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
