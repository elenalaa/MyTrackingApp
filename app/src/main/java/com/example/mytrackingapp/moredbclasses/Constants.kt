package com.example.mytrackingapp.moredbclasses

object Constants {

    const val TRACKING_DATABASE_NAME = "tracking_database"
    const val REQUEST_CODE_LOCATION = 0

    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val ACTION_NAVIGATION_TO_MAP_TRACKING ="ACTION_NAVIGATION_TO_MAP_TRACKING"

    const val LOCATION_UPDATE_INTERVAL = 10000L
    const val FASTEST_LOCATION_INTERVAL = 4000L

    const val PERMISSION_LOCATION_REQUEST_CODE = 1
    const val PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE = 2
    const val NOTIFICATION_CHANNEL_ID = "tracker_notification_id"
    const val NOTIFICATION_CHANNEL_NAME = "tracker_notification"
    const val NOTIFICATION_ID = 3
    const val PENDING_INTENT_REQUEST_CODE = 4
}