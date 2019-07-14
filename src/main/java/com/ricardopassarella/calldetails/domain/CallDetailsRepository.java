package com.ricardopassarella.calldetails.domain;

import com.ricardopassarella.calldetails.domain.exception.FailedToInsertCallDetails;
import com.ricardopassarella.calldetails.dto.CallFinanceInsert;
import com.ricardopassarella.calldetails.dto.CallLog;
import com.ricardopassarella.calldetails.dto.CallerDetails;
import com.ricardopassarella.calldetails.dto.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class CallDetailsRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    void insertBatch(List<CallLog> callLog, List<CallFinanceInsert> callFinanceInsert) {
        try {
            insertCallLog(callLog);
            insertCallFinance(callFinanceInsert);
        } catch (DataAccessException e) {
            throw new FailedToInsertCallDetails(e);
        }
    }

    private void insertCallLog(List<CallLog> callLogs) {
        String sql = "insert into call_log " +
                     " (id, caller_id, recipient, call_start, call_end, reference) " +
                     " values " +
                     " (:callLogId, :callerId, :recipient, :callStart, :callEnd, :reference)";

        MapSqlParameterSource[] params = callLogs.stream()
                                                 .map(callLog -> {
                                                     MapSqlParameterSource map = new MapSqlParameterSource();
                                                     map.addValue("callLogId", callLog.getUuid());
                                                     map.addValue("callerId", callLog.getCallerId());
                                                     map.addValue("recipient", callLog.getRecipient());
                                                     map.addValue("callStart", callLog.getCallStart());
                                                     map.addValue("callEnd", callLog.getCallEnd());
                                                     map.addValue("reference", callLog.getReference());
                                                     return map;
                                                 })
                                                 .toArray(MapSqlParameterSource[]::new);
        jdbcTemplate.batchUpdate(sql, params);
    }

    private void insertCallFinance(List<CallFinanceInsert> callFinanceInserts) {
        String sql = "insert into call_finance" +
                     " (id, cost, local_cost, local_currency, exchange_rate, call_log_id) " +
                     " values " +
                     " (uuid(), :cost, :localCost, :localCurrency, :exchangeRate, :callLogId)";

        MapSqlParameterSource[] param = callFinanceInserts.stream()
                                                          .map(callFinanceInsert -> {
                                                              MapSqlParameterSource map = new MapSqlParameterSource();
                                                              map.addValue("cost", callFinanceInsert.getCost());
                                                              map.addValue("localCost", callFinanceInsert.getLocalCost());
                                                              map.addValue("localCurrency", callFinanceInsert.getLocalCurrency());
                                                              map.addValue("exchangeRate", callFinanceInsert.getExchangeRate());
                                                              map.addValue("callLogId", callFinanceInsert.getCallLogId());
                                                              return map;
                                                          })
                                                          .toArray(MapSqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(sql, param);
    }

    Optional<ExchangeRate> getExchangeRate(String currency) {
        String sql = "select er.* from exchange_rate er where er.currency = :currency";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("currency", currency);

        return jdbcTemplate.query(sql, params, rs -> {
            if (rs.next()) {
                return Optional.of(ExchangeRate.builder()
                                               .localCurrency(rs.getString("currency"))
                                               .exchangeRate(BigDecimal.valueOf(rs.getDouble("rate")))
                                               .build());
            }
            return Optional.empty();
        });
    }

    List<CallerDetails> getCallerDetails(String callerId, LocalDateTime from, LocalDateTime to) {
        String sql = " select " +
                     "       cl.call_start, " +
                     "       cl.call_end, " +
                     "       cf.cost " +
                     " from call_log cl " +
                     "         join call_finance cf on cl.id = cf.call_log_id " +
                     " where caller_id = :callerId" +
                     "  and call_end > :from " +
                     "  and call_end < :to ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("callerId", callerId);
        params.addValue("from", from);
        params.addValue("to", to);

        return jdbcTemplate.query(sql, params,
                                  (rs, rowNum) -> CallerDetails.builder()
                                                               .callStart(rs.getObject("call_start", LocalDateTime.class))
                                                               .callEnd(rs.getObject("call_end", LocalDateTime.class))
                                                               .cost(BigDecimal.valueOf(rs.getDouble("cost")))
                                                               .build());
    }

    List<CallLog> getCallLog(String callerId, LocalDateTime from, LocalDateTime to, int limit, int offset) {
        String sql = "select cl.* " +
                     "from call_log cl " +
                     "where cl.caller_id = :callerId " +
                     "  and call_end > :from " +
                     "  and call_end < :to " +
                     "order by cl.call_end ASC " +
                     "limit :limit " +
                     "    offset :offset";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("callerId", callerId);
        params.addValue("from", from);
        params.addValue("to", to);
        params.addValue("limit", limit);
        params.addValue("offset", offset);

        return jdbcTemplate.query(sql, params,
                                  (rs, rowNum) -> CallLog.builder()
                                                         .uuid(rs.getString("id"))
                                                         .callerId(rs.getString("caller_id"))
                                                         .recipient(rs.getString("recipient"))
                                                         .callStart(rs.getObject("call_start", LocalDateTime.class))
                                                         .callEnd(rs.getObject("call_end", LocalDateTime.class))
                                                         .reference(rs.getString("reference"))
                                                         .build());
    }

    Integer getCountCallLog(String callerId, LocalDateTime from, LocalDateTime to) {
        String sql = "select count(1) as count " +
                     "from call_log cl " +
                     "where cl.caller_id = :callerId " +
                     "  and call_end > :from " +
                     "  and call_end < :to ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("callerId", callerId);
        params.addValue("from", from);
        params.addValue("to", to);

        return jdbcTemplate.query(sql, params, rs -> {
            if (rs.next()) {
                return rs.getInt("count");
            }
            return 0;
        });
    }
}
