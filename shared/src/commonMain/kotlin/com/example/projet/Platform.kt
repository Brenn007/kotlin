package com.example.projet

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform