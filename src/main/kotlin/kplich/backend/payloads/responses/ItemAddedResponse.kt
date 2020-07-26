package kplich.backend.payloads.responses

import java.time.LocalDateTime

class ItemAddedResponse(
        val title: String = "",
        val description: String = "",
        val price: Float = 0F,
        val addedBy: String = "",
        val added: LocalDateTime = LocalDateTime.now(),
        val category: String = "",
        val usedStatus: String = "",
        val photoUrls: List<String> = emptyList(),
        val closed: LocalDateTime? = LocalDateTime.MAX)
