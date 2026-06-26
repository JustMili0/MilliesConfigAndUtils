package net.justmili.libs.v1.event;

/*
Sources: https://wiki.fabricmc.net/tutorial:event_index

Events to replicate:

ServerLifecycleEvents
- SERVER_STARTING
- SERVER_STARTED
- SERVER_STOPPING
- SERVER_STOPPED
- BEFORE_SAVE -> SAVE_PRE
- AFTER_SAVE -> SAVE_POST
- START_DATA_PACK_RELOAD -> DATAPACK_RELOAD_PRE
- END_DATA_PACK_RELOAD -> DATAPACK_RELOAD_POST
- SYNC_DATA_PACK_CONTENTS -> DATAPACK_SYNC_CONTENTS

ServerTickEvents
- START_SERVER_TICK -> SERVER_PRE
- END_SERVER_TICK -> SERVER_POST
- START_WORLD_TICK -> WORLD_PRE
- END_WORLD_TICK -> WORLD_POST

ServerEntityEvents
- ENTITY_LOAD
- ENTITY_UNLOAD
- EQUIPMENT_CHANGE

ServerEntityCombatEvents -> ServerCombatEvents
- AFTER_KILLED_OTHER_ENTITY -> KILLED_OTHER_ENTITY_POST

ServerLivingEntityEvents -> LivingEntityEvents
- ALLOW_DAMAGE
- AFTER_DAMAGE -> DAMAGE_POST
- ALLOW_DEATH
- AFTER_DEATH -> DEATH_POST
- MOB_CONVERSION

EntitySleepEvents
- ALLOW_BED -> IS_BED
- ALLOW_SETTING_SPAWN
- ALLOW_NEARBY_MONSTERS
- ALLOW_RESETTING_TIME
- START_SLEEPING -> SLEEP
- STOP_SLEEPING -> WAKE
- SET_BED_OCCUPATION_STATE
- MODIFY_WAKE_UP_POSITION

ServerPlayerEvents
+ (copy from ServerLivingEntityEvents) ALLOW_DAMAGE
+ (copy from ServerLivingEntityEvents) AFTER_DAMAGE -> DAMAGE_POST
- ALLOW_DEATH
+ (copy from ServerLivingEntityEvents) AFTER_DEATH -> DEATH_POST
- AFTER_RESPAWN -> RESPAWN_POST
- COPY_FROM

+ ServerAdvancementEvents
+ COMPLETE (When completing an advancement)
+ PROGRESS (When progressing in a advancement with multiple critera, e.g. Hot Tourist Destination)
+ REVOKE (When getting an advancement revoked)

ServerChunkEvents
- CHUNK_LOAD
- CHUNK_UNLOAD

ServerBlockEntityEvents
- BLOCK_ENTITY_LOAD -> ENTITY_LOAD
- BLOCK_ENTITY_UNLOAD -> ENTITY_UNLOAD

ServerEntityWorldChangeEvents -> ServerEntityLevelChangeEvents
- AFTER_ENTITY_CHANGE_WORLD -> ENTITY_LEVEL_CHANGE_POST
- AFTER_PLAYER_CHANGE_WORLD -> PLAYER_LEVEL_CHANGE_POST

ServerWorldEvents -> ServerLevelEvents
- LOAD
- UNLOAD

ModifyItemAttributeModifiersCallback -> ItemAttributeModifiersEvent
- EVENT -> MODIFY

UseBlockCallback -> UseBlockEvent
- EVENT

UseItemCallback -> UseItemEvent
- EVENT

UseEntityCallback -> EntityInteractEvent
- EVENT

AttackBlockCallback -> AttackBlockEvent
- EVENT

AttackEntityCallback -> AttackEntityEvent
- EVENT

PlayerBlockBreakEvents -> PlayerBlockEvents
- BEFORE -> BREAK_PRE
- AFTER -> BREAK_POST
- CANCELED -> BREAK_CANCEL

CommonLifecycleEvents
- TAGS_LOADED

ClientPlayConnectionEvents -> ClientConnectionEvents
- JOIN -> JOIN_SERVER
- DISCONNECT -> LEAVE_SERVER
- INIT

ServerPlayConnectionEvents -> ServerConnectionEvents
- JOIN -> JOIN_SERVER
- DISCONNECT -> LEAVE_SERVER
- INIT

ServerMessageEvents
- ALLOW_CHAT_MESSAGE -> ALLOW_CHAT
- ALLOW_COMMAND_MESSAGE -> ALLOW_COMMAND
- ALLOW_GAME_MESSAGE -> ALLOW_SYSTEM
- CHAT_MESSAGE -> MESSAGE_SENT
- COMMAND_MESSAGE -> COMMAND_EXECUTED
- GAME_MESSAGE -> SYSTEM_BROADCASTED

EntityTrackingEvents
- START_TRACKING -> START
- STOP_TRACKING -> STOP

*/