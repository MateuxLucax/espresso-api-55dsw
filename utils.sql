CREATE EXTENSION IF NOT EXISTS pg_trgm;


CREATE INDEX idx_brew_method_name_trgm
    ON brew_method
 USING gin(name gin_trgm_ops);
