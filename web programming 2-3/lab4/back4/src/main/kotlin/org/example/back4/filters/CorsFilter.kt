package org.example.back4.filters

import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerResponseContext
import jakarta.ws.rs.container.ContainerResponseFilter
import jakarta.ws.rs.ext.Provider

@Provider
class CorsFilter : ContainerResponseFilter {
    override fun filter(requestContext: ContainerRequestContext, responseContext: ContainerResponseContext) {
        responseContext.headers.add("Access-Control-Allow-Origin", "http://localhost:4200")
        responseContext.headers.add("Access-Control-Allow-Headers", "Origin, Content-Type, Authorization")
        responseContext.headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
    }
}
