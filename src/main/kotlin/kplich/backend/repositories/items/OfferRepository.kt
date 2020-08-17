package kplich.backend.repositories.items

import kplich.backend.entities.Offer
import org.springframework.data.jpa.repository.JpaRepository

interface OfferRepository : JpaRepository<Offer, Long>
