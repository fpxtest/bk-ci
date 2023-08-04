package com.tencent.devops.auth.resources

import com.tencent.devops.auth.api.oauth2.Oauth2ServiceEndpointResource
import com.tencent.devops.auth.pojo.Oauth2AccessTokenRequest
import com.tencent.devops.auth.pojo.vo.Oauth2AccessTokenVo
import com.tencent.devops.auth.service.oauth2.Oauth2EndpointService
import com.tencent.devops.common.api.pojo.Result
import com.tencent.devops.common.web.RestResource

@RestResource
class Oauth2ServiceEndpointResourceImpl constructor(
    private val endpointService: Oauth2EndpointService
) : Oauth2ServiceEndpointResource {
    override fun getAccessToken(
        clientId: String,
        clientSecret: String,
        accessTokenRequest: Oauth2AccessTokenRequest
    ): Result<Oauth2AccessTokenVo?> {
        return Result(
            endpointService.getAccessToken(
                clientId = clientId,
                clientSecret = clientSecret,
                accessTokenRequest = accessTokenRequest
            )
        )
    }

    override fun verifyAccessToken(
        clientId: String,
        clientSecret: String,
        accessToken: String
    ): Result<String> {
        return Result(
            endpointService.verifyAccessToken(
                clientId = clientId,
                clientSecret = clientSecret,
                accessToken = accessToken
            )
        )
    }
}
