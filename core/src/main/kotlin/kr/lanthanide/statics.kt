package kr.lanthanide

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Skin

val skin by lazy { Skin(Gdx.files.internal("expee/expee-ui.json")) }
val swingPhone by lazy { SwingPhone() }
val menuScreen by lazy { MenuScreen(swingPhone) }
