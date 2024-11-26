package kr.lanthanide

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.video.VideoPlayerCreator
import ktx.async.KtxAsync


/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class SwingPhone : Game() {
    val batch by lazy { SpriteBatch() }
    private val player by lazy { VideoPlayerCreator.createVideoPlayer() }
    private var alpha = 0.0f
    val shape by lazy { ShapeRenderer() }
    var isLoading = true

    override fun create() {
        val fileHandle = Gdx.files.internal("logo.webm")
        if (fileHandle.exists()) {
            player.play(fileHandle)
        } else {
            println("Error: swingphone_logo.mp4 file not found")
        }
        KtxAsync.initiate()
        GameListManager.startWatching()
    }

    override fun render() {
        if (isLoading) {
            ScreenUtils.clear(0f, 0f, 0f, 1f)
            player.update()
            batch.begin()
            val frame = player.texture
            frame.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
            if (frame != null) {
                val ratio = Gdx.graphics.width.toFloat() / frame.width
                batch.color = Color(1f, 1f, 1f, 1 - alpha)
                batch.draw(frame, 0f, Gdx.graphics.height / 2 - frame.height * ratio / 2, Gdx.graphics.width.toFloat(), frame.height.toFloat() * ratio)
            }
            if (!player.isPlaying) {
//            shape.begin(ShapeRenderer.ShapeType.Filled)
//            shape.color = Color(1f, 1f, 1f, alpha)
//            shape.rect(0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
//            shape.end()
                alpha += 0.01f
            }
            if (alpha > 1f) {
                setScreen(menuScreen)
                isLoading = false
            }
            batch.end()
        }
        super.render()
    }

    override fun dispose() {
        println("dispose")
        batch.dispose()
        player.dispose()
        shape.dispose()
        menuScreen.dispose()
    }
}
