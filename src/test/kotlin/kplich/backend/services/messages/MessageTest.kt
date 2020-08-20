package kplich.backend.services.messages

import kplich.backend.data.MessageTestData
import kplich.backend.services.items.MessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
abstract class MessageTest : MessageTestData() {

    @Autowired
    protected lateinit var messageService: MessageService
}
