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
/**
 * The core events that are fired by this library, informing
 * the end-user about the state of the current JDA instance.
 *
 *
 * This package contains all implementations of [Event][net.dv8tion.jda.api.events.Event].
 * <br></br>These are specific depending on the event that has been received by the gateway connection.
 *
 *
 * All events are forwarded by an [IEventManager][net.dv8tion.jda.api.hooks.IEventManager] implementation.
 * <br></br>Some events are specific for JDA internal events such as the [ReadyEvent][net.dv8tion.jda.api.events.session.ReadyEvent]
 * which is only fired when JDA finishes to setup its internal cache.
 */
package net.dv8tion.jda.api.events

import net.dv8tion.jda.api.entities.channel.unions.ChannelUnion.asThreadChannel
import net.dv8tion.jda.api.entities.Member.guild
import net.dv8tion.jda.api.entities.GuildVoiceState.approveSpeaker
import net.dv8tion.jda.api.entities.GuildVoiceState.declineSpeaker
import net.dv8tion.jda.api.entities.Member.flags
import net.dv8tion.jda.api.entities.ISnowflake.id
import net.dv8tion.jda.api.entities.Guild.getMemberById
import net.dv8tion.jda.api.audit.AuditLogEntry.getGuild
import net.dv8tion.jda.api.entities.MessageReaction.messageIdLong
import net.dv8tion.jda.api.entities.MessageReaction.getChannel
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion.asPrivateChannel
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel.retrieveMessageById
import net.dv8tion.jda.api.entities.channel.ChannelType.isThread
import net.dv8tion.jda.api.entities.Role.guild
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.automod.AutoModExecution
import net.dv8tion.jda.api.entities.channel.unions.GuildMessageChannelUnion
import net.dv8tion.jda.api.entities.automod.AutoModResponse
import net.dv8tion.jda.api.entities.automod.AutoModTriggerType
import net.dv8tion.jda.api.entities.automod.AutoModRule
import net.dv8tion.jda.api.events.automod.GenericAutoModRuleEvent
import net.dv8tion.jda.api.entities.channel.attribute.IPostContainer
import net.dv8tion.jda.api.entities.channel.forums.ForumTag
import net.dv8tion.jda.api.events.channel.forum.update.GenericForumTagUpdateEvent
import net.dv8tion.jda.api.events.channel.forum.update.ForumTagUpdateNameEvent
import net.dv8tion.jda.api.entities.emoji.EmojiUnion
import net.dv8tion.jda.api.events.channel.forum.update.ForumTagUpdateEmojiEvent
import net.dv8tion.jda.api.events.channel.forum.GenericForumTagEvent
import net.dv8tion.jda.api.events.UpdateEvent
import net.dv8tion.jda.api.events.channel.forum.update.ForumTagUpdateModeratedEvent
import net.dv8tion.jda.api.events.channel.update.GenericChannelUpdateEvent
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateNSFWEvent
import net.dv8tion.jda.api.entities.channel.ChannelField
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateNameEvent
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateTypeEvent
import java.util.EnumSet
import net.dv8tion.jda.api.entities.channel.ChannelFlag
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateFlagsEvent
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateTopicEvent
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateLockedEvent
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateParentEvent
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateRegionEvent
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateBitrateEvent
import net.dv8tion.jda.api.events.channel.GenericChannelEvent
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateArchivedEvent
import net.dv8tion.jda.api.events.channel.update.ChannelUpdatePositionEvent
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateSlowmodeEvent
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateInvitableEvent
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateUserLimitEvent
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateAppliedTagsEvent
import net.dv8tion.jda.api.utils.cache.SortedSnowflakeCacheView
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateVoiceStatusEvent
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateDefaultLayoutEvent
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateDefaultReactionEvent
import java.time.OffsetDateTime
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateArchiveTimestampEvent
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateDefaultSortOrderEvent
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel.AutoArchiveDuration
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateAutoArchiveDurationEvent
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateDefaultThreadSlowmodeEvent
import net.dv8tion.jda.api.entities.channel.unions.ChannelUnion
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji
import net.dv8tion.jda.api.events.emoji.update.GenericEmojiUpdateEvent
import net.dv8tion.jda.api.events.emoji.update.EmojiUpdateNameEvent
import net.dv8tion.jda.api.events.emoji.update.EmojiUpdateRolesEvent
import net.dv8tion.jda.api.events.emoji.GenericEmojiEvent
import net.dv8tion.jda.api.entities.Entitlement
import net.dv8tion.jda.api.events.entitlement.GenericEntitlementEvent
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent
import net.dv8tion.jda.api.events.guild.GenericGuildEvent
import net.dv8tion.jda.api.entities.GuildVoiceState
import net.dv8tion.jda.api.requests.RestAction
import net.dv8tion.jda.api.entities.Invite
import net.dv8tion.jda.api.events.guild.invite.GenericGuildInviteEvent
import net.dv8tion.jda.api.entities.channel.unions.GuildChannelUnion
import net.dv8tion.jda.api.entities.Member.MemberFlag
import net.dv8tion.jda.api.events.guild.member.update.GenericGuildMemberUpdateEvent
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateFlagsEvent
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateAvatarEvent
import net.dv8tion.jda.api.utils.ImageProxy
import net.dv8tion.jda.api.events.guild.member.GenericGuildMemberEvent
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdatePendingEvent
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateTimeOutEvent
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent
import net.dv8tion.jda.api.events.guild.update.GenericGuildUpdateEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateIconEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateOwnerEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBannerEvent
import net.dv8tion.jda.api.interactions.DiscordLocale
import net.dv8tion.jda.api.events.guild.update.GuildUpdateLocaleEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateSplashEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateFeaturesEvent
import Guild.MFALevel
import net.dv8tion.jda.api.events.guild.update.GuildUpdateMFALevelEvent
import Guild.BoostTier
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostTierEvent
import Guild.NSFWLevel
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNSFWLevelEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateAfkChannelEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateAfkTimeoutEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostCountEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateMaxMembersEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateVanityCodeEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateDescriptionEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateMaxPresencesEvent
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.guild.update.GuildUpdateRulesChannelEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateSystemChannelEvent
import Guild.NotificationLevel
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNotificationLevelEvent
import Guild.VerificationLevel
import net.dv8tion.jda.api.events.guild.update.GuildUpdateVerificationLevelEvent
import Guild.ExplicitContentLevel
import net.dv8tion.jda.api.events.guild.update.GuildUpdateExplicitContentLevelEvent
import net.dv8tion.jda.api.events.guild.update.GuildUpdateCommunityUpdatesChannelEvent
import net.dv8tion.jda.api.entities.channel.attribute.IPermissionContainer
import net.dv8tion.jda.api.events.guild.override.GenericPermissionOverrideEvent
import net.dv8tion.jda.api.entities.channel.unions.IPermissionContainerUnion
import net.dv8tion.jda.api.entities.IPermissionHolder
import net.dv8tion.jda.api.entities.ScheduledEvent
import net.dv8tion.jda.api.events.guild.scheduledevent.update.GenericScheduledEventUpdateEvent
import net.dv8tion.jda.api.events.guild.scheduledevent.update.ScheduledEventUpdateNameEvent
import net.dv8tion.jda.api.events.guild.scheduledevent.update.ScheduledEventUpdateImageEvent
import net.dv8tion.jda.api.events.guild.scheduledevent.update.ScheduledEventUpdateStatusEvent
import net.dv8tion.jda.api.events.guild.scheduledevent.GenericScheduledEventGatewayEvent
import net.dv8tion.jda.api.events.guild.scheduledevent.update.ScheduledEventUpdateEndTimeEvent
import net.dv8tion.jda.api.events.guild.scheduledevent.update.ScheduledEventUpdateLocationEvent
import net.dv8tion.jda.api.events.guild.scheduledevent.update.ScheduledEventUpdateStartTimeEvent
import net.dv8tion.jda.api.events.guild.scheduledevent.update.ScheduledEventUpdateDescriptionEvent
import net.dv8tion.jda.api.events.guild.scheduledevent.GenericScheduledEventUserEvent
import net.dv8tion.jda.api.requests.restaction.CacheRestAction
import net.dv8tion.jda.api.audit.AuditLogEntry
import net.dv8tion.jda.api.utils.data.DataArray
import net.dv8tion.jda.api.utils.data.DataObject
import net.dv8tion.jda.api.requests.Route.CompiledRoute
import net.dv8tion.jda.api.interactions.commands.privileges.IntegrationPrivilege
import net.dv8tion.jda.api.interactions.commands.privileges.PrivilegeTargetType
import net.dv8tion.jda.api.interactions.commands.context.UserContextInteraction
import net.dv8tion.jda.api.events.interaction.command.GenericContextInteractionEvent
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion
import net.dv8tion.jda.api.interactions.commands.CommandInteraction
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import net.dv8tion.jda.api.interactions.InteractionHook
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction
import net.dv8tion.jda.api.requests.restaction.interactions.ModalCallbackAction
import net.dv8tion.jda.api.requests.restaction.interactions.PremiumRequiredCallbackAction
import net.dv8tion.jda.api.interactions.commands.context.ContextInteraction
import net.dv8tion.jda.api.interactions.commands.context.ContextInteraction.ContextTarget
import net.dv8tion.jda.api.interactions.commands.context.MessageContextInteraction
import net.dv8tion.jda.api.events.interaction.command.GenericPrivilegeUpdateEvent
import net.dv8tion.jda.api.interactions.commands.CommandAutoCompleteInteraction
import net.dv8tion.jda.api.events.interaction.GenericAutoCompleteInteractionEvent
import net.dv8tion.jda.api.interactions.AutoCompleteQuery
import net.dv8tion.jda.api.requests.restaction.interactions.AutoCompleteCallbackAction
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectInteraction
import net.dv8tion.jda.api.events.interaction.component.GenericSelectMenuInteractionEvent
import net.dv8tion.jda.api.entities.IMentionable
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu
import net.dv8tion.jda.api.entities.Mentions
import net.dv8tion.jda.api.interactions.components.selections.StringSelectInteraction
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu
import net.dv8tion.jda.api.interactions.components.selections.SelectMenuInteraction
import net.dv8tion.jda.api.interactions.components.ComponentInteraction
import net.dv8tion.jda.api.interactions.components.ActionComponent
import net.dv8tion.jda.api.requests.restaction.interactions.MessageEditCallbackAction
import net.dv8tion.jda.api.interactions.modals.ModalInteraction
import net.dv8tion.jda.api.interactions.modals.ModalMapping
import net.dv8tion.jda.api.interactions.callbacks.IAutoCompleteCallback
import net.dv8tion.jda.api.entities.MessageReaction
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent
import net.dv8tion.jda.api.events.message.GenericMessageEvent
import net.dv8tion.jda.internal.requests.CompletedRestAction
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel
import net.dv8tion.jda.api.entities.RoleIcon
import net.dv8tion.jda.api.events.role.update.GenericRoleUpdateEvent
import net.dv8tion.jda.api.events.role.update.RoleUpdateIconEvent
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent
import net.dv8tion.jda.api.events.role.update.RoleUpdateColorEvent
import net.dv8tion.jda.api.events.role.GenericRoleEvent
import net.dv8tion.jda.api.events.role.update.RoleUpdateHoistedEvent
import net.dv8tion.jda.api.events.role.update.RoleUpdatePositionEvent
import net.dv8tion.jda.api.events.role.update.RoleUpdateMentionableEvent
import net.dv8tion.jda.api.events.role.update.RoleUpdatePermissionsEvent
import net.dv8tion.jda.api.events.self.GenericSelfUpdateEvent
import net.dv8tion.jda.api.events.self.SelfUpdateMFAEvent
import net.dv8tion.jda.api.events.self.SelfUpdateNameEvent
import net.dv8tion.jda.api.events.self.SelfUpdateAvatarEvent
import net.dv8tion.jda.api.entities.SelfUser
import net.dv8tion.jda.api.events.self.SelfUpdateVerifiedEvent
import net.dv8tion.jda.api.events.self.SelfUpdateGlobalNameEvent
import net.dv8tion.jda.annotations.ForRemoval
import net.dv8tion.jda.api.events.self.SelfUpdateDiscriminatorEvent
import net.dv8tion.jda.api.events.session.GenericSessionEvent
import net.dv8tion.jda.api.events.session.SessionState
import net.dv8tion.jda.internal.handle.GuildSetupController
import net.dv8tion.jda.internal.JDAImpl
import net.dv8tion.jda.api.requests.CloseCode
import com.neovisionaries.ws.client.WebSocketFrame
import net.dv8tion.jda.api.entities.StageInstance
import net.dv8tion.jda.api.events.stage.update.GenericStageInstanceUpdateEvent
import net.dv8tion.jda.api.events.stage.update.StageInstanceUpdateTopicEvent
import net.dv8tion.jda.api.events.stage.GenericStageInstanceEvent
import net.dv8tion.jda.api.entities.StageInstance.PrivacyLevel
import net.dv8tion.jda.api.events.stage.update.StageInstanceUpdatePrivacyLevelEvent
import net.dv8tion.jda.api.entities.channel.concrete.StageChannel
import net.dv8tion.jda.api.entities.sticker.GuildSticker
import net.dv8tion.jda.api.events.sticker.update.GenericGuildStickerUpdateEvent
import net.dv8tion.jda.api.events.sticker.update.GuildStickerUpdateNameEvent
import net.dv8tion.jda.api.events.sticker.update.GuildStickerUpdateTagsEvent
import net.dv8tion.jda.api.events.sticker.GenericGuildStickerEvent
import net.dv8tion.jda.api.events.sticker.update.GuildStickerUpdateAvailableEvent
import net.dv8tion.jda.api.events.sticker.update.GuildStickerUpdateDescriptionEvent
import net.dv8tion.jda.api.entities.ThreadMember
import net.dv8tion.jda.api.events.thread.member.GenericThreadMemberEvent
import net.dv8tion.jda.api.events.thread.GenericThreadEvent
import net.dv8tion.jda.api.events.user.update.GenericUserUpdateEvent
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent
import net.dv8tion.jda.api.entities.User.UserFlag
import net.dv8tion.jda.api.events.user.update.UserUpdateFlagsEvent
import net.dv8tion.jda.api.events.user.update.UserUpdateAvatarEvent
import net.dv8tion.jda.api.events.user.GenericUserEvent
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.user.update.UserUpdateActivitiesEvent
import net.dv8tion.jda.api.events.user.update.GenericUserPresenceEvent
import net.dv8tion.jda.api.events.user.update.UserUpdateGlobalNameEvent
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent
import net.dv8tion.jda.api.events.user.update.UserUpdateActivityOrderEvent
import net.dv8tion.jda.api.events.user.update.UserUpdateDiscriminatorEvent
import net.dv8tion.jda.internal.utils.EntityString
import net.dv8tion.jda.api.events.GatewayPingEvent
import net.dv8tion.jda.api.events.StatusChangeEvent
