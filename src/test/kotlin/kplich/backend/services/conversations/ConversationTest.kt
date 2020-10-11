package kplich.backend.services.conversations

import kplich.backend.data.ConversationTestData
import kplich.backend.conversation.services.ConversationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
abstract class ConversationTest : ConversationTestData() {

    @Autowired
    protected lateinit var conversationService: ConversationService
}
