/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.linecorp.armeria.common.reactivestreams;

import com.linecorp.armeria.common.util.Exceptions;

public final class CancelledSubscriptionException extends RuntimeException {

    private static final long serialVersionUID = -7815958463104921571L;

    private static final CancelledSubscriptionException INSTANCE =
            Exceptions.clearTrace(new CancelledSubscriptionException());

    public static CancelledSubscriptionException get() {
        return Exceptions.isVerbose() ? new CancelledSubscriptionException() : INSTANCE;
    }

    private CancelledSubscriptionException() {}
}