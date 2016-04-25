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
import tech.aroma.thrift.Message;
import tech.aroma.thrift.exceptions.InvalidArgumentException;
import tech.sirwellington.alchemy.annotations.arguments.Required;
import tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern;

import static tech.sirwellington.alchemy.annotations.designs.patterns.StrategyPattern.Role.INTERFACE;
import static tech.sirwellington.alchemy.arguments.Arguments.checkThat;
import static tech.sirwellington.alchemy.arguments.assertions.Assertions.notNull;


/**
 * An {@link Action} represents a unit of work that can be performed as part of the Aroma Service.
 * As part of it's work, and Action can return one or more additional units of actions that must be
 * completed as well. 
 * 
 * An {@link Action} should take care that it does not regress infinitely, as it will cause an OOM.
 * 
 * @author SirWellington
 */
@StrategyPattern(role = INTERFACE)
public interface Action
{
    /**
     * Performs an action on a Message, and optionally returns a List of subsequent actions.
     * 
     * @param message
     * @return
     * @throws TException 
     */
    public List<Action> actOnMessage(@Required Message message) throws TException;
    
    default void checkMessage(Message message) throws InvalidArgumentException
    {
        checkThat(message)
            .throwing(InvalidArgumentException.class)
            .usingMessage("missing message")
            .is(notNull());
    }
}
