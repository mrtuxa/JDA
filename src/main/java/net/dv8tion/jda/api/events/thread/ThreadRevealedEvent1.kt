/*
 * Copyright 2015 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.dv8tion.jda.api.events.thread

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel
import javax.annotation.Nonnull

/**
 * This event is dispatched when a [ThreadChannel] that JDA didn't previously have access to (due to permissions) is now visible.
 *
 *
 * For example, if the bot is given the [Permission.ADMINISTRATOR] permission, any thread channels that the bot could not previously see would be "revealed".
 * This event is also used when the bot is added to a [private][ThreadChannel.isPublic] thread channel, revealing it through membership.
 *
 * @see ThreadHiddenEvent
 */
class ThreadRevealedEvent(@Nonnull api: JDA, responseNumber: Long, thread: ThreadChannel) :
    GenericThreadEvent(api, responseNumber, thread)
