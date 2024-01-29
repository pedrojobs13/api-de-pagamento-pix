ALTER TABLE
    cliente
    ADD COLUMN
        fk_pagamento_id bigint;


ALTER TABLE
    cliente
    ADD CONSTRAINT fk_pagamento_id FOREIGN KEY (fk_pagamento_id) REFERENCES pagamento (id);
