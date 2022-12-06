package com.skyyaros.weatherultimate.ui

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.skyyaros.weatherultimate.databinding.HelloFragmentBinding
import kotlinx.coroutines.delay

class HelloFragment: Fragment() {
    private var _bind: HelloFragmentBinding? = null
    private val bind get() = _bind!!
    private var appBarCallbacks: AppBarCallbacks? = null
    private lateinit var message_anim: ValueAnimator

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appBarCallbacks = context as AppBarCallbacks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _bind = HelloFragmentBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onStart() {
        super.onStart()
        appBarCallbacks!!.hideAppbar()
        message_anim = ValueAnimator.ofObject(
            GradientArgbEvaluator,
            intArrayOf(Color.MAGENTA, Color.MAGENTA, Color.MAGENTA),
            intArrayOf(Color.MAGENTA, Color.MAGENTA, Color.BLUE),
            intArrayOf(Color.MAGENTA, Color.BLACK, Color.BLACK),
            intArrayOf(Color.BLUE, Color.BLACK, Color.RED),
            intArrayOf(Color.BLACK, Color.RED, Color.GREEN),
            intArrayOf(Color.BLACK, Color.GREEN, Color.BLUE),
            intArrayOf(Color.GREEN, Color.BLUE, Color.CYAN),
            intArrayOf(Color.BLUE, Color.CYAN, Color.YELLOW),
            intArrayOf(Color.CYAN, Color.YELLOW, Color.MAGENTA)
        ).apply {
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            duration = 3000
            addUpdateListener {
                val shader = LinearGradient(
                    0F, 0F,
                    bind.message.paint.measureText(bind.message.text.toString()),
                    bind.message.textSize,
                    it.animatedValue as IntArray,
                    null,
                    Shader.TileMode.CLAMP
                )
                bind.message.paint.shader = shader
                bind.message.invalidate()
            }
            start()
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            delay(3000)
            message_anim.cancel()
            val action = HelloFragmentDirections.actionHelloFragmentToMainFragment()
            findNavController().navigate(action)
        }
    }

    override fun onStop() {
        super.onStop()
        message_anim.cancel()
    }

    override fun onDestroyView() {
        _bind = null
        super.onDestroyView()
    }

    override fun onDetach() {
        appBarCallbacks = null
        super.onDetach()
    }

    object GradientArgbEvaluator: TypeEvaluator<IntArray> {
        private val argbEvaluator = ArgbEvaluator()
        override fun evaluate(fraction: Float, startValue: IntArray, endValue: IntArray): IntArray {
            return startValue.mapIndexed { index, item ->
                argbEvaluator.evaluate(fraction, item, endValue[index]) as Int
            }.toIntArray()
        }
    }
}