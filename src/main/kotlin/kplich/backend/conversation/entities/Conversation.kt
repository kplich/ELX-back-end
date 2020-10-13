package kplich.backend.conversation.entities

import kplich.backend.authentication.entities.ApplicationUser
import kplich.backend.items.entities.Item
import javax.persistence.*

@Entity
@Table(
        name = "conversations",
        indexes = [Index(columnList = "interested_user_id, item_id", unique = true)]
)
data class Conversation(
        @OneToOne
        var interestedUser: ApplicationUser,

        @ManyToOne
        var item: Item,

        @OneToMany(mappedBy = "conversation")
        var messages: MutableList<Message>,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0
)