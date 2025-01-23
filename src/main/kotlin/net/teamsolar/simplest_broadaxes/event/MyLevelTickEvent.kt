package net.teamsolar.simplest_broadaxes.event

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.Level
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.tick.LevelTickEvent

// Only used on the server
class MyLevelTickEvent {
    val queuedBroadaxeTasks = mutableMapOf<Level, TaskLevelContainer<BroadaxeTask>>()
    class TaskLevelContainer<T: Task> {
        val list = mutableListOf<T>()
        val blocksBeingMined = mutableSetOf<BlockPos>()
    }
    interface Task {
        fun progress()
        fun isFinished(): Boolean
    }
    abstract class BroadaxeTask(val level: Level, val player: ServerPlayer, val position: BlockPos): Task
    @SubscribeEvent
    public fun onLevelTick(event: LevelTickEvent.Pre) {
        val container = queuedBroadaxeTasks.get(event.level)
        if(container == null) {
            return
        } else {
            val finishedTasks = mutableListOf<BroadaxeTask>()
            for(task in container.list) {
                task.progress()
                if(task.isFinished()) {
                    finishedTasks.add(task)
                }
            }
            for(task in finishedTasks) {
                container.list.remove(task)
            }
        }
    }
    public fun addTask(level: Level, task: BroadaxeTask) {
        if(queuedBroadaxeTasks.get(level) == null) {
            queuedBroadaxeTasks[level] = TaskLevelContainer()
        }
        val container = queuedBroadaxeTasks[level]!!
        val list = container.list
        val set = container.blocksBeingMined
        if(task.position in set) {
            return
        }
        list.add(task)
    }
}