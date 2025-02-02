/*
 * Copyright [2013-2021], Alibaba Group Holding Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.polardbx.common.eventlogger;

import com.alibaba.polardbx.common.properties.DynamicConfig;
import com.alibaba.polardbx.common.utils.logger.Logger;
import com.alibaba.polardbx.common.utils.logger.LoggerFactory;

public class EventLogger {

    private final static Logger logger = LoggerFactory.getLogger("event", true);

    private final static String LOG_FORMAT = "%s %s %s";

    public static void log(EventType type, String msg) {
        if (EventType.isTrxEvent(type) && !DynamicConfig.getInstance().isEnableTrxEventLog()) {
            return;
        }
        String logContent = String.format(LOG_FORMAT, type.getLevel().name(), type.name(), msg);
        logger.info(logContent);
    }
}
