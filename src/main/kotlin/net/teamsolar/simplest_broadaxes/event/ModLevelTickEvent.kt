package net.teamsolar.simplest_broadaxes.event

import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

// Only used on the server
class ModLevelTickEvent {
    val queuedBroadaxeTasks = mutableMapOf<World, TaskLevelContainer<BroadaxeTask>>()
    class TaskLevelContainer<T: Task> {
        val list = mutableListOf<T>()
        val blocksBeingMined = mutableSetOf<BlockPos>()
    }
    interface Task {
        fun progress()
        fun isFinished(): Boolean
    }
    abstract class BroadaxeTask(val level: World, val player: ServerPlayerEntity, val position: BlockPos): Task
    // ...
    public fun onLevelTick(level: World) {
        val container = queuedBroadaxeTasks.get(level)
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
    public fun addTask(level: World, task: BroadaxeTask) {
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