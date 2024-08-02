package com.example.guru_21

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.Date

object JwtUtils {

    private const val SECRET_KEY = "your_secret_key"

    // 토큰 생성
    fun generateToken(userId: String): String {
        return Jwts.builder()
            .setSubject(userId)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간 유효
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY.toByteArray())
            .compact()
    }

    // 토큰 유효성 검사
    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder()  // parserBuilder() 사용
                .setSigningKey(SECRET_KEY.toByteArray())
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
            val claims: Claims = Jwts.parserBuilder()  // parserBuilder() 사용
                .setSigningKey(SECRET_KEY.toByteArray())
                .build()
                .parseClaimsJws(token)
                .body
            claims.subject
        } catch (e: Exception) {
            null
        }
    }
}
