package kplich.backend.services.items

import kplich.backend.data.ItemTestData
import kplich.backend.items.services.ItemService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

/**
 * Data for test are loaded from script resources/other-data.sql.
 */
@SpringBootTest
@Sql("/other-data.sql")
abstract class ItemTest : ItemTestData() {
    @Autowired
    protected lateinit var itemService: ItemService


}
