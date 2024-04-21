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
package net.dv8tion.jda.api.events.guild.member.update

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Member
import javax.annotation.Nonnull

/**
 * Indicates that a [Member][net.dv8tion.jda.api.entities.Member] updated their [Guild][net.dv8tion.jda.api.entities.Guild] nickname.
 *
 *
 * Can be used to retrieve members who change their nickname, the triggering guild, the old nick and the new nick.
 *
 *
 * Identifier: `nick`
 *
 *
 * **Requirements**<br></br>
 *
 *
 * This event requires the [GUILD_MEMBERS][net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MEMBERS] intent to be enabled.
 * <br></br>[createDefault(String)][net.dv8tion.jda.api.JDABuilder.createDefault] and
 * [createLight(String)][net.dv8tion.jda.api.JDABuilder.createLight] disable this by default!
 *
 *
 * Additionally, this event requires the [MemberCachePolicy][net.dv8tion.jda.api.utils.MemberCachePolicy]
 * to cache the updated members. Discord does not specifically tell us about the updates, but merely tells us the
 * member was updated and gives us the updated member object. In order to fire a specific event like this we
 * need to have the old member cached to compare against.
 */
class GuildMemberUpdateNicknameEvent(
    @Nonnull api: JDA,
    responseNumber: Long,
    @Nonnull member: Member,
    oldNick: String?
) : GenericGuildMemberUpdateEvent<String?>(api, responseNumber, member, oldNick, member.nickname, IDENTIFIER) {
    val oldNickname: String?
        /**
         * The old nickname
         *
         * @return The old nickname
         */
        get() = oldValue
    val newNickname: String?
        /**
         * The new nickname
         *
         * @return The new nickname
         */
        get() = newValue

    companion object {
        const val IDENTIFIER = "nick"
    }
}
