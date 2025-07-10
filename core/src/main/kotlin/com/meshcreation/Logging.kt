package com.meshcreation

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx

class Logging {

    companion object {
        fun normal(s: String){
            Gdx.app.log("MESSAGE: ", s)
        }

        fun debug(s: String){
            Gdx.app.logLevel = LOG_DEBUG
            Gdx.app.debug("DEBUG:", s)
        }

        fun error(s: String){
            Gdx.app.error("ERROR: ", s)
        }

    }
}
