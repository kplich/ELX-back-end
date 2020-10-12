package kplich.backend.services.items

import kplich.backend.data.ItemTestData
import kplich.backend.items.services.ItemService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * Data for test are loaded from script resources/data.sql.
 */
@SpringBootTest
abstract class ItemTest : ItemTestData() {
    @Autowired
    protected lateinit var itemService: ItemService


}
