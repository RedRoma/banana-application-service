/*
 * Copyright 2016 RedRoma.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.aroma.application.service.reactions.actions;

import java.util.List;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sir.wellington.alchemy.collections.lists.Lists;
import tech.aroma.thrift.Message;
import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern;

import static tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern.Role.CONCRETE_BEHAVIOR;
import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.StringAssertions.nonEmptyString;

/**
 *
 * @author SirWellington
 */
@StrategyPattern(role = CONCRETE_BEHAVIOR)
@Internal
final class ForwardToSlackAction implements Action
{

    private final static Logger LOG = LoggerFactory.getLogger(ForwardToSlackAction.class);

    private final boolean isUser;
    private final String channel;
    private final String slackDomain;
    private final String webToken;

    ForwardToSlackAction(boolean isUser, String channel, String slackDomain, String webToken)
    {
        checkThat(channel, slackDomain, webToken)
            .are(nonEmptyString());

        this.isUser = isUser;
        this.channel = channel;
        this.slackDomain = slackDomain;
        this.webToken = webToken;
    }

    @Override
    public List<Action> actOnMessage(Message message) throws TException
    {

        return Lists.emptyList();
    }

}
