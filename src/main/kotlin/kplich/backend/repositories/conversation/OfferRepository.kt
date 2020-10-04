package kplich.backend.repositories.conversation

import kplich.backend.entities.conversation.offer.Offer
import org.springframework.data.jpa.repository.JpaRepository

interface OfferRepository : JpaRepository<Offer, Long>
