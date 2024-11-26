package kr.lanthanide

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector3
import kotlin.math.*

class MenuCamController(private val camera: Camera) : InputProcessor {
    private var lastX = 0f
    private var lastY = 0f
    // unit: -10 rad/s
    private var deltaX = 0f
    private var deltaY = 0f
    private var isTouching = false
    private var parking = false
    var parked = true
    var index = 0

    fun tick(delta: Float) {


        if (isTouching) {
            parking = false
            // in control of player
            camera.position.rotateRad(Vector3.Y, -delta * deltaX / 10f)
            camera.lookAt(0f, 0f, 0f)
            camera.update()
            deltaX = 0f
            deltaY = 0f

            parked = false
        } else if (deltaX.absoluteValue <= 5 || parking) {
            if (parked) return
            parking = true
            // parking sequence
            val indexSize = 2 * PI.toFloat() / GameListManager.gameList.size
            var theta = acos(Vector3(0f, 0f, -1f).dot(camera.direction) / camera.direction.len())
            if (camera.position.x < 0) theta = -theta + 2 * PI.toFloat()
            theta += indexSize / 2

            val offsetFromNearest = theta % indexSize - indexSize / 2

            if (offsetFromNearest.absoluteValue < 0.01) {
                deltaX = 0f
                camera.position.rotateRad(Vector3.Y, -offsetFromNearest)

                index = floor(theta / indexSize).toInt()
                parked = true
            } else {
                deltaX += (offsetFromNearest) / 2
                camera.position.rotateRad(Vector3.Y, -delta * deltaX / 10f)

                parked = false
            }

            camera.lookAt(0f, 0f, 0f)
            camera.update()
        } else {
            parking = false
            // enough speed to rotate
            camera.position.rotateRad(Vector3.Y, -delta * deltaX / 10f)
            camera.lookAt(0f, 0f, 0f)
            camera.update()
            deltaX *= 0.95f
            deltaY *= 0.95f
            if (deltaX.absoluteValue <= 0.001f) deltaX = 0f
            if (deltaY.absoluteValue <= 0.001f) deltaY = 0f

            parked = false
        }

    }

    override fun keyDown(keycode: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        lastX = screenX.toFloat()
        lastY = screenY.toFloat()
        isTouching = true

        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        lastX = 0f
        lastY = 0f
        isTouching = false

        return true
    }

    override fun touchCancelled(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        if (isTouching) {
            deltaX = screenX - lastX
            deltaY = screenY - lastY
            lastX = screenX.toFloat()
            lastY = screenY.toFloat()

            return true
        }

        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return false
    }
}
