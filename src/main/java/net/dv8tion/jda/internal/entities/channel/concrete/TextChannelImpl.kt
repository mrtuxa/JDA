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
package net.dv8tion.jda.internal.entities.channel.concrete

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.entities.channel.unions.DefaultGuildChannelUnion
import net.dv8tion.jda.api.managers.channel.concrete.TextChannelManager
import net.dv8tion.jda.api.requests.restaction.ChannelAction
import net.dv8tion.jda.api.requests.restaction.StageInstanceAction.setTopic
import net.dv8tion.jda.internal.entities.GuildImpl
import net.dv8tion.jda.internal.entities.channel.middleman.AbstractStandardGuildMessageChannelImpl
import net.dv8tion.jda.internal.entities.channel.mixin.attribute.ISlowmodeChannelMixin
import net.dv8tion.jda.internal.managers.channel.concrete.TextChannelManagerImpl
import net.dv8tion.jda.internal.utils.Checks
import net.dv8tion.jda.internal.utils.Helpers
import java.util.function.Predicate
import javax.annotation.Nonnull

class TextChannelImpl(id: Long, guild: GuildImpl?) :
    AbstractStandardGuildMessageChannelImpl<TextChannelImpl?>(id, guild), TextChannel, DefaultGuildChannelUnion,
    ISlowmodeChannelMixin<TextChannelImpl?> {
    override var slowmode = 0
        private set

    @get:Nonnull
    override val type: ChannelType
        get() = ChannelType.TEXT

    @get:Nonnull
    override val members: List<Member>
        get() = getGuild().getMembersView().stream()
            .filter(Predicate<Member> { m: Member -> m.hasPermission(this, Permission.VIEW_CHANNEL) })
            .collect<List<Member>, Any>(Helpers.toUnmodifiableList<Member>())

    @Nonnull
    override fun createCopy(@Nonnull guild: Guild?): ChannelAction<TextChannel?>? {
        Checks.notNull(guild, "Guild")
        val action: ChannelAction<TextChannel?> =
            guild.createTextChannel(name).setNSFW(nsfw).setTopic(topic).setSlowmode(slowmode)
        if (guild == getGuild()) {
            val parent = parentCategory
            if (parent != null) action.setParent(parent)
            for (o in overrides.valueCollection()) {
                if (o.isMemberOverride) action.addMemberPermissionOverride(
                    o.idLong,
                    o.allowedRaw,
                    o.deniedRaw
                ) else action.addRolePermissionOverride(o.idLong, o.allowedRaw, o.deniedRaw)
            }
        }
        return action
    }

    @get:Nonnull
    override val manager: TextChannelManager
        get() = TextChannelManagerImpl(this)

    override fun setSlowmode(slowmode: Int): TextChannelImpl {
        this.slowmode = slowmode
        return this
    }
}
