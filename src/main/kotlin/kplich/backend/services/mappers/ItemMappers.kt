package kplich.backend.services.mappers

import kplich.backend.entities.*
import kplich.backend.payloads.requests.ItemAddRequest
import java.math.MathContext
import java.math.RoundingMode

fun ItemAddRequest.mapToItem(): Item {
    val title = this.title
    val description = this.description
    val price = this.price
            .toBigDecimal(MathContext(13)).setScale(4, RoundingMode.HALF_UP)
    val addedBy = ApplicationUser("", "", this.addedBy)
    val category = Category("", this.category)
    val usedStatus = UsedStatus.valueOf(this.usedStatus)

    val item = Item(title, description, price, addedBy, category, usedStatus, mutableListOf())

    val itemPhotos = this.photos.map {
        ItemPhoto(it, item)
    }.toMutableList()

    return item.apply {
        this.photos = itemPhotos
    }
}
