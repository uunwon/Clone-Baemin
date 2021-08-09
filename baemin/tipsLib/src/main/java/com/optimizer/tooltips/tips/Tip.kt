package com.optimizer.tooltips.tips

import android.content.Context
import android.view.View
import com.optimizer.tooltips.animations.AnimationComposer
import com.optimizer.tooltips.animations.BaseViewAnimator

interface Tip {

    fun getEnterAnimation(): AnimationComposer<BaseViewAnimator>?

    fun getExitAnimation(): AnimationComposer<BaseViewAnimator>?

    fun getTipView(): View

    fun calculatePosition()

    fun createCloneOfAnchorView(context: Context): View
}
