package com.nazarethlabs.joseph.stock

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface StockRepository : JpaRepository<Stock, UUID>
