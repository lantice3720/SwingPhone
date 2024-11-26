package kr.lanthanide

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.TextureRegion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.lanthanide.spgame.TableTennis
import ktx.async.KtxAsync
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds

object GameListManager {
    private var isWatching = false
    private val path = Paths.get(System.getProperty("user.dir") + "/")
    private val watchService = path.fileSystem.newWatchService()
    val gameList = ArrayList<SPGame>()

    init {
        path.register(
            watchService,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY
        )

        gameList.add(TableTennis())
        gameList.add(TableTennis())
        gameList.add(TableTennis())
        gameList.add(TableTennis())
        gameList.add(TableTennis())
        gameList.add(TableTennis())
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

abstract class SPGame(val title: String, val thumbnail: TextureRegion): Screen
