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
package net.dv8tion.jda.api.events.user.update

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.UpdateEvent
import net.dv8tion.jda.api.events.user.GenericUserEvent
import javax.annotation.Nonnull

/**
 * Indicates that a user has updated their presence on discord.
 * <br></br>This includes name, avatar, and similar visible features of the user.
 *
 *
 * **Requirements**<br></br>
 *
 *
 * These events require the [GUILD_MEMBERS][net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MEMBERS] intent to be enabled.
 * <br></br>[createDefault(String)][net.dv8tion.jda.api.JDABuilder.createDefault] and
 * [createLight(String)][net.dv8tion.jda.api.JDABuilder.createLight] disable this by default!
 *
 *
 * Additionally, these events require the [MemberCachePolicy][net.dv8tion.jda.api.utils.MemberCachePolicy]
 * to cache the updated members. Discord does not specifically tell us about the updates, but merely tells us the
 * member was updated and gives us the updated member object. In order to fire a specific event like this we
 * need to have the old member cached to compare against.
 *
 * @param <T>
 * The type of the updated value
</T> */
abstract class GenericUserUpdateEvent<T>(
    @Nonnull api: JDA, responseNumber: Long, @Nonnull user: User?,
    previous: T?, next: T?, @Nonnull identifier: String
) : GenericUserEvent(api, responseNumber, user), UpdateEvent<User?, T?> {
    protected val previous: T
    protected val next: T

    @get:Nonnull
    override val propertyIdentifier: String

    init {
        this.previous = previous
        this.next = next
        propertyIdentifier = identifier
    }

    @get:Nonnull
    override val entity: E
        get() = user
    override val oldValue: T?
        get() = previous
    override val newValue: T?
        get() = next
}
