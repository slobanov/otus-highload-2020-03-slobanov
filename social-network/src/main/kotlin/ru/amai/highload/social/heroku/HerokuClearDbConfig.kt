package ru.amai.highload.social.heroku

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.net.URI
import java.util.concurrent.TimeUnit.SECONDS
import javax.sql.DataSource

@Configuration
@Profile("heroku")
class HerokuClearDbConfig {

    @Bean
    fun dataSource(properties: SocialDbProperties): DataSource {
        val dbUri = URI(System.getenv("CLEARDB_DATABASE_URL"))
        val username: String = dbUri.userInfo.split(":")[0]
        val password: String = dbUri.userInfo.split(":")[1]
        val dbUrl = "jdbc:mysql://" + dbUri.host + dbUri.path

        val config = HikariConfig().also {
            it.username = username
            it.password = password
            it.jdbcUrl = dbUrl
            it.maxLifetime = SECONDS.toMillis(properties.maxLifeTimeSeconds)
            it.maximumPoolSize = properties.poolSize
        }

        return HikariDataSource(config)
    }
}

@ConstructorBinding
@ConfigurationProperties("social.db")
@Profile("heroku")
data class SocialDbProperties(
    val poolSize: Int,
    val maxLifeTimeSeconds: Long
)
