package net.justmili.libs.v1.event;

/*
Events to replicate:

ClientTickEvents
- START_CLIENT_TICK -> CLIENT_PRE
- END_CLIENT_TICK -> CLIENT_POST
- START_WORLD_TICK -> WORLD_PRE
- END_WORLD_TICK -> WORLD_POST

ClientChunkEvents
- CHUNK_LOAD
- CHUNK_UNLOAD

ClientEntityEvents
- ENTITY_LOAD
- ENTITY_UNLOAD

ClientBlockEntityEvents
- BLOCK_ENTITY_LOAD -> ENTITY_LOAD
- BLOCK_ENTITY_UNLOAD -> ENTITY_UNLOAD

ClientPickBlockGatherCallback -> ClientPickBlockEvents
- EVENT -> GATHER
ClientPickBlockApplyCallback -> ClientPickBlockEvents
- EVENT -> APPLY

ScreenEvents
- BEFORE_INIT -> INIT_PRE
- AFTER_INIT -> INIT_POST

TooltipComponentCallback -> TooltipRenderEvent
- EVENT -> RENDER

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

CommonLifecycleEvents
- TAGS_LOADED

EntityTrackingEvents
- START_TRACKING -> START
- STOP_TRACKING -> STOP

ServerChunkEvents
- CHUNK_LOAD
- CHUNK_UNLOAD

ServerEntityEvents
- ENTITY_LOAD
- ENTITY_UNLOAD
- EQUIPMENT_CHANGE

ServerEntityCombatEvents -> ServerCombatEvents
- AFTER_KILLED_OTHER_ENTITY -> KILLED_OTHER_ENTITY_POST

ServerBlockEntityEvents
- BLOCK_ENTITY_LOAD -> ENTITY_LOAD
- BLOCK_ENTITY_UNLOAD -> ENTITY_UNLOAD

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

ServerEntityWorldChangeEvents -> ServerEntityLevelChangeEvents
- AFTER_ENTITY_CHANGE_WORLD -> ENTITY_LEVEL_CHANGE_POST
- AFTER_PLAYER_CHANGE_WORLD -> PLAYER_LEVEL_CHANGE_POST

ServerWorldEvents -> ServerLevelEvents
- LOAD
- UNLOAD

ServerPlayerEvents
+ (copy from ServerLivingEntityEvents) ALLOW_DAMAGE
+ (copy from ServerLivingEntityEvents) AFTER_DAMAGE -> DAMAGE_POST
- ALLOW_DEATH
+ (copy from ServerLivingEntityEvents) AFTER_DEATH -> DEATH_POST
- AFTER_RESPAWN -> RESPAWN_POST
- COPY_FROM

ServerPlayConnectionEvents -> ConnectionEvents
- JOIN -> JOIN_SERVER
- DISCONNECT -> LEAVE_SERVER
- INIT

ModifyItemAttributeModifiersCallback -> ItemAttributeModifiersEvent
- EVENT -> MODIFY

UseBlockCallback -> UseBlockEvent
- EVENT -> USE

UseItemCallback -> UseItemEvent
- EVENT -> USE

UseEntityCallback -> EntityInteractEvent
- EVENT

AttackBlockCallback -> AttackBlockEvent
- EVENT

AttackEntityCallback -> AttackEntityEvent
- EVENT

PlayerBreakBlockEvents -> PlayerBlockEvents
- BEFORE -> BREAK_PRE
- AFTER -> BREAK_POST
- CANCELED -> BREAK_CANCEL
 */