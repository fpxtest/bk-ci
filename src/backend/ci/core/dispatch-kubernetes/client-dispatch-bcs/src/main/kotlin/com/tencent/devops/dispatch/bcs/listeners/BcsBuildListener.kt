/*
 * Tencent is pleased to support the open source community by making BK-CI 蓝鲸持续集成平台 available.
 *
 * Copyright (C) 2019 THL A29 Limited, a Tencent company.  All rights reserved.
 *
 * BK-CI 蓝鲸持续集成平台 is licensed under the MIT license.
 *
 * A copy of the MIT License is included in this file.
 *
 *
 * Terms of the MIT License:
 * ---------------------------------------------------
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.tencent.devops.dispatch.bcs.listeners

import com.tencent.devops.common.dispatch.sdk.listener.BuildListener
import com.tencent.devops.common.dispatch.sdk.pojo.DispatchMessage
import com.tencent.devops.dispatch.kubernetes.builds.services.DispatchBuildService
import com.tencent.devops.dispatch.kubernetes.pojo.DispatchEnumType
import com.tencent.devops.dispatch.pojo.enums.JobQuotaVmType
import com.tencent.devops.process.pojo.mq.PipelineAgentShutdownEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class BcsBuildListener @Autowired constructor(
    private val dispatchBuildService: DispatchBuildService
) : BuildListener {

    override fun getShutdownQueue(): String {
        return ".bcs.public"
    }

    override fun getStartupDemoteQueue(): String {
        return ".bcs.public.demote"
    }

    override fun getStartupQueue(): String {
        return ".bcs.public"
    }

    override fun getVmType(): JobQuotaVmType? {
        return JobQuotaVmType.DOCKER_BCS
    }

    override fun onStartup(dispatchMessage: DispatchMessage) {
        startUp(dispatchMessage)
    }

    override fun onStartupDemote(dispatchMessage: DispatchMessage) {
        startUp(dispatchMessage)
    }

    private fun startUp(dispatchMessage: DispatchMessage) {
        val retry = dispatchBuildService.preStartUp(DispatchEnumType.BCS, dispatchMessage)
        if (retry) {
            retry()
        } else {
            dispatchBuildService.startUp(DispatchEnumType.BCS, dispatchMessage, 0)
        }
    }

    override fun onShutdown(event: PipelineAgentShutdownEvent) {
        dispatchBuildService.doShutdown(DispatchEnumType.BCS, event)
    }
}
