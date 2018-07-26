package com.chenpeng.simpleToDo.utils

import org.greenrobot.eventbus.EventBus

class EventBusUtils {

    companion object {

        fun post(event: Any) {
            EventBus.getDefault().post(event)
        }

        fun registet(subscriber: Any) {
            if (!EventBus.getDefault().isRegistered(subscriber)) {
                EventBus.getDefault().register(subscriber)
            }
        }

        fun unregister(subscriber: Any) {
            if (EventBus.getDefault().isRegistered(subscriber)) {
                EventBus.getDefault().unregister(subscriber)
            }
        }
    }
}