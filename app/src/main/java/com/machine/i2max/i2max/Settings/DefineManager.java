package com.machine.i2max.i2max.Settings;

/**
 * Created by stories2 on 2017. 8. 9..
 */

public class DefineManager {
    public static final String
            SERVER_DOMAIN_NAME = "211.249.49.198:5000",
            COMMUNICATE_CONTENT_TYPE = "application/json",
            APP_NAME = "i2max",

            BUNDLE_STATUS = "status",
            BUNDLE_DATE = "date",
            BUNDLE_RESULT = "result",

            STATUS_WORKING = "Working",
            STATUS_DONE = "Done";

    public static final int
            LOG_LEVEL_VERBOSE = 0,
            LOG_LEVEL_DEBUG = 1,
            LOG_LEVEL_INFO = 2,
            LOG_LEVEL_WARN = 3,
            LOG_LEVEL_ERROR = 4,

            ZERO = 0,
            NOT_AVAILABLE = -1,
            VISIBLE_UPLOADING_PROGRESS = 1,
            INVISIBLE_UPLOADING_PROGRESS = 0,
            DISABLE_PULLING_PROGRESS = 2,
            FORECAST_DATA_RECEIVED = 3,
            PRINT_PROCESS_NOT_READY = 4,
            FORECAST_DATA_RECEIVED_ERROR = 5,

            CONNECTION_SUCCESSFULL = 200;
}
