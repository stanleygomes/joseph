package com.nazarethlabs.joseph.stock

fun Stock.toResponse(): StockResponse =
    StockResponse(
        id = this.id!!,
        ticker = this.ticker,
        companyName = this.companyName
    )
