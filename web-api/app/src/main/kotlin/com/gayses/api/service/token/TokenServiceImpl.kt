package com.gayses.api.service.token

import com.security.jwt.service.TokenService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import java.util.function.Function

@Service
class TokenServiceImpl : TokenService {
    override fun extractEmail(token: String) =
        extractClaim(token) { obj -> obj.subject }

    override fun generateToken(user: UserDetails): String =
        generateToken(user, java.util.Map.of())

    override fun generateToken(user: UserDetails, extra: Map<String, Any?>?): String =
        Jwts.builder()
            .setClaims(extra)
            .setSubject(user.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 3)) // FIXME
            .signWith(signInKey, SignatureAlgorithm.HS256)
            .compact()

    private fun <T> extractClaim(token: String, claimsResolver: Function<Claims, T>) =
        try {
            val claims = extractAllClaims(token)
            claimsResolver.apply(claims)
        } catch (e: JwtException) {
            null
        }

    @Throws(JwtException::class)
    private fun extractAllClaims(token: String) =
        Jwts.parserBuilder()
            .setSigningKey(signInKey)
            .build()
            .parseClaimsJws(token)
            .body

    private val signInKey: Key
        get() = Decoders.BASE64.decode(SECRET_KEY).let {
            Keys.hmacShaKeyFor(it)
        }

    companion object {
        // FIXME: extract secret
        private const val SECRET_KEY =
            "QfKajL15gTjhSRsCmk9XEguFRdSleKaUX3jxJE0Sz0hw3Q0caVgrIW3onolfSuKKdqF46fXrNBWt5JKv3ZprgqgBgXug7Q6dEcQnjROfR6ThfECEsJSR09y2HLPtyGxebhMGyk8laLbXfs9YHfWRLUQzpzxIe1fi1xWdxxv5lD2ywRCuxUR2zuBBiUiuqLESKxDizy"
    }
}