package kplich.backend.services.conversations

import kplich.backend.conversation.services.ConversationService
import kplich.backend.data.ConversationTestData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
@Sql("/item-conversation-data.sql")
abstract class ConversationTest : ConversationTestData() {

    @Autowired
    protected lateinit var conversationService: ConversationService
}
