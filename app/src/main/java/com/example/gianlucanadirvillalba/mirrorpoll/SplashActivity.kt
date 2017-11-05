package com.example.gianlucanadirvillalba.mirrorpoll

import android.content.Intent
import com.daimajia.androidanimations.library.Techniques
import com.viksaa.sssplash.lib.activity.AwesomeSplash
import com.viksaa.sssplash.lib.cnst.Flags
import com.viksaa.sssplash.lib.model.ConfigSplash

/**
 * Created by gianlucanadirvillalba on 19/08/2017.
 */
class SplashActivity : AwesomeSplash()
{
    override fun initSplash(configSplash: ConfigSplash?)
    {
        //To change body of created functions use File | Settings | File Templates.
        //Customize Circular Reveal
        configSplash?.backgroundColor = R.color.colorPrimaryDark//any color you want form colors.xml
        configSplash?.animCircularRevealDuration = 1500 //int ms
        configSplash?.revealFlagX = Flags.REVEAL_RIGHT  //or Flags.REVEAL_LEFT
        configSplash?.revealFlagY = Flags.REVEAL_BOTTOM //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash?.logoSplash = R.mipmap.ic_launcher_round //or any other drawable
        configSplash?.animLogoSplashDuration = 1000 //int ms
        configSplash?.animLogoSplashTechnique = Techniques.FadeIn //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        //Customize Path

        /*configSplash?.pathSplash = "/app/src/main/res/mipmap-hdpi/ic-launcher.png" //set path String
        configSplash?.originalHeight = 400 //in relation to your svg (path) resource
        configSplash?.originalWidth = 400 //in relation to your svg (path) resource
        configSplash?.animPathStrokeDrawingDuration = 3000
        configSplash?.pathSplashStrokeSize = 3 //I advise value be <5
        configSplash?.pathSplashStrokeColor = R.color.colorAccent //any color you want form colors.xml
        configSplash?.animPathFillingDuration = 3000
        configSplash?.pathSplashFillColor = R.color.colorPrimaryDark //path object filling color*/


        //Customize Title
        configSplash?.titleSplash="Rankit Mirror"
        configSplash?.titleTextColor = R.color.colorWhite
        configSplash?.titleTextSize = 30f //float value
        configSplash?.animTitleDuration = 1500
        configSplash?.animTitleTechnique = Techniques.BounceInDown
        //configSplash?.titleFont = "fonts/myfont.ttf" //provide string to your font located in assets/fonts/


    }

    override fun animationsFinished()
    {
        //To change body of created functions use File | Settings | File Templates.
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}