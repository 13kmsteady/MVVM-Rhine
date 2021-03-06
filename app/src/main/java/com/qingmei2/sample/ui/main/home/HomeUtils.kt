package com.qingmei2.sample.ui.main.home

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.qingmei2.rhine.functional.Consumer
import com.qingmei2.sample.R
import com.qingmei2.sample.entity.ReceivedEvent
import com.qingmei2.sample.entity.Type
import com.qingmei2.sample.utils.TimeConverter

object HomeUtils {

    @JvmStatic
    fun eventTimeToString(eventTime: String?): String =
            TimeConverter.tramsTimeAgo(eventTime)

    @JvmStatic
    fun eventTypeToDrawable(view: ImageView, eventType: Type): Drawable? =
            when (eventType) {
                Type.WatchEvent ->
                    ContextCompat.getDrawable(view.context, R.mipmap.ic_star_yellow_light)
                Type.CreateEvent, Type.ForkEvent, Type.PushEvent ->
                    ContextCompat.getDrawable(view.context, R.mipmap.ic_fork_green_light)
                else ->
                    throw RuntimeException("$eventType can't be displayed.")
            }

    @JvmStatic
    fun eventTitle(view: TextView, data: ReceivedEvent,
                   actorEvent: Consumer<String>, repoEvent: Consumer<String>): CharSequence {
        val actor = data.actor.displayLogin
        val action = when (data.type) {
            Type.WatchEvent -> "starred"
            Type.CreateEvent -> "created"
            Type.ForkEvent -> "forked"
            Type.PushEvent -> "pushed"
            else -> throw RuntimeException("${data.type} can't be displayed.")
        }
        val repo = data.repo.name

        val actorSpan = object : ClickableSpan() {
            override fun onClick(widget: View?) {
                actorEvent.accept(data.actor.url)
            }
        }
        val repoSpan = object : ClickableSpan() {
            override fun onClick(widget: View?) {
                repoEvent.accept(data.repo.url)
            }
        }
        val styleSpan = StyleSpan(Typeface.BOLD)
        val styleSpan2 = StyleSpan(Typeface.BOLD)

        view.movementMethod = LinkMovementMethod.getInstance()

        return SpannableStringBuilder().apply {
            append("$actor $action $repo")
            setSpan(actorSpan, 0, actor.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(styleSpan, 0, actor.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(repoSpan,
                    actor.length + action.length + 2,
                    actor.length + action.length + repo.length + 2,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(styleSpan2,
                    actor.length + action.length + 2,
                    actor.length + action.length + repo.length + 2,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

}