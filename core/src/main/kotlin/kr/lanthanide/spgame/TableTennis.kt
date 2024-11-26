package kr.lanthanide.spgame

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import kr.lanthanide.SPGame

class TableTennis: SPGame("TableTennis", TextureRegion(Texture("game/table_tennis/thumbnail.png").apply { setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear) })) {
    override fun show() {
    }

    override fun render(delta: Float) {
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun hide() {
    }

    override fun dispose() {
    }
}
