create TABLE call_log(
    id VARCHAR(36) PRIMARY KEY,
    caller_id VARCHAR(20),
    recipient VARCHAR(20),
    call_start datetime,
    call_end datetime,
    reference VARCHAR(50) NOT NULL UNIQUE
);

create TABLE currency (
    id VARCHAR(3) PRIMARY KEY
);

create TABLE exchange_rate (
  id VARCHAR(36) PRIMARY KEY,
  currency VARCHAR(3),
  rate DOUBLE,

  FOREIGN KEY (currency) references currency(id)
);

create TABLE call_finance (
  id VARCHAR(36) PRIMARY KEY,
  cost DOUBLE,
  local_cost DOUBLE,
  local_currency VARCHAR(3),
  exchange_rate DOUBLE,
  call_log_id VARCHAR(36) NOT NULL UNIQUE,

  FOREIGN KEY (local_currency) references currency(id)
);