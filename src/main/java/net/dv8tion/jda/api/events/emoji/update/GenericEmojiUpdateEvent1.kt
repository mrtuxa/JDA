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
package net.dv8tion.jda.api.events.emoji.update

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji
import net.dv8tion.jda.api.events.UpdateEvent
import net.dv8tion.jda.api.events.emoji.GenericEmojiEvent
import javax.annotation.Nonnull

/**
 * Indicates that an [Custom Emoji][RichCustomEmoji] was updated.
 *
 *
 * **Requirements**<br></br>
 *
 *
 * These events require the [EMOJI][net.dv8tion.jda.api.utils.cache.CacheFlag.EMOJI] CacheFlag to be enabled, which requires
 * the [GUILD_EMOJIS_AND_STICKERS][net.dv8tion.jda.api.requests.GatewayIntent.GUILD_EMOJIS_AND_STICKERS] intent.
 *
 * <br></br>[createLight(String)][net.dv8tion.jda.api.JDABuilder.createLight] disables that CacheFlag by default!
 */
abstract class GenericEmojiUpdateEvent<T>(
    @Nonnull api: JDA, responseNumber: Long, @Nonnull emoji: RichCustomEmoji,
    previous: T?, next: T?, @Nonnull identifier: String
) : GenericEmojiEvent(api, responseNumber, emoji), UpdateEvent<RichCustomEmoji?, T?> {
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
        get() = getEmoji()
    override val oldValue: T?
        get() = previous
    override val newValue: T?
        get() = next
}
