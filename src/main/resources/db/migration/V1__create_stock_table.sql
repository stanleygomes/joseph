CREATE TABLE stocks (
    id UUID PRIMARY KEY,
    ticker VARCHAR(255) NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    deleted_at TIMESTAMPTZ,
    CONSTRAINT uk_ticker UNIQUE (ticker)
);
