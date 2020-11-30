package kplich.backend.conversation.repositories

import kplich.backend.conversation.entities.offer.Offer
import org.springframework.data.jpa.repository.JpaRepository

interface OfferRepository : JpaRepository<Offer, Long>
