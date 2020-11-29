package kplich.backend.services.user

import kplich.backend.data.UserItemsData
import kplich.backend.user.services.UserItemsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@Sql("/user-item-data.sql")
abstract class UserItemsTest: UserItemsData() {
    @Autowired
    protected lateinit var userItemsService: UserItemsService
}