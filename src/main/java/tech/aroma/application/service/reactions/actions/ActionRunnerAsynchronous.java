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

import com.google.common.collect.Queues;
import java.util.List;
import java.util.Queue;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sir.wellington.alchemy.collections.lists.Lists;
import tech.aroma.thrift.Message;
import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern;

import static java.util.stream.Collectors.toList;
import static tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern.Role.CONCRETE_BEHAVIOR;

/**
 * This is a concrete implementation of the {@link ActionRunner} interface
 * that executes {@linkplain Action Actions} in a FI-FO manner.
 * 
 * @author SirWellington
 */
@Internal
@StrategyPattern(role = CONCRETE_BEHAVIOR)
final class ActionRunnerAsynchronous implements ActionRunner
{

    private final static Logger LOG = LoggerFactory.getLogger(ActionRunnerAsynchronous.class);

    @Override
    public int runThroughActions(Message message, List<Action> actions)
    {
        
        int totalRuns = 0;
        Queue<Action> queue = Queues.newLinkedBlockingDeque(actions);

        while (!queue.isEmpty())
        {
            List<Action> additionalActions = queue.parallelStream()
                .map(action -> this.runAction(message, action))
                .flatMap(List::stream)
                .collect(toList());

            queue.addAll(additionalActions);

            ++totalRuns;
        }

        return totalRuns;
    }

    private List<Action> runAction(Message message, Action action)
    {
        try
        {
            return action.actOnMessage(message);
        }
        catch (TException ex)
        {
            LOG.error("Failed to run Action {} on Message {}", action, message, ex);
            return Lists.emptyList();
        }
    }
}