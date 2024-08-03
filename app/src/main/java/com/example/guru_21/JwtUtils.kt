package com.example.guru_21

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.util.Date
import java.security.Key

object JwtUtils {

    private val SECRET_KEY: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256) // 256비트 이상의 안전한 키 생성

    // 토큰 생성
    fun generateToken(userId: String): String {
        return Jwts.builder()
            .setSubject(userId)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간 유효
            .signWith(SECRET_KEY)
            .compact()
    }

    // 토큰 유효성 검사
    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    // 토큰에서 사용자 ID 추출
    fun getUserIdFromToken(token: String): String? {
        return try {
            val claims: Claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .body
            claims.subject
        } catch (e: Exception) {
            null
        }
    }
}

