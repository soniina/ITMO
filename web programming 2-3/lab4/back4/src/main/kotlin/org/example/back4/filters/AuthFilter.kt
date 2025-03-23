package org.example.back4.filters

import jakarta.ws.rs.NameBinding
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.Provider
import org.example.back4.utils.TokenUtils

@NameBinding
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Secured

@Provider
@Secured
class AuthFilter : ContainerRequestFilter {
    override fun filter(requestContext: ContainerRequestContext) {
        val authHeader = requestContext.getHeaderString("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build())
        }

        val token = authHeader.substring("Bearer ".length)
        if (!TokenUtils.validateToken(token)) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build())
        }
    }
}
