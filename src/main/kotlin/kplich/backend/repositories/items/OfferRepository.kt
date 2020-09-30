package kplich.backend.repositories.items

import kplich.backend.entities.conversation.offer.Offer
import org.springframework.data.jpa.repository.JpaRepository

interface OfferRepository : JpaRepository<Offer, Long>
