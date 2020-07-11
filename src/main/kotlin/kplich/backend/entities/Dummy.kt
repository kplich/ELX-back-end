package kplich.backend.entities

import javax.persistence.*

@Entity
@Table(name = "dummy")
class Dummy(
        var stringAttribute: String,
        var intAttribute: Int,
        @Id @GeneratedValue var id: Long? = null
)

@Entity
@Table(name = "dummy_child")
class DummyChild(
        var stringAttribute: String,
        @ManyToOne() var dummyParent: Dummy,
        @Id @GeneratedValue var id: Long? = null
)
