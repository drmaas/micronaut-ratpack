package io.micronaut.ratpack.server

import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.http.HttpRequest
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.RxHttpClient
import io.micronaut.runtime.server.EmbeddedServer
import ratpack.func.Action
import ratpack.groovy.test.embed.GroovyEmbeddedApp
import ratpack.handling.Chain
import ratpack.server.RatpackServer
import ratpack.test.embed.EmbeddedApp
import spock.lang.Shared
import spock.lang.Specification

class DualServerSpec extends Specification {

    @Shared
    private EmbeddedServer server
    @Shared
    private ApplicationContext context = null

    def setupSpec() {
        server = ApplicationContext.run(EmbeddedServer, "test")
        context = server.applicationContext
    }

    def cleanupSpec() {
        server.close()
    }

    def "assert that micronaut netty and ratpack servers run simultaneously"() {
        given:
        def mnClient = context.createBean(RxHttpClient, server.URL).toBlocking()
        def rpServer = context.getBean(RatpackServer)
        def rpClient = GroovyEmbeddedApp.from(EmbeddedApp.fromServer(rpServer)).httpClient

        when:
        def mnResult1 = mnClient.retrieve(HttpRequest.GET("/ctx/test1"))
        def mnResult2 = mnClient.retrieve(HttpRequest.GET("/ctx/test2"))

        then:
        mnResult1 == "test1"
        mnResult2 == "test2"

        when:
        def rpResult1 = rpClient.get("/ctx/test1")
        def rpResult2 = rpClient.get("/ctx/test2")

        then:
        rpResult1.body.text == "test1"
        rpResult2.body.text == "test2"

        and:
        server.port != rpServer.bindPort
    }

    @Controller
    protected static class MicronautController {

        @Get("test1")
        String test1() {
            "test1"
        }

        @Get("test2")
        String test2() {
            "test2"
        }
    }

    @Factory
    protected static class RatpackConfiguration {

        @Bean
        Action<Chain> test1() {
            { Chain chain ->
                chain.get("test1") {
                    it.response.send("test1")
                }
            }
        }

        @Bean
        Action<Chain> test2() {
            { Chain chain ->
                chain.get("test2") {
                    it.response.send("test2")
                }
            }
        }
    }
}
