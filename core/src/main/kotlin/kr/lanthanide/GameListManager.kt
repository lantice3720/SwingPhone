package kr.lanthanide;

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds

object GameListManager {
    private var isWatching = false
    private val path = Paths.get(System.getProperty("user.dir") + "/")
    private val watchService = path.fileSystem.newWatchService()
    val gameList = List(4) { "테스트 게임!" }

    init {
        path.register(
            watchService,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY
        )
    }

    fun startWatching() {
        if (isWatching) return
        isWatching = true

        KtxAsync.launch(Dispatchers.IO) {
            while (isWatching) {
                val key = watchService.take()
                key.pollEvents().forEach {
                    println(it.context().toString())
                }
                key.reset()
            }
        }
    }

    fun stopWatching() {
        isWatching = false
    }
}
