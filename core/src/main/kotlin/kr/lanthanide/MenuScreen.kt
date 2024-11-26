package kr.lanthanide

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy
import com.badlogic.gdx.graphics.g3d.decals.Decal
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.StretchViewport


class MenuScreen(private val game: SwingPhone) : Screen {
    private val stage by lazy { Stage(StretchViewport(1600f, 900f), game.batch) }
    private val camera3d by lazy { PerspectiveCamera(67f, 1600f, 900f) }
    private val inputController3d by lazy { MenuCamController(camera3d) }
    private val decalBatch by lazy { DecalBatch(CameraGroupStrategy(camera3d)) }
    private val gameDecalList = ArrayList<Decal>()
    private val startButton = TextButton("Start", skin)

//    private val particleSystem by lazy { ParticleSystem() }
//    private val pointSpriteBatch by lazy { PointSpriteParticleBatch() }
//    private val assetManager by lazy { AssetManager() }

    init {
        // 2d
        val rootTable = Table()
        rootTable.setFillParent(true)
        rootTable.pad(50f)
        stage.addActor(rootTable)

        val profile = ImageButton(TextureRegionDrawable(Texture("icon/profile.png"))).apply {
        }
        val settingsButton = TextButton("Settings", skin)
        rootTable.add(profile).align(Align.left).expandX().size(50f, 50f)
        rootTable.add(settingsButton).align(Align.right).expandX()

        rootTable.row()

        val spacer = Widget()
        rootTable.add(spacer).align(Align.center).expand().colspan(100)

        rootTable.row()

        rootTable.add(startButton).align(Align.center).size(200f, 100f).pad(100f, 0f, 100f, 0f).colspan(100)

        rootTable.row()

        val optionsButton = TextButton("Options", skin).apply {
            addListener(object : InputListener() {

            })
        }
        rootTable.add(optionsButton)

        val quitButton = TextButton("Quit", skin).apply {
            addListener(object : InputListener() {
                override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                    Gdx.app.exit()
                    return true
                }
            })
        }
        rootTable.add(quitButton)

//        rootTable.debug = true

        // 3d
        camera3d.position.set(0f, 0f, 20f)
        camera3d.lookAt(0f, 0f, 0f)
        camera3d.near = 0.1f
        camera3d.far = 1000f
        camera3d.update()

        updateGameList()
    }

    fun updateGameList() {
        // TODO: After implementing 3rd party game importer, make it call this method.
        for (i in 0 until GameListManager.gameList.size) {
            val gameDecal = Decal.newDecal(
                6f,
                6f,
                TextureRegion(Texture("test.png").apply {
                    setFilter(
                        Texture.TextureFilter.Linear,
                        Texture.TextureFilter.Linear
                    )
                })
            )
            gameDecal.position = Vector3(0f, 0f, 10f)
            gameDecal.position.rotate(Vector3.Y, (360 / GameListManager.gameList.size * i).toFloat())
            gameDecalList.add(gameDecal)
        }
    }

    override fun show() {
        println("show")
        Gdx.input.inputProcessor = InputMultiplexer(stage, inputController3d)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        inputController3d.tick(delta)


        for (decal in gameDecalList) {
            decalBatch.add(decal)
            decal.lookAt(decal.position.cpy().add(camera3d.position), Vector3.Y)
        }
        decalBatch.flush()

        startButton.isVisible = inputController3d.parked
        stage.act(delta)
        stage.draw()

    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun hide() {
    }

    override fun dispose() {
        stage.dispose()
    }
}
