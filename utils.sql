CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX idx_brew_method_title_trgm
    ON brew_method
 USING gin(title gin_trgm_ops);
