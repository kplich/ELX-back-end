package kplich.backend.services.items

import kplich.backend.data.ItemTestData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * Data for test are loaded from script resources/data-h2.sql.
 */
@SpringBootTest
abstract class ItemTest : ItemTestData() {
    @Autowired
    protected lateinit var itemService: ItemService


}
