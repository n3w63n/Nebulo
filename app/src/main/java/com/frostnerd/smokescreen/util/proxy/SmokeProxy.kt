package com.frostnerd.smokescreen.util.proxy

import com.frostnerd.dnstunnelproxy.DnsPacketProxy
import com.frostnerd.dnstunnelproxy.QueryListener
import com.frostnerd.dnstunnelproxy.SimpleDnsCache
import com.frostnerd.smokescreen.log
import com.frostnerd.smokescreen.service.DnsVpnService
import com.frostnerd.vpntunnelproxy.FutureAnswer
import org.minidns.dnsmessage.DnsMessage

/**
 * Copyright Daniel Wolf 2018
 * All rights reserved.
 * Code may NOT be used without proper permission, neither in binary nor in source form.
 * All redistributions of this software in source code must retain this copyright header
 * All redistributions of this software in binary form must visibly inform users about usage of this software
 *
 * development@frostnerd.com
 */

class SmokeProxy(dnsHandle: ProxyHandler, vpnService: DnsVpnService, val cache: SimpleDnsCache?) :
    DnsPacketProxy(listOf(dnsHandle), vpnService, cache, queryListener = object:QueryListener {
        override suspend fun onDeviceQuery(questionMessage: DnsMessage) {
            vpnService.log("Query from device: $questionMessage")
        }

        override suspend fun onQueryResponse(responseMessage: DnsMessage, fromUpstream: Boolean) {
            vpnService.log("Response to device: $responseMessage")
        }

    }) {


    override suspend fun informFailedRequest(request: FutureAnswer) {
        super.informFailedRequest(request)
        vpnService.log("Query from ${request.time} failed.")
    }
}